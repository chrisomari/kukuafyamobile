package com.example.kukuafya.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.kukuafya.Answer;
import com.example.kukuafya.Bookmark;
import com.example.kukuafya.Question;
import com.example.kukuafya.Vote;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "poultry_qa.db";
    private static final int DATABASE_VERSION = 1;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    // Table Names
    private static final String TABLE_QUESTIONS = "questions";
    private static final String TABLE_ANSWERS = "answers";
    private static final String TABLE_BOOKMARKS = "bookmarks";
    private static final String TABLE_VOTES = "votes";

    // Common Column Names
    private static final String KEY_ID = "id";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_CREATED_AT = "created_at";

    // QUESTIONS Table - Column Names
    private static final String KEY_TITLE = "title";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_UPVOTES = "upvotes";
    private static final String KEY_DOWNVOTES = "downvotes";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_IS_SOLVED = "is_solved";
    private static final String KEY_REPORT_COUNT = "report_count";

    // ANSWERS Table - Column Names
    private static final String KEY_QUESTION_ID = "question_id";
    private static final String KEY_IS_ACCEPTED = "is_accepted";

    // BOOKMARKS Table - Column Names
    private static final String KEY_ANSWER_ID = "answer_id";

    // VOTES Table - Column Names
    private static final String KEY_IS_UPVOTE = "is_upvote";

    // Create Table Statements
    private static final String CREATE_TABLE_QUESTIONS = "CREATE TABLE " + TABLE_QUESTIONS + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_TITLE + " TEXT,"
            + KEY_CONTENT + " TEXT,"
            + KEY_USER_ID + " TEXT,"
            + KEY_USER_NAME + " TEXT,"
            + KEY_CREATED_AT + " DATETIME,"
            + KEY_UPVOTES + " INTEGER DEFAULT 0,"
            + KEY_DOWNVOTES + " INTEGER DEFAULT 0,"
            + KEY_CATEGORY + " TEXT,"
            + KEY_IS_SOLVED + " INTEGER DEFAULT 0,"
            + KEY_REPORT_COUNT + " INTEGER DEFAULT 0"
            + ")";

    private static final String CREATE_TABLE_ANSWERS = "CREATE TABLE " + TABLE_ANSWERS + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_QUESTION_ID + " INTEGER,"
            + KEY_CONTENT + " TEXT,"
            + KEY_USER_ID + " TEXT,"
            + KEY_USER_NAME + " TEXT,"
            + KEY_CREATED_AT + " DATETIME,"
            + KEY_UPVOTES + " INTEGER DEFAULT 0,"
            + KEY_DOWNVOTES + " INTEGER DEFAULT 0,"
            + KEY_IS_ACCEPTED + " INTEGER DEFAULT 0,"
            + KEY_REPORT_COUNT + " INTEGER DEFAULT 0"
            + ")";

    private static final String CREATE_TABLE_BOOKMARKS = "CREATE TABLE " + TABLE_BOOKMARKS + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_USER_ID + " TEXT,"
            + KEY_QUESTION_ID + " INTEGER,"
            + KEY_ANSWER_ID + " INTEGER DEFAULT -1,"
            + KEY_CREATED_AT + " DATETIME,"
            + "UNIQUE(" + KEY_USER_ID + ", " + KEY_QUESTION_ID + ", " + KEY_ANSWER_ID + ")"
            + ")";

    private static final String CREATE_TABLE_VOTES = "CREATE TABLE " + TABLE_VOTES + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_USER_ID + " TEXT,"
            + KEY_QUESTION_ID + " INTEGER,"
            + KEY_ANSWER_ID + " INTEGER DEFAULT -1,"
            + KEY_IS_UPVOTE + " INTEGER,"
            + "UNIQUE(" + KEY_USER_ID + ", " + KEY_QUESTION_ID + ", " + KEY_ANSWER_ID + ")"
            + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_QUESTIONS);
        db.execSQL(CREATE_TABLE_ANSWERS);
        db.execSQL(CREATE_TABLE_BOOKMARKS);
        db.execSQL(CREATE_TABLE_VOTES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VOTES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKMARKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ANSWERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);
        onCreate(db);
    }

    // Question CRUD Operations
    public long addQuestion(Question question) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, question.getTitle());
        values.put(KEY_CONTENT, question.getContent());
        values.put(KEY_USER_ID, question.getUserId());
        values.put(KEY_USER_NAME, question.getUserName());
        values.put(KEY_CREATED_AT, dateFormat.format(question.getCreatedAt()));
        values.put(KEY_CATEGORY, question.getCategory());
        values.put(KEY_UPVOTES, question.getUpvotes());
        values.put(KEY_DOWNVOTES, question.getDownvotes());
        values.put(KEY_IS_SOLVED, question.isSolved() ? 1 : 0);
        values.put(KEY_REPORT_COUNT, question.getReportCount());

        long id = db.insert(TABLE_QUESTIONS, null, values);
        db.close();
        return id;
    }

    public Question getQuestion(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_QUESTIONS, null, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Question question = new Question();
            question.setId(cursor.getLong(cursor.getColumnIndex(KEY_ID)));
            question.setTitle(cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
            question.setContent(cursor.getString(cursor.getColumnIndex(KEY_CONTENT)));
            question.setUserId(cursor.getString(cursor.getColumnIndex(KEY_USER_ID)));
            question.setUserName(cursor.getString(cursor.getColumnIndex(KEY_USER_NAME)));
            try {
                question.setCreatedAt(dateFormat.parse(cursor.getString(cursor.getColumnIndex(KEY_CREATED_AT))));
            } catch (ParseException e) {
                question.setCreatedAt(new Date());
            }
            question.setUpvotes(cursor.getInt(cursor.getColumnIndex(KEY_UPVOTES)));
            question.setDownvotes(cursor.getInt(cursor.getColumnIndex(KEY_DOWNVOTES)));
            question.setCategory(cursor.getString(cursor.getColumnIndex(KEY_CATEGORY)));
            question.setSolved(cursor.getInt(cursor.getColumnIndex(KEY_IS_SOLVED)) == 1);
            question.setReportCount(cursor.getInt(cursor.getColumnIndex(KEY_REPORT_COUNT)));
            cursor.close();
            return question;
        }
        if (cursor != null) {
            cursor.close();
        }
        return null;
    }

    public List<Question> getAllQuestions() {
        List<Question> questions = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_QUESTIONS +
                " WHERE " + KEY_REPORT_COUNT + " < 5" +
                " ORDER BY " + KEY_CREATED_AT + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Question question = new Question();
                question.setId(cursor.getLong(cursor.getColumnIndex(KEY_ID)));
                question.setTitle(cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
                question.setContent(cursor.getString(cursor.getColumnIndex(KEY_CONTENT)));
                question.setUserId(cursor.getString(cursor.getColumnIndex(KEY_USER_ID)));
                question.setUserName(cursor.getString(cursor.getColumnIndex(KEY_USER_NAME)));
                try {
                    question.setCreatedAt(dateFormat.parse(cursor.getString(cursor.getColumnIndex(KEY_CREATED_AT))));
                } catch (ParseException e) {
                    question.setCreatedAt(new Date());
                }
                question.setUpvotes(cursor.getInt(cursor.getColumnIndex(KEY_UPVOTES)));
                question.setDownvotes(cursor.getInt(cursor.getColumnIndex(KEY_DOWNVOTES)));
                question.setCategory(cursor.getString(cursor.getColumnIndex(KEY_CATEGORY)));
                question.setSolved(cursor.getInt(cursor.getColumnIndex(KEY_IS_SOLVED)) == 1);
                question.setReportCount(cursor.getInt(cursor.getColumnIndex(KEY_REPORT_COUNT)));
                questions.add(question);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return questions;
    }

    public List<Question> getQuestionsByCategory(String category) {
        List<Question> questions = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_QUESTIONS, null, KEY_CATEGORY + "=? AND " + KEY_REPORT_COUNT + " < 5",
                new String[]{category}, null, null, KEY_CREATED_AT + " DESC", null);

        if (cursor.moveToFirst()) {
            do {
                Question question = new Question();
                question.setId(cursor.getLong(cursor.getColumnIndex(KEY_ID)));
                question.setTitle(cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
                question.setContent(cursor.getString(cursor.getColumnIndex(KEY_CONTENT)));
                question.setUserId(cursor.getString(cursor.getColumnIndex(KEY_USER_ID)));
                question.setUserName(cursor.getString(cursor.getColumnIndex(KEY_USER_NAME)));
                try {
                    question.setCreatedAt(dateFormat.parse(cursor.getString(cursor.getColumnIndex(KEY_CREATED_AT))));
                } catch (ParseException e) {
                    question.setCreatedAt(new Date());
                }
                question.setUpvotes(cursor.getInt(cursor.getColumnIndex(KEY_UPVOTES)));
                question.setDownvotes(cursor.getInt(cursor.getColumnIndex(KEY_DOWNVOTES)));
                question.setCategory(cursor.getString(cursor.getColumnIndex(KEY_CATEGORY)));
                question.setSolved(cursor.getInt(cursor.getColumnIndex(KEY_IS_SOLVED)) == 1);
                question.setReportCount(cursor.getInt(cursor.getColumnIndex(KEY_REPORT_COUNT)));
                questions.add(question);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return questions;
    }

    public List<Question> getTopWeeklyQuestions() {
        List<Question> questions = new ArrayList<>();
        // Get date from 7 days ago
        long oneWeekAgo = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000);
        Date weekAgo = new Date(oneWeekAgo);
        String weekAgoStr = dateFormat.format(weekAgo);

        String selectQuery = "SELECT * FROM " + TABLE_QUESTIONS +
                " WHERE " + KEY_CREATED_AT + " >= '" + weekAgoStr + "'" +
                " AND " + KEY_REPORT_COUNT + " < 5" +
                " ORDER BY " + KEY_UPVOTES + " DESC, " + KEY_CREATED_AT + " DESC" +
                " LIMIT 5";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Question question = new Question();
                question.setId(cursor.getLong(cursor.getColumnIndex(KEY_ID)));
                question.setTitle(cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
                question.setContent(cursor.getString(cursor.getColumnIndex(KEY_CONTENT)));
                question.setUserId(cursor.getString(cursor.getColumnIndex(KEY_USER_ID)));
                question.setUserName(cursor.getString(cursor.getColumnIndex(KEY_USER_NAME)));
                try {
                    question.setCreatedAt(dateFormat.parse(cursor.getString(cursor.getColumnIndex(KEY_CREATED_AT))));
                } catch (ParseException e) {
                    question.setCreatedAt(new Date());
                }
                question.setUpvotes(cursor.getInt(cursor.getColumnIndex(KEY_UPVOTES)));
                question.setDownvotes(cursor.getInt(cursor.getColumnIndex(KEY_DOWNVOTES)));
                question.setCategory(cursor.getString(cursor.getColumnIndex(KEY_CATEGORY)));
                question.setSolved(cursor.getInt(cursor.getColumnIndex(KEY_IS_SOLVED)) == 1);
                question.setReportCount(cursor.getInt(cursor.getColumnIndex(KEY_REPORT_COUNT)));
                questions.add(question);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return questions;
    }

    public int updateQuestion(Question question) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, question.getTitle());
        values.put(KEY_CONTENT, question.getContent());
        values.put(KEY_UPVOTES, question.getUpvotes());
        values.put(KEY_DOWNVOTES, question.getDownvotes());
        values.put(KEY_CATEGORY, question.getCategory());
        values.put(KEY_IS_SOLVED, question.isSolved() ? 1 : 0);
        values.put(KEY_REPORT_COUNT, question.getReportCount());

        return db.update(TABLE_QUESTIONS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(question.getId())});
    }

    public void deleteQuestion(long questionId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_QUESTIONS, KEY_ID + " = ?", new String[]{String.valueOf(questionId)});

        // Delete related answers, bookmarks, and votes
        db.delete(TABLE_ANSWERS, KEY_QUESTION_ID + " = ?", new String[]{String.valueOf(questionId)});
        db.delete(TABLE_BOOKMARKS, KEY_QUESTION_ID + " = ?", new String[]{String.valueOf(questionId)});
        db.delete(TABLE_VOTES, KEY_QUESTION_ID + " = ?", new String[]{String.valueOf(questionId)});
        db.close();
    }

    // Answer CRUD Operations
    public long addAnswer(Answer answer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_QUESTION_ID, answer.getQuestionId());
        values.put(KEY_CONTENT, answer.getContent());
        values.put(KEY_USER_ID, answer.getUserId());
        values.put(KEY_USER_NAME, answer.getUserName());
        values.put(KEY_CREATED_AT, dateFormat.format(answer.getCreatedAt()));
        values.put(KEY_UPVOTES, answer.getUpvotes());
        values.put(KEY_DOWNVOTES, answer.getDownvotes());
        values.put(KEY_IS_ACCEPTED, answer.isAccepted() ? 1 : 0);
        values.put(KEY_REPORT_COUNT, answer.getReportCount());

        long id = db.insert(TABLE_ANSWERS, null, values);
        db.close();
        return id;
    }

    public Answer getAnswer(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ANSWERS, null, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Answer answer = new Answer();
            answer.setId(cursor.getLong(cursor.getColumnIndex(KEY_ID)));
            answer.setQuestionId(cursor.getLong(cursor.getColumnIndex(KEY_QUESTION_ID)));
            answer.setContent(cursor.getString(cursor.getColumnIndex(KEY_CONTENT)));
            answer.setUserId(cursor.getString(cursor.getColumnIndex(KEY_USER_ID)));
            answer.setUserName(cursor.getString(cursor.getColumnIndex(KEY_USER_NAME)));
            try {
                answer.setCreatedAt(dateFormat.parse(cursor.getString(cursor.getColumnIndex(KEY_CREATED_AT))));
            } catch (ParseException e) {
                answer.setCreatedAt(new Date());
            }
            answer.setUpvotes(cursor.getInt(cursor.getColumnIndex(KEY_UPVOTES)));
            answer.setDownvotes(cursor.getInt(cursor.getColumnIndex(KEY_DOWNVOTES)));
            answer.setAccepted(cursor.getInt(cursor.getColumnIndex(KEY_IS_ACCEPTED)) == 1);
            answer.setReportCount(cursor.getInt(cursor.getColumnIndex(KEY_REPORT_COUNT)));
            cursor.close();
            return answer;
        }
        if (cursor != null) {
            cursor.close();
        }
        return null;
    }

    public List<Answer> getAnswersForQuestion(long questionId) {
        List<Answer> answers = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // First get accepted answers, then others sorted by upvotes
        String selectQuery = "SELECT * FROM " + TABLE_ANSWERS +
                " WHERE " + KEY_QUESTION_ID + " = " + questionId +
                " AND " + KEY_REPORT_COUNT + " < 5" +
                " ORDER BY " + KEY_IS_ACCEPTED + " DESC, " +
                KEY_UPVOTES + " DESC, " + KEY_CREATED_AT + " DESC";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Answer answer = new Answer();
                answer.setId(cursor.getLong(cursor.getColumnIndex(KEY_ID)));
                answer.setQuestionId(cursor.getLong(cursor.getColumnIndex(KEY_QUESTION_ID)));
                answer.setContent(cursor.getString(cursor.getColumnIndex(KEY_CONTENT)));
                answer.setUserId(cursor.getString(cursor.getColumnIndex(KEY_USER_ID)));
                answer.setUserName(cursor.getString(cursor.getColumnIndex(KEY_USER_NAME)));
                try {
                    answer.setCreatedAt(dateFormat.parse(cursor.getString(cursor.getColumnIndex(KEY_CREATED_AT))));
                } catch (ParseException e) {
                    answer.setCreatedAt(new Date());
                }
                answer.setUpvotes(cursor.getInt(cursor.getColumnIndex(KEY_UPVOTES)));
                answer.setDownvotes(cursor.getInt(cursor.getColumnIndex(KEY_DOWNVOTES)));
                answer.setAccepted(cursor.getInt(cursor.getColumnIndex(KEY_IS_ACCEPTED)) == 1);
                answer.setReportCount(cursor.getInt(cursor.getColumnIndex(KEY_REPORT_COUNT)));
                answers.add(answer);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return answers;
    }

    public int updateAnswer(Answer answer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CONTENT, answer.getContent());
        values.put(KEY_UPVOTES, answer.getUpvotes());
        values.put(KEY_DOWNVOTES, answer.getDownvotes());
        values.put(KEY_IS_ACCEPTED, answer.isAccepted() ? 1 : 0);
        values.put(KEY_REPORT_COUNT, answer.getReportCount());

        return db.update(TABLE_ANSWERS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(answer.getId())});
    }

    public void deleteAnswer(long answerId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ANSWERS, KEY_ID + " = ?", new String[]{String.valueOf(answerId)});

        // Delete related bookmarks and votes
        db.delete(TABLE_BOOKMARKS, KEY_ANSWER_ID + " = ?", new String[]{String.valueOf(answerId)});
        db.delete(TABLE_VOTES, KEY_ANSWER_ID + " = ?", new String[]{String.valueOf(answerId)});
        db.close();
    }

    // Mark an answer as accepted
    public void markAnswerAsAccepted(long answerId, long questionId) {
        SQLiteDatabase db = this.getWritableDatabase();

        // First, unmark all answers for this question
        ContentValues resetValues = new ContentValues();
        resetValues.put(KEY_IS_ACCEPTED, 0);
        db.update(TABLE_ANSWERS, resetValues, KEY_QUESTION_ID + " = ?",
                new String[]{String.valueOf(questionId)});

        // Then mark the selected answer as accepted
        ContentValues acceptValues = new ContentValues();
        acceptValues.put(KEY_IS_ACCEPTED, 1);
        db.update(TABLE_ANSWERS, acceptValues, KEY_ID + " = ?",
                new String[]{String.valueOf(answerId)});

        // Mark the question as solved
        ContentValues solvedValues = new ContentValues();
        solvedValues.put(KEY_IS_SOLVED, 1);
        db.update(TABLE_QUESTIONS, solvedValues, KEY_ID + " = ?",
                new String[]{String.valueOf(questionId)});

        db.close();
    }

    // Bookmark Operations
    public long addBookmark(Bookmark bookmark) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USER_ID, bookmark.getUserId());
        values.put(KEY_QUESTION_ID, bookmark.getQuestionId());
        values.put(KEY_ANSWER_ID, bookmark.getAnswerId());
        values.put(KEY_CREATED_AT, dateFormat.format(bookmark.getCreatedAt()));

        long id = -1;
        try {
            id = db.insertOrThrow(TABLE_BOOKMARKS, null, values);
        } catch (Exception e) {
            // Bookmark already exists
        }
        db.close();
        return id;
    }

    public void removeBookmark(String userId, long questionId, long answerId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BOOKMARKS,
                KEY_USER_ID + " = ? AND " + KEY_QUESTION_ID + " = ? AND " + KEY_ANSWER_ID + " = ?",
                new String[]{userId, String.valueOf(questionId), String.valueOf(answerId)});
        db.close();
    }

    public List<Question> getBookmarkedQuestions(String userId) {
        List<Question> questions = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT q.* FROM " + TABLE_QUESTIONS + " q INNER JOIN " +
                TABLE_BOOKMARKS + " b ON q." + KEY_ID + " = b." + KEY_QUESTION_ID +
                " WHERE b." + KEY_USER_ID + " = '" + userId + "'" +
                " AND b." + KEY_ANSWER_ID + " = -1" +
                " AND q." + KEY_REPORT_COUNT + " < 5" +
                " ORDER BY b." + KEY_CREATED_AT + " DESC";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Question question = new Question();
                question.setId(cursor.getLong(cursor.getColumnIndex(KEY_ID)));
                question.setTitle(cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
                question.setContent(cursor.getString(cursor.getColumnIndex(KEY_CONTENT)));
                question.setUserId(cursor.getString(cursor.getColumnIndex(KEY_USER_ID)));
                question.setUserName(cursor.getString(cursor.getColumnIndex(KEY_USER_NAME)));
                try {
                    question.setCreatedAt(dateFormat.parse(cursor.getString(cursor.getColumnIndex(KEY_CREATED_AT))));
                } catch (ParseException e) {
                    question.setCreatedAt(new Date());
                }
                question.setUpvotes(cursor.getInt(cursor.getColumnIndex(KEY_UPVOTES)));
                question.setDownvotes(cursor.getInt(cursor.getColumnIndex(KEY_DOWNVOTES)));
                question.setCategory(cursor.getString(cursor.getColumnIndex(KEY_CATEGORY)));
                question.setSolved(cursor.getInt(cursor.getColumnIndex(KEY_IS_SOLVED)) == 1);
                question.setReportCount(cursor.getInt(cursor.getColumnIndex(KEY_REPORT_COUNT)));
                questions.add(question);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return questions;
    }

    public List<Answer> getBookmarkedAnswers(String userId) {
        List<Answer> answers = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT a.* FROM " + TABLE_ANSWERS + " a INNER JOIN " +
                TABLE_BOOKMARKS + " b ON a." + KEY_ID + " = b." + KEY_ANSWER_ID +
                " WHERE b." + KEY_USER_ID + " = '" + userId + "'" +
                " AND b." + KEY_ANSWER_ID + " != -1" +
                " AND a." + KEY_REPORT_COUNT + " < 5" +
                " ORDER BY b." + KEY_CREATED_AT + " DESC";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Answer answer = new Answer();
                answer.setId(cursor.getLong(cursor.getColumnIndex(KEY_ID)));
                answer.setQuestionId(cursor.getLong(cursor.getColumnIndex(KEY_QUESTION_ID)));
                answer.setContent(cursor.getString(cursor.getColumnIndex(KEY_CONTENT)));
                answer.setUserId(cursor.getString(cursor.getColumnIndex(KEY_USER_ID)));
                answer.setUserName(cursor.getString(cursor.getColumnIndex(KEY_USER_NAME)));
                try {
                    answer.setCreatedAt(dateFormat.parse(cursor.getString(cursor.getColumnIndex(KEY_CREATED_AT))));
                } catch (ParseException e) {
                    answer.setCreatedAt(new Date());
                }
                answer.setUpvotes(cursor.getInt(cursor.getColumnIndex(KEY_UPVOTES)));
                answer.setDownvotes(cursor.getInt(cursor.getColumnIndex(KEY_DOWNVOTES)));
                answer.setAccepted(cursor.getInt(cursor.getColumnIndex(KEY_IS_ACCEPTED)) == 1);
                answer.setReportCount(cursor.getInt(cursor.getColumnIndex(KEY_REPORT_COUNT)));
                answers.add(answer);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return answers;
    }

    public boolean isBookmarked(String userId, long questionId, long answerId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BOOKMARKS, null,
                KEY_USER_ID + " = ? AND " + KEY_QUESTION_ID + " = ? AND " + KEY_ANSWER_ID + " = ?",
                new String[]{userId, String.valueOf(questionId), String.valueOf(answerId)},
                null, null, null, null);

        boolean isBookmarked = cursor.getCount() > 0;
        cursor.close();
        return isBookmarked;
    }

    // Vote Operations
    public void addOrUpdateVote(Vote vote) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Check if vote already exists
        Cursor cursor = db.query(TABLE_VOTES, null,
                KEY_USER_ID + " = ? AND " + KEY_QUESTION_ID + " = ? AND " + KEY_ANSWER_ID + " = ?",
                new String[]{vote.getUserId(), String.valueOf(vote.getQuestionId()), String.valueOf(vote.getAnswerId())},
                null, null, null, null);

        ContentValues values = new ContentValues();
        values.put(KEY_USER_ID, vote.getUserId());
        values.put(KEY_QUESTION_ID, vote.getQuestionId());
        values.put(KEY_ANSWER_ID, vote.getAnswerId());
        values.put(KEY_IS_UPVOTE, vote.isUpvote() ? 1 : 0);

        if (cursor.getCount() > 0) {
            // Update existing vote
            db.update(TABLE_VOTES, values,
                    KEY_USER_ID + " = ? AND " + KEY_QUESTION_ID + " = ? AND " + KEY_ANSWER_ID + " = ?",
                    new String[]{vote.getUserId(), String.valueOf(vote.getQuestionId()), String.valueOf(vote.getAnswerId())});
        } else {
            // Insert new vote
            db.insert(TABLE_VOTES, null, values);
        }

        cursor.close();

        // Update the vote count on the question or answer
        updateVoteCount(vote);

        db.close();
    }

    public void removeVote(String userId, long questionId, long answerId) {
        SQLiteDatabase db = this.getWritableDatabase();

        // First get the vote to know if it was upvote or downvote
        Cursor cursor = db.query(TABLE_VOTES, null,
                KEY_USER_ID + " = ? AND " + KEY_QUESTION_ID + " = ? AND " + KEY_ANSWER_ID + " = ?",
                new String[]{userId, String.valueOf(questionId), String.valueOf(answerId)},
                null, null, null, null);

        if (cursor.moveToFirst()) {
            boolean wasUpvote = cursor.getInt(cursor.getColumnIndex(KEY_IS_UPVOTE)) == 1;

            // Delete the vote
            db.delete(TABLE_VOTES,
                    KEY_USER_ID + " = ? AND " + KEY_QUESTION_ID + " = ? AND " + KEY_ANSWER_ID + " = ?",
                    new String[]{userId, String.valueOf(questionId), String.valueOf(answerId)});

            // Update the vote count
            if (answerId == -1) {
                // Question vote
                Question question = getQuestion(questionId);
                if (question != null) {
                    if (wasUpvote) {
                        question.decrementUpvote();
                    } else {
                        question.decrementDownvote();
                    }
                    updateQuestion(question);
                }
            } else {
                // Answer vote
                Answer answer = getAnswer(answerId);
                if (answer != null) {
                    if (wasUpvote) {
                        answer.decrementUpvote();
                    } else {
                        answer.decrementDownvote();
                    }
                    updateAnswer(answer);
                }
            }
        }

        cursor.close();
        db.close();
    }

    private void updateVoteCount(Vote vote) {
        if (vote.isQuestionVote()) {
            Question question = getQuestion(vote.getQuestionId());
            if (question != null) {
                // Check if user already voted
                SQLiteDatabase db = this.getReadableDatabase();
                Cursor cursor = db.query(TABLE_VOTES, null,
                        KEY_USER_ID + " = ? AND " + KEY_QUESTION_ID + " = ? AND " + KEY_ANSWER_ID + " = ? AND " + KEY_IS_UPVOTE + " != ?",
                        new String[]{vote.getUserId(), String.valueOf(vote.getQuestionId()), "-1", String.valueOf(vote.isUpvote() ? 1 : 0)},
                        null, null, null, null);

                boolean hadOppositeVote = cursor.getCount() > 0;
                cursor.close();

                if (hadOppositeVote) {
                    // User changed vote type
                    if (vote.isUpvote()) {
                        question.decrementDownvote();
                        question.incrementUpvote();
                    } else {
                        question.decrementUpvote();
                        question.incrementDownvote();
                    }
                } else {
                    // New vote
                    if (vote.isUpvote()) {
                        question.incrementUpvote();
                    } else {
                        question.incrementDownvote();
                    }
                }
                updateQuestion(question);
            }
        } else {
            Answer answer = getAnswer(vote.getAnswerId());
            if (answer != null) {
                // Check if user already voted
                SQLiteDatabase db = this.getReadableDatabase();
                Cursor cursor = db.query(TABLE_VOTES, null,
                        KEY_USER_ID + " = ? AND " + KEY_QUESTION_ID + " = ? AND " + KEY_ANSWER_ID + " = ? AND " + KEY_IS_UPVOTE + " != ?",
                        new String[]{vote.getUserId(), String.valueOf(vote.getQuestionId()), String.valueOf(vote.getAnswerId()), String.valueOf(vote.isUpvote() ? 1 : 0)},
                        null, null, null, null);

                boolean hadOppositeVote = cursor.getCount() > 0;
                cursor.close();

                if (hadOppositeVote) {
                    // User changed vote type
                    if (vote.isUpvote()) {
                        answer.decrementDownvote();
                        answer.incrementUpvote();
                    } else {
                        answer.decrementUpvote();
                        answer.incrementDownvote();
                    }
                } else {
                    // New vote
                    if (vote.isUpvote()) {
                        answer.incrementUpvote();
                    } else {
                        answer.incrementDownvote();
                    }
                }
                updateAnswer(answer);
            }
        }
    }

    public int getUserVoteStatus(String userId, long questionId, long answerId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_VOTES, new String[]{KEY_IS_UPVOTE},
                KEY_USER_ID + " = ? AND " + KEY_QUESTION_ID + " = ? AND " + KEY_ANSWER_ID + " = ?",
                new String[]{userId, String.valueOf(questionId), String.valueOf(answerId)},
                null, null, null, null);

        int voteStatus = 0; // 0 = no vote, 1 = upvote, -1 = downvote

        if (cursor.moveToFirst()) {
            boolean isUpvote = cursor.getInt(cursor.getColumnIndex(KEY_IS_UPVOTE)) == 1;
            voteStatus = isUpvote ? 1 : -1;
        }

        cursor.close();
        return voteStatus;
    }

    // Report Operations
    public void reportQuestion(long questionId) {
        Question question = getQuestion(questionId);
        if (question != null) {
            question.incrementReportCount();
            updateQuestion(question);
        }
    }

    public void reportAnswer(long answerId) {
        Answer answer = getAnswer(answerId);
        if (answer != null) {
            answer.incrementReportCount();
            updateAnswer(answer);
        }
    }
}
