package com.example.kukuafya;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.kukuafya.adapters.QuestionAdapter;
import com.example.kukuafya.fragments.QuestionDetailFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class notesFragment extends Fragment implements QuestionAdapter.OnQuestionClickListener, CategoryAdapter.OnCategoryClickListener {

    private RecyclerView rvQuestions;
    private RecyclerView rvCategories;
    private SwipeRefreshLayout swipeRefresh;
    private FloatingActionButton fabAddQuestion;
    private EditText etSearch;
    private TabLayout tabLayout;
    private TextView tvEmptyState;

    private QuestionAdapter questionAdapter;
    private CategoryAdapter categoryAdapter;
    private DatabaseHelper dbHelper;
    private List<Question> questionList;
    private List<String> categoryList;

    // User information from SharedPreferences
    private String currentUserId;
    private String currentUserName;
    private boolean isLoggedIn = false;

    private int currentTab = 0; // 0 = All, 1 = Bookmarks, 2 = Weekly Top
    private String currentCategory = "All";
    private String searchQuery = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        View view = inflater.inflate(R.layout.fragment_notes, container, false);

        // If not logged in, return the view early
        if (!isLoggedIn) {
            return view;
        }

        // Initialize views
        rvQuestions = view.findViewById(R.id.rv_questions);
        rvCategories = view.findViewById(R.id.rv_categories);
        swipeRefresh = view.findViewById(R.id.swipe_refresh);
        fabAddQuestion = view.findViewById(R.id.fab_add_question);
        etSearch = view.findViewById(R.id.et_search);
        tabLayout = view.findViewById(R.id.tab_layout);
        tvEmptyState = view.findViewById(R.id.tv_empty_state);

        // Initialize database helper
        dbHelper = new DatabaseHelper(requireContext());

        // Initialize question list
        questionList = new ArrayList<>();
        questionAdapter = new QuestionAdapter(requireContext(), questionList, currentUserId, this);
        rvQuestions.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvQuestions.setAdapter(questionAdapter);

        // Initialize category list
        categoryList = new ArrayList<>(Arrays.asList(
                "All", "Disease", "Feeding", "Housing", "Vaccination", "Business"
        ));
        categoryAdapter = new CategoryAdapter(requireContext(), categoryList, this);
        rvCategories.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        rvCategories.setAdapter(categoryAdapter);

        // Set up tabs
        setupTabs();

        // Set up search
        setupSearch();

        // Set up swipe refresh
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadQuestions();
            }
        });

        // Set up FAB
        fabAddQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddQuestionDialog();
            }
        });

        // Load initial data
        loadQuestions();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Check login status again when resuming
        checkLoginStatus();
        if (isLoggedIn && dbHelper != null) {
            loadQuestions();
        }
    }

    private void setupTabs() {
        tabLayout.addTab(tabLayout.newTab().setText("All Questions"));
        tabLayout.addTab(tabLayout.newTab().setText("Bookmarked"));
        tabLayout.addTab(tabLayout.newTab().setText("Weekly Top"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentTab = tab.getPosition();
                loadQuestions();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Not needed
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Not needed
            }
        });
    }

    private void setupSearch() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchQuery = s.toString().toLowerCase().trim();
                filterQuestions();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not needed
            }
        });
    }

    private void filterQuestions() {
        if (!searchQuery.isEmpty()) {
            List<Question> filteredQuestions = new ArrayList<>();
            for (Question question : getAllQuestions()) {
                if (question.getTitle().toLowerCase().contains(searchQuery) ||
                        question.getContent().toLowerCase().contains(searchQuery)) {
                    filteredQuestions.add(question);
                }
            }
            updateQuestionList(filteredQuestions);
        } else {
            loadQuestions();
        }
    }

    private List<com.example.kukuafya.Question> getAllQuestions() {
        switch (currentTab) {
            case 0: // All Questions
                if (currentCategory.equals("All")) {
                    return dbHelper.getAllQuestions();
                } else {
                    return dbHelper.getQuestionsByCategory(currentCategory);
                }
            case 1: // Bookmarked
                return dbHelper.getBookmarkedQuestions(currentUserId);
            case 2: // Weekly Top
                return dbHelper.getTopWeeklyQuestions();
            default:
                return new ArrayList<>();
        }
    }

    private void updateQuestionList(List<com.example.kukuafya.Question> questions) {
        questionList.clear();
        questionList.addAll(questions);
        questionAdapter.notifyDataSetChanged();

        // Show empty state if needed
        if (questionList.isEmpty()) {
            tvEmptyState.setVisibility(View.VISIBLE);
            if (currentTab == 1) {
                tvEmptyState.setText(R.string.no_bookmarks);
            } else if (!searchQuery.isEmpty()) {
                tvEmptyState.setText(R.string.no_search_results);
            } else {
                tvEmptyState.setText(R.string.no_questions);
            }
        } else {
            tvEmptyState.setVisibility(View.GONE);
        }
    }

    private void loadQuestions() {
        updateQuestionList(getAllQuestions());
        swipeRefresh.setRefreshing(false);
    }

    private void showAddQuestionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_question, null);
        builder.setView(dialogView);

        EditText etTitle = dialogView.findViewById(R.id.et_question_title);
        EditText etContent = dialogView.findViewById(R.id.et_question_content);
        Spinner spinnerCategory = dialogView.findViewById(R.id.spinner_category);
        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);
        Button btnSubmit = dialogView.findViewById(R.id.btn_submit);

        // Set up category spinner
        List<String> categories = new ArrayList<>(categoryList);
        categories.remove(0); // Remove "All" option
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                requireContext(), android.R.layout.simple_spinner_item, categories);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(spinnerAdapter);

        final AlertDialog dialog = builder.create();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = etTitle.getText().toString().trim();
                String content = etContent.getText().toString().trim();
                String category = spinnerCategory.getSelectedItem().toString();

                if (title.isEmpty() || content.isEmpty()) {
                    Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                Question question = new Question();
                question.setTitle(title);
                question.setContent(content);
                question.setUserId(currentUserId);
                question.setUserName(currentUserName);
                question.setCategory(category);

                long id = dbHelper.addQuestion(question);
                if (id > 0) {
                    Toast.makeText(requireContext(), "Question posted successfully", Toast.LENGTH_SHORT).show();
                    // Reset to All Questions tab to see the new post
                    tabLayout.selectTab(tabLayout.getTabAt(0));
                    currentTab = 0;
                    currentCategory = "All";
                    categoryAdapter.setSelectedPosition(0);
                    loadQuestions();
                    dialog.dismiss();
                } else {
                    Toast.makeText(requireContext(), "Failed to post question", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }

    @Override
    public void onQuestionClick(com.example.kukuafya.Question question) {
        // Navigate to question detail
        QuestionDetailFragment detailFragment = QuestionDetailFragment.newInstance(question.getId());
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, detailFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onUpvoteClick(com.example.kukuafya.Question question) {
        int currentVoteStatus = dbHelper.getUserVoteStatus(currentUserId, question.getId(), -1);

        if (currentVoteStatus == 1) {
            // Already upvoted, remove vote
            dbHelper.removeVote(currentUserId, question.getId(), -1);
        } else {
            // Not upvoted, add upvote
            com.example.kukuafya.Vote vote = new com.example.kukuafya.Vote();
            vote.setUserId(currentUserId);
            vote.setQuestionId(question.getId());
            vote.setAnswerId(-1);
            vote.setUpvote(true);
            dbHelper.addOrUpdateVote(vote);
        }

        loadQuestions();
    }

    @Override
    public void onDownvoteClick(com.example.kukuafya.Question question) {
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

        loadQuestions();
    }

    @Override
    public void onBookmarkClick(Question question, boolean isBookmarked) {
        if (isBookmarked) {
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

        loadQuestions();
    }

    @Override
    public void onDeleteClick(com.example.kukuafya.Question question) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete Question")
                .setMessage("Are you sure you want to delete this question?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    dbHelper.deleteQuestion(question.getId());
                    Toast.makeText(requireContext(), "Question deleted", Toast.LENGTH_SHORT).show();
                    loadQuestions();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onCategoryClick(String category, int position) {
        currentCategory = category;
        loadQuestions();
    }
}
