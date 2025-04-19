package com.example.kukuafya;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link step_one#newInstance} factory method to
 * create an instance of this fragment.
 */
public class step_one extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public step_one() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment step_one.
     */
    // TODO: Rename and change types and number of parameters
    public static step_one newInstance(String param1, String param2) {
        step_one fragment = new step_one();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_step_one, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Spinner one=view.findViewById(R.id.flockspinner);
        Spinner two=view.findViewById(R.id.titlespinner);







        one.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

              String ifr= adapterView.getItemAtPosition(i).toString();
                SharedPreferences sh = getContext().getSharedPreferences("reminderData", MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sh.edit();

                // write all the data entered by the user in SharedPreference and apply
                myEdit.putString("flock", ifr);
                myEdit.apply();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        two.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            String bn=adapterView.getItemAtPosition(i).toString();
            // Fetching the stored data from the SharedPreference
            SharedPreferences sh = getContext().getSharedPreferences("reminderData", MODE_PRIVATE);
            SharedPreferences.Editor myEdit = sh.edit();

            // write all the data entered by the user in SharedPreference and apply
            myEdit.putString("title", bn);
            myEdit.apply();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
});





    }
}