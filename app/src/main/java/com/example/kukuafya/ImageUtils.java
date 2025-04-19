package com.example.kukuafya;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtils {
    private static final String TAG = "ImageUtils";
    private static final int TARGET_SIZE = 224; // Model requires 224x224 images

    public static Bitmap loadAndPrepareImage(Context context, Uri imageUri) {
        try {
            // Take persistable URI permission if possible
            takePersistableUriPermission(context, imageUri);

            // First decode with inJustDecodeBounds=true to check dimensions
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            decodeBounds(context, imageUri, options);

            // Calculate inSampleSize to load smaller version into memory
            options.inSampleSize = calculateInSampleSize(options, TARGET_SIZE, TARGET_SIZE);
            options.inJustDecodeBounds = false;

            // Try normal loading methods first
            Bitmap bitmap = tryNormalLoad(context, imageUri, options);
            if (bitmap != null) {
                return processLoadedBitmap(context, imageUri, bitmap);
            }

            // If normal loading failed, try emergency fallback
            return tryEmergencyLoad(context, imageUri);
        } catch (OutOfMemoryError oom) {
            Log.e(TAG, "Out of memory error", oom);
            System.gc();
            return null;
        } catch (Exception e) {
            Log.e(TAG, "Error loading image", e);
            return null;
        }
    }

    private static void takePersistableUriPermission(Context context, Uri uri) {
        if ("content".equals(uri.getScheme())) {
            try {
                context.getContentResolver().takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                );
            } catch (SecurityException e) {
                Log.w(TAG, "Couldn't take persistable URI permission", e);
            }
        }
    }

    private static void decodeBounds(Context context, Uri uri, BitmapFactory.Options options) {
        InputStream is = null;
        try {
            is = context.getContentResolver().openInputStream(uri);
            BitmapFactory.decodeStream(is, null, options);
        } catch (Exception e) {
            Log.e(TAG, "Error decoding bounds", e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    Log.e(TAG, "Error closing stream", e);
                }
            }
        }
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    private static Bitmap tryNormalLoad(Context context, Uri imageUri, BitmapFactory.Options options) {
        Bitmap bitmap = tryParcelFileDescriptorLoad(context, imageUri, options);
        if (bitmap != null) return bitmap;

        bitmap = tryDecodeStreamLoad(context, imageUri, options);
        if (bitmap != null) return bitmap;

        bitmap = tryMediaStoreLoad(context, imageUri);
        if (bitmap != null) return bitmap;

        bitmap = tryFilePathLoad(context, imageUri, options);
        if (bitmap != null) return bitmap;

        return tryTempFileLoad(context, imageUri, options);
    }

    private static Bitmap tryParcelFileDescriptorLoad(Context context, Uri uri, BitmapFactory.Options options) {
        ParcelFileDescriptor pfd = null;
        try {
            pfd = context.getContentResolver().openFileDescriptor(uri, "r");
            if (pfd != null) {
                FileDescriptor fd = pfd.getFileDescriptor();
                return BitmapFactory.decodeFileDescriptor(fd, null, options);
            }
        } catch (Exception e) {
            Log.w(TAG, "ParcelFileDescriptor load failed", e);
        } finally {
            if (pfd != null) {
                try {
                    pfd.close();
                } catch (IOException e) {
                    Log.e(TAG, "Error closing ParcelFileDescriptor", e);
                }
            }
        }
        return null;
    }

    private static Bitmap tryDecodeStreamLoad(Context context, Uri uri, BitmapFactory.Options options) {
        InputStream is = null;
        try {
            is = context.getContentResolver().openInputStream(uri);
            return BitmapFactory.decodeStream(is, null, options);
        } catch (Exception e) {
            Log.w(TAG, "DecodeStream load failed", e);
            return null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    Log.e(TAG, "Error closing InputStream", e);
                }
            }
        }
    }

    private static Bitmap tryMediaStoreLoad(Context context, Uri uri) {
        if ("content".equals(uri.getScheme())) {
            try {
                return MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            } catch (Exception e) {
                Log.w(TAG, "MediaStore load failed", e);
            }
        }
        return null;
    }

    private static Bitmap tryFilePathLoad(Context context, Uri uri, BitmapFactory.Options options) {
        String filePath = getPathFromUri(context, uri);
        if (filePath != null) {
            return BitmapFactory.decodeFile(filePath, options);
        }
        return null;
    }

    private static Bitmap tryTempFileLoad(Context context, Uri uri, BitmapFactory.Options options) {
        File tempFile = createTempFileFromUri(context, uri);
        if (tempFile != null) {
            try {
                return BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);
            } finally {
                tempFile.delete();
            }
        }
        return null;
    }

    private static Bitmap processLoadedBitmap(Context context, Uri imageUri, Bitmap bitmap) {
        if (bitmap == null) return null;

        Bitmap orientedBitmap = correctOrientation(context, imageUri, bitmap);
        if (orientedBitmap != bitmap) {
            bitmap.recycle();
            bitmap = orientedBitmap;
        }

        Bitmap resizedBitmap = resizeToTargetSize(bitmap);
        if (resizedBitmap != bitmap) {
            bitmap.recycle();
        }

        return resizedBitmap;
    }

    private static Bitmap tryEmergencyLoad(Context context, Uri imageUri) {
        try {
            InputStream is = context.getContentResolver().openInputStream(imageUri);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);
            is.close();

            return processLoadedBitmap(context, imageUri, bitmap);
        } catch (Exception e) {
            Log.e(TAG, "Emergency load failed", e);
            return null;
        }
    }

    private static Bitmap resizeToTargetSize(Bitmap bitmap) {
        if (bitmap == null) return null;

        if (bitmap.getWidth() == TARGET_SIZE && bitmap.getHeight() == TARGET_SIZE) {
            return bitmap;
        }

        return Bitmap.createScaledBitmap(bitmap, TARGET_SIZE, TARGET_SIZE, true);
    }

    private static String getPathFromUri(Context context, Uri uri) {
        try {
            if ("file".equals(uri.getScheme())) {
                return uri.getPath();
            }

            if ("content".equals(uri.getScheme())) {
                String[] projection = {MediaStore.Images.Media.DATA};
                Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
                if (cursor != null) {
                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    if (cursor.moveToFirst()) {
                        String path = cursor.getString(columnIndex);
                        cursor.close();
                        return path;
                    }
                    cursor.close();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getting path from URI", e);
        }
        return null;
    }

    private static File createTempFileFromUri(Context context, Uri uri) {
        File tempFile = null;
        FileOutputStream outputStream = null;
        InputStream inputStream = null;

        try {
            inputStream = context.getContentResolver().openInputStream(uri);
            if (inputStream == null) return null;

            tempFile = File.createTempFile("img_", ".jpg", context.getCacheDir());
            outputStream = new FileOutputStream(tempFile);

            byte[] buffer = new byte[4 * 1024];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            outputStream.flush();

            return tempFile;
        } catch (Exception e) {
            Log.e(TAG, "Error creating temp file", e);
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
            return null;
        } finally {
            try {
                if (inputStream != null) inputStream.close();
                if (outputStream != null) outputStream.close();
            } catch (IOException e) {
                Log.e(TAG, "Error closing streams", e);
            }
        }
    }

    private static Bitmap correctOrientation(Context context, Uri photoUri, Bitmap bitmap) {
        try {
            int orientation = getOrientation(context, photoUri);

            if (orientation != 0) {
                Matrix matrix = new Matrix();
                matrix.postRotate(orientation);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            }

            return bitmap;
        } catch (Exception e) {
            Log.e(TAG, "Error correcting orientation", e);
            return bitmap;
        }
    }

    private static int getOrientation(Context context, Uri photoUri) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                try (InputStream inputStream = context.getContentResolver().openInputStream(photoUri)) {
                    if (inputStream != null) {
                        ExifInterface ei = new ExifInterface(inputStream);
                        int orientation = ei.getAttributeInt(
                                ExifInterface.TAG_ORIENTATION,
                                ExifInterface.ORIENTATION_NORMAL);

                        switch (orientation) {
                            case ExifInterface.ORIENTATION_ROTATE_90:
                                return 90;
                            case ExifInterface.ORIENTATION_ROTATE_180:
                                return 180;
                            case ExifInterface.ORIENTATION_ROTATE_270:
                                return 270;
                            default:
                                return 0;
                        }
                    }
                }
            }

            if ("content".equals(photoUri.getScheme())) {
                String[] projection = {MediaStore.Images.ImageColumns.ORIENTATION};
                Cursor cursor = context.getContentResolver().query(photoUri, projection, null, null, null);

                if (cursor != null && cursor.moveToFirst()) {
                    int orientationColumnIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.ORIENTATION);
                    if (orientationColumnIndex != -1) {
                        int orientation = cursor.getInt(orientationColumnIndex);
                        cursor.close();
                        return orientation;
                    }
                    cursor.close();
                }

                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                cursor = context.getContentResolver().query(photoUri, filePathColumn, null, null, null);

                if (cursor != null && cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    if (columnIndex != -1) {
                        String filePath = cursor.getString(columnIndex);
                        cursor.close();

                        if (filePath != null) {
                            ExifInterface ei = new ExifInterface(filePath);
                            int orientation = ei.getAttributeInt(
                                    ExifInterface.TAG_ORIENTATION,
                                    ExifInterface.ORIENTATION_NORMAL);

                            switch (orientation) {
                                case ExifInterface.ORIENTATION_ROTATE_90:
                                    return 90;
                                case ExifInterface.ORIENTATION_ROTATE_180:
                                    return 180;
                                case ExifInterface.ORIENTATION_ROTATE_270:
                                    return 270;
                                default:
                                    return 0;
                            }
                        }
                    } else {
                        cursor.close();
                    }
                }
            }

            return 0;
        } catch (Exception e) {
            Log.e(TAG, "Error getting orientation", e);
            return 0;
        }
    }
}