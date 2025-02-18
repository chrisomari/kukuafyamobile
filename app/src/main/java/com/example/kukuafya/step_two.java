package com.example.kukuafya;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Intent.getIntent;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link step_two#newInstance} factory method to
 * create an instance of this fragment.
 */
public class step_two extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public step_two() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment step_two.
     */
    // TODO: Rename and change types and number of parameters
    public static step_two newInstance(String param1, String param2) {
        step_two fragment = new step_two();
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
        return inflater.inflate(R.layout.fragment_step_two, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText e1=view.findViewById(R.id.dateEt);
        EditText e2=view.findViewById(R.id.aboutET);
        Button b1=view.findViewById(R.id.finishbtn);

        DBhandler nd=new DBhandler(getContext());
        SharedPreferences shbh = getContext().getSharedPreferences("reminderData", MODE_PRIVATE);
        String s1 = shbh.getString("flock", "");
        String s2 = shbh.getString("title","");

b1.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

        nd.addNewReminder(s2,s1,e1.getText().toString(),e2.getText().toString());
        Toast.makeText(getContext(), "reminder added", Toast.LENGTH_LONG).show();

    }
});









    }

}