package com.example.kukuafya;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class chatFragment extends Fragment {

    // Constants
    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    private static final long TYPING_DELAY = 1500;

    // UI Components
    private RecyclerView chatRecyclerView;
    private MessageAdapter messageAdapter;
    private List<Message> messageList;
    private EditText messageEditText;
    private ImageButton sendButton, voiceButton;
    private Button suggestMeButton, breedInfoButton, diseaseInfoButton, housingInfoButton;
    private ProgressBar typingProgressBar;

    // Engine
    private ChatbotEngineWithAPI chatbotEngine;
    private Handler typingHandler = new Handler(Looper.getMainLooper());
    private Runnable typingRunnable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeViews(view);
        setupChatRecyclerView();
        setupEventListeners();
        showWelcomeMessage();

        // Initialize chatbot with cloud backend
        chatbotEngine = new ChatbotEngineWithAPI(requireActivity());
        Toast.makeText(requireContext(), "Connected to Poultry Assistant", Toast.LENGTH_LONG).show();
    }

    private void initializeViews(View view) {
        chatRecyclerView = view.findViewById(R.id.chatRecyclerView);
        messageEditText = view.findViewById(R.id.messageEditText);
        sendButton = view.findViewById(R.id.sendButton);
        voiceButton = view.findViewById(R.id.voiceButton);
        suggestMeButton = view.findViewById(R.id.suggestMeButton);
        breedInfoButton = view.findViewById(R.id.breedInfoButton);
        diseaseInfoButton = view.findViewById(R.id.diseaseInfoButton);
        housingInfoButton = view.findViewById(R.id.housingInfoButton);
        typingProgressBar = view.findViewById(R.id.typingProgressBar);
    }

    private void setupChatRecyclerView() {
        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        chatRecyclerView.setAdapter(messageAdapter);
    }

    private void setupEventListeners() {
        // Text input listener
        messageEditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean hasText = s.toString().trim().length() > 0;
                sendButton.setVisibility(hasText ? View.VISIBLE : View.GONE);
                voiceButton.setVisibility(hasText ? View.GONE : View.VISIBLE);
            }
        });

        // Send button
        sendButton.setOnClickListener(v -> sendUserMessage());

        // Voice input
        voiceButton.setOnClickListener(v -> startVoiceInput());

        // Quick action buttons
        suggestMeButton.setOnClickListener(v -> sendMessage("Nipatie ushauri wa ufugaji kuku"));
        breedInfoButton.setOnClickListener(v -> sendMessage("Aina gani za kuku zinafaa Kenya?"));
        diseaseInfoButton.setOnClickListener(v -> sendMessage("Dalili za ugonjwa wa Newcastle?"));
        housingInfoButton.setOnClickListener(v -> sendMessage("Mbinu bora za kujenga kuku nyumba"));
    }

    private void sendUserMessage() {
        String message = messageEditText.getText().toString().trim();
        if (!message.isEmpty()) {
            sendMessage(message);
            messageEditText.setText("");
        }
    }

    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        } catch (Exception e) {
            Toast.makeText(requireContext(), "Voice input not supported on this device", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SPEECH_INPUT && resultCode == getActivity().RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && !result.isEmpty()) {
                messageEditText.setText(result.get(0));
                sendButton.setVisibility(View.VISIBLE);
                voiceButton.setVisibility(View.GONE);
            }
        }
    }

    private void sendMessage(String messageText) {
        // Add user message to chat
        messageList.add(new Message(messageText, true));
        messageAdapter.notifyItemInserted(messageList.size() - 1);
        chatRecyclerView.smoothScrollToPosition(messageList.size() - 1);

        // Show typing indicator
        showTypingIndicator();

        // Get bot response
        chatbotEngine.getResponse(messageText, new ChatbotEngineWithAPI.ResponseCallback() {
            @Override
            public void onResponse(String response) {
                requireActivity().runOnUiThread(() -> {
                    hideTypingIndicator();
                    messageList.add(new Message(response, false));
                    messageAdapter.notifyItemInserted(messageList.size() - 1);
                    chatRecyclerView.smoothScrollToPosition(messageList.size() - 1);
                });
            }

            @Override
            public void onError(String error) {
                requireActivity().runOnUiThread(() -> {
                    hideTypingIndicator();
                    Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
                    messageList.add(new Message("Sorry, I couldn't process your request", false));
                    messageAdapter.notifyItemInserted(messageList.size() - 1);
                });
            }
        });
    }

    private void showTypingIndicator() {
        typingProgressBar.setVisibility(View.VISIBLE);
        if (typingRunnable != null) {
            typingHandler.removeCallbacks(typingRunnable);
        }

        typingRunnable = () -> typingProgressBar.setVisibility(View.GONE);
        typingHandler.postDelayed(typingRunnable, TYPING_DELAY);
    }

    private void hideTypingIndicator() {
        if (typingRunnable != null) {
            typingHandler.removeCallbacks(typingRunnable);
        }
        typingProgressBar.setVisibility(View.GONE);
    }

    private void showWelcomeMessage() {
        new Handler().postDelayed(() -> {
            messageList.add(new Message("Hello! How can I help with your poultry farming today?", false));
            messageAdapter.notifyItemInserted(messageList.size() - 1);
        }, 1000);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        typingHandler.removeCallbacksAndMessages(null);
    }
}