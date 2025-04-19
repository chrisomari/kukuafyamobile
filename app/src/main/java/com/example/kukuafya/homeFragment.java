package com.example.kukuafya;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class homeFragment extends Fragment {

    private CardView cardChat, cardDiseaseScanner, cardCommunity, cardReports, cardPoultryInfo;
    private TextView tvReadMore, tvPoultryInfoReadMore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        cardChat = view.findViewById(R.id.cardChat);
        cardDiseaseScanner = view.findViewById(R.id.cardDiseaseScanner);
        cardCommunity = view.findViewById(R.id.cardCommunity);
        cardReports = view.findViewById(R.id.cardReports);
        cardPoultryInfo = view.findViewById(R.id.cardPoultryInfo);
        tvReadMore = view.findViewById(R.id.tvReadMore);
        tvPoultryInfoReadMore = view.findViewById(R.id.tvPoultryInfoReadMore);

        // Set up click listeners
        cardChat.setOnClickListener(v -> navigateToChat());
        cardDiseaseScanner.setOnClickListener(v -> navigateToDiseaseScanner());
        cardCommunity.setOnClickListener(v -> navigateToCommunity());
        cardReports.setOnClickListener(v -> navigateToReports());

        // Set up poultry info card click listener
        cardPoultryInfo.setOnClickListener(v -> openPoultryInfoUrl());

        // Optional: Also make the "Visit Website" text clickable
        if (tvPoultryInfoReadMore != null) {
            tvPoultryInfoReadMore.setOnClickListener(v -> openPoultryInfoUrl());
        }

        // Set up spotlight read more link
        tvReadMore.setOnClickListener(v -> openSpotlightUrl());
    }

    private void navigateToChat() {
        // TODO: Navigate to chat screen
        // For example:
        // Intent intent = new Intent(getActivity(), ChatActivity.class);
        // startActivity(intent);
    }

    private void navigateToDiseaseScanner() {
        // TODO: Navigate to disease scanner screen
        // For example:
        // Intent intent = new Intent(getActivity(), DiseaseScannerActivity.class);
        // startActivity(intent);
    }

    private void navigateToCommunity() {
        // TODO: Navigate to community screen
        // For example:
        // Intent intent = new Intent(getActivity(), CommunityActivity.class);
        // startActivity(intent);
    }

    private void navigateToReports() {
        // TODO: Navigate to reports screen
        // For example:
        // Intent intent = new Intent(getActivity(), ReportsActivity.class);
        // startActivity(intent);
    }

    private void openPoultryInfoUrl() {
        // Open the poultry info website in browser
        String url = "https://www.bentoli.com/chicken-problems-common/";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    private void openSpotlightUrl() {
        // Open the KukuAfya website in browser
        String url = "https://www.youtube.com/watch?v=nV7lk3lht60";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }
}