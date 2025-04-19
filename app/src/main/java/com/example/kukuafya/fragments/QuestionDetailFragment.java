package com.example.kukuafya.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kukuafya.Answer;
import com.example.kukuafya.Bookmark;
import com.example.kukuafya.DatabaseHelper;
import com.example.kukuafya.Question;
import com.example.kukuafya.R;
import com.example.kukuafya.Vote;
import com.example.kukuafya.adapters.AnswerAdapter;
import com.example.kukuafya.sign_in;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class QuestionDetailFragment extends Fragment implements AnswerAdapter.OnAnswerClickListener {

    private static final String ARG_QUESTION_ID = "question_id";

    private long questionId;
    private Question question;
    private DatabaseHelper dbHelper;
    private List<Answer> answerList;
    private AnswerAdapter answerAdapter;

    // User information from SharedPreferences
    private String currentUserId;
    private String currentUserName;
    private boolean isLoggedIn = false;

    private TextView tvTitle, tvContent, tvUsername, tvCategory, tvDate, tvUpvotes;
    private ImageButton btnUpvote, btnDownvote, btnBookmark, btnReport;
    private ImageView ivSolved;
    private RecyclerView rvAnswers;
    private EditText etAnswer;
    private Button btnSubmitAnswer;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());

    public static QuestionDetailFragment newInstance(long questionId) {
        QuestionDetailFragment fragment = new QuestionDetailFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_QUESTION_ID, questionId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            questionId = getArguments().getLong(ARG_QUESTION_ID);
        }
        dbHelper = new DatabaseHelper(requireContext());

        // Check login status
        checkLoginStatus();
    }

    private void checkLoginStatus() {
        SharedPreferences loginPrefs = requireActivity().getSharedPreferences("logininfo", Context.MODE_PRIVATE);
        String loginStatus = loginPrefs.getString("status", "");
        isLoggedIn = "logged in".equals(loginStatus);

        if (isLoggedIn) {
            // Get user information
            currentUserName = loginPrefs.getString("username", "Anonymous User");
            currentUserId = loginPrefs.getString("email", "anonymous");
        } else {
            // Redirect to sign in
            Intent intent = new Intent(requireActivity(), sign_in.class);
            startActivity(intent);
            Toast.makeText(requireActivity(), "Please sign in to use the forum", Toast.LENGTH_LONG).show();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question_detail, container, false);

        // If not logged in, return the view early
        if (!isLoggedIn) {
            return view;
        }

        // Initialize views
        tvTitle = view.findViewById(R.id.tv_question_title);
        tvContent = view.findViewById(R.id.tv_question_content);
        tvUsername = view.findViewById(R.id.tv_question_username);
        tvCategory = view.findViewById(R.id.tv_question_category);
        tvDate = view.findViewById(R.id.tv_question_date);
        tvUpvotes = view.findViewById(R.id.tv_question_upvotes);
        btnUpvote = view.findViewById(R.id.btn_question_upvote);
        btnDownvote = view.findViewById(R.id.btn_question_downvote);
        btnBookmark = view.findViewById(R.id.btn_question_bookmark);
        btnReport = view.findViewById(R.id.btn_question_report);
        ivSolved = view.findViewById(R.id.iv_question_solved);
        rvAnswers = view.findViewById(R.id.rv_answers);
        etAnswer = view.findViewById(R.id.et_answer);
        btnSubmitAnswer = view.findViewById(R.id.btn_submit_answer);

        // Set up back button
        view.findViewById(R.id.btn_back).setOnClickListener(v -> requireActivity().onBackPressed());

        // Load question data
        loadQuestionData();

        // Set up answers recycler view
        answerList = new ArrayList<>();
        answerAdapter = new AnswerAdapter(requireContext(), answerList, currentUserId, question.getUserId(), this);
        rvAnswers.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvAnswers.setAdapter(answerAdapter);

        // Load answers
        loadAnswers();

        // Set up click listeners
        setupClickListeners();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Check login status again when resuming
        checkLoginStatus();
        if (isLoggedIn) {
            loadQuestionData();
            loadAnswers();
        }
    }

    private void loadQuestionData() {
        question = dbHelper.getQuestion(questionId);
        if (question == null) {
            Toast.makeText(requireContext(), "Question not found", Toast.LENGTH_SHORT).show();
            requireActivity().onBackPressed();
            return;
        }

        tvTitle.setText(question.getTitle());
        tvContent.setText(question.getContent());
        tvUsername.setText(question.getUserName());
        tvCategory.setText(question.getCategory());
        tvDate.setText(dateFormat.format(question.getCreatedAt()));
        tvUpvotes.setText(String.valueOf(question.getUpvotes()));

        // Set solved status
        if (question.isSolved()) {
            ivSolved.setVisibility(View.VISIBLE);
        } else {
            ivSolved.setVisibility(View.GONE);
        }

        // Set vote status
        updateVoteUI();

        // Set bookmark status
        updateBookmarkUI();
    }

    private void loadAnswers() {
        List<Answer> answers = dbHelper.getAnswersForQuestion(questionId);
        answerList.clear();
        answerList.addAll(answers);
        answerAdapter.notifyDataSetChanged();
    }

    private void setupClickListeners() {
        btnUpvote.setOnClickListener(v -> {
            int currentVoteStatus = dbHelper.getUserVoteStatus(currentUserId, question.getId(), -1);

            if (currentVoteStatus == 1) {
                // Already upvoted, remove vote
                dbHelper.removeVote(currentUserId, question.getId(), -1);
            } else {
                // Not upvoted, add upvote
                Vote vote = new Vote();
                vote.setUserId(currentUserId);
                vote.setQuestionId(question.getId());
                vote.setAnswerId(-1);
                vote.setUpvote(true);
                dbHelper.addOrUpdateVote(vote);
            }

            // Refresh question data
            question = dbHelper.getQuestion(questionId);
            tvUpvotes.setText(String.valueOf(question.getUpvotes()));
            updateVoteUI();
        });

        btnDownvote.setOnClickListener(v -> {
            int currentVoteStatus = dbHelper.getUserVoteStatus(currentUserId, question.getId(), -1);

            if (currentVoteStatus == -1) {
                // Already downvoted, remove vote
                dbHelper.removeVote(currentUserId, question.getId(), -1);
            } else {
                // Not downvoted, add downvote
                Vote vote = new Vote();
                vote.setUserId(currentUserId);
                vote.setQuestionId(question.getId());
                vote.setAnswerId(-1);
                vote.setUpvote(false);
                dbHelper.addOrUpdateVote(vote);
            }

            // Refresh question data
            question = dbHelper.getQuestion(questionId);
            tvUpvotes.setText(String.valueOf(question.getUpvotes()));
            updateVoteUI();
        });

        btnBookmark.setOnClickListener(v -> {
            boolean isBookmarked = dbHelper.isBookmarked(currentUserId, question.getId(), -1);

            if (!isBookmarked) {
                Bookmark bookmark = new Bookmark();
                bookmark.setUserId(currentUserId);
                bookmark.setQuestionId(question.getId());
                bookmark.setAnswerId(-1);
                dbHelper.addBookmark(bookmark);
                Toast.makeText(requireContext(), "Question bookmarked", Toast.LENGTH_SHORT).show();
            } else {
                dbHelper.removeBookmark(currentUserId, question.getId(), -1);
                Toast.makeText(requireContext(), "Bookmark removed", Toast.LENGTH_SHORT).show();
            }

            updateBookmarkUI();
        });

        btnReport.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Report Question")
                    .setMessage("Are you sure you want to report this question?")
                    .setPositiveButton("Report", (dialog, which) -> {
                        dbHelper.reportQuestion(question.getId());
                        Toast.makeText(requireContext(), "Question reported", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        btnSubmitAnswer.setOnClickListener(v -> {
            String content = etAnswer.getText().toString().trim();
            if (content.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter your answer", Toast.LENGTH_SHORT).show();
                return;
            }

            Answer answer = new Answer();
            answer.setQuestionId(questionId);
            answer.setContent(content);
            answer.setUserId(currentUserId);
            answer.setUserName(currentUserName);

            long id = dbHelper.addAnswer(answer);
            if (id > 0) {
                Toast.makeText(requireContext(), "Answer posted successfully", Toast.LENGTH_SHORT).show();
                etAnswer.setText("");
                loadAnswers();
            } else {
                Toast.makeText(requireContext(), "Failed to post answer", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateVoteUI() {
        int voteStatus = dbHelper.getUserVoteStatus(currentUserId, question.getId(), -1);
        if (voteStatus == 1) {
            btnUpvote.setColorFilter(requireContext().getResources().getColor(R.color.colorAccent));
            btnDownvote.setColorFilter(requireContext().getResources().getColor(R.color.colorGray));
        } else if (voteStatus == -1) {
            btnUpvote.setColorFilter(requireContext().getResources().getColor(R.color.colorGray));
            btnDownvote.setColorFilter(requireContext().getResources().getColor(R.color.colorAccent));
        } else {
            btnUpvote.setColorFilter(requireContext().getResources().getColor(R.color.colorGray));
            btnDownvote.setColorFilter(requireContext().getResources().getColor(R.color.colorGray));
        }
    }

    private void updateBookmarkUI() {
        boolean isBookmarked = dbHelper.isBookmarked(currentUserId, question.getId(), -1);
        if (isBookmarked) {
            btnBookmark.setImageResource(R.drawable.ic_bookmark_filled);
        } else {
            btnBookmark.setImageResource(R.drawable.ic_bookmark_outline);
        }
    }

    @Override
    public void onUpvoteClick(Answer answer) {
        int currentVoteStatus = dbHelper.getUserVoteStatus(currentUserId, answer.getQuestionId(), answer.getId());

        if (currentVoteStatus == 1) {
            // Already upvoted, remove vote
            dbHelper.removeVote(currentUserId, answer.getQuestionId(), answer.getId());
        } else {
            // Not upvoted, add upvote
            Vote vote = new Vote();
            vote.setUserId(currentUserId);
            vote.setQuestionId(answer.getQuestionId());
            vote.setAnswerId(answer.getId());
            vote.setUpvote(true);
            dbHelper.addOrUpdateVote(vote);
        }

        loadAnswers();
    }

    @Override
    public void onDownvoteClick(Answer answer) {
        int currentVoteStatus = dbHelper.getUserVoteStatus(currentUserId, answer.getQuestionId(), answer.getId());

        if (currentVoteStatus == -1) {
            // Already downvoted, remove vote
            dbHelper.removeVote(currentUserId, answer.getQuestionId(), answer.getId());
        } else {
            // Not downvoted, add downvote
            Vote vote = new Vote();
            vote.setUserId(currentUserId);
            vote.setQuestionId(answer.getQuestionId());
            vote.setAnswerId(answer.getId());
            vote.setUpvote(false);
            dbHelper.addOrUpdateVote(vote);
        }

        loadAnswers();
    }

    @Override
    public void onBookmarkClick(Answer answer, boolean isBookmarked) {
        if (isBookmarked) {
            Bookmark bookmark = new Bookmark();
            bookmark.setUserId(currentUserId);
            bookmark.setQuestionId(answer.getQuestionId());
            bookmark.setAnswerId(answer.getId());
            dbHelper.addBookmark(bookmark);
            Toast.makeText(requireContext(), "Answer bookmarked", Toast.LENGTH_SHORT).show();
        } else {
            dbHelper.removeBookmark(currentUserId, answer.getQuestionId(), answer.getId());
            Toast.makeText(requireContext(), "Bookmark removed", Toast.LENGTH_SHORT).show();
        }

        loadAnswers();
    }

    @Override
    public void onAcceptClick(Answer answer) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Accept Answer")
                .setMessage("Are you sure you want to mark this answer as accepted?")
                .setPositiveButton("Accept", (dialog, which) -> {
                    dbHelper.markAnswerAsAccepted(answer.getId(), answer.getQuestionId());
                    Toast.makeText(requireContext(), "Answer accepted", Toast.LENGTH_SHORT).show();
                    loadQuestionData();
                    loadAnswers();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
    // Just find the bottom nav like you mentioned

    // Set up your navigation as needed
    @Override
    public void onDeleteClick(Answer answer) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete Answer")
                .setMessage("Are you sure you want to delete this answer?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    dbHelper.deleteAnswer(answer.getId());
                    Toast.makeText(requireContext(), "Answer deleted", Toast.LENGTH_SHORT).show();
                    loadAnswers();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

        @Override
        public void onReportClick(Answer answer) {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Report Answer")
                    .setMessage("Are you sure you want to report this answer?")
                    .setPositiveButton("Report", (dialog, which) -> {
                        dbHelper.reportAnswer(answer.getId());
                        Toast.makeText(requireContext(), "Answer reported", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        }
    }
