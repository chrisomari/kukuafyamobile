package com.example.kukuafya;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Intent.getIntent;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

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
    private DatePickerDialog dpd;
    private Button b2;
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
        initDatePicker();

        EditText e2=view.findViewById(R.id.aboutET);
         b2=view.findViewById(R.id.datepickerButton);
        Button b1=view.findViewById(R.id.finishbtn);
   b2.setText(gettodaysdate());
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Opendatepicker(view);
            }
        });








        DBhandler nd=new DBhandler(getContext());
        SharedPreferences shbh = getContext().getSharedPreferences("reminderData", MODE_PRIVATE);
        String s1 = shbh.getString("flock", "");
        String s2 = shbh.getString("title","");

           b1.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

        nd.addNewReminder(s2,s1,b2.getText().toString(),e2.getText().toString());
        Toast.makeText(getContext(), "reminder added", Toast.LENGTH_LONG).show();

    }
});









    }

    private String gettodaysdate() {
        Calendar cal=Calendar.getInstance();
        int year=cal.get(Calendar.YEAR);
        int month=cal.get(Calendar.MONTH) + 1;
        int day=cal.get(Calendar.DAY_OF_MONTH);
        return makestringdate(day,month,year);
    }

    private void initDatePicker(){
          DatePickerDialog.OnDateSetListener dsl= new DatePickerDialog.OnDateSetListener() {
              @Override
              public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                     month=month + 1;
                     String date=makestringdate(day,month,year);
                     b2.setText(date);
              }
          };
        Calendar cal=Calendar.getInstance();
       int year=cal.get(Calendar.YEAR);
       int month=cal.get(Calendar.MONTH);
       int day=cal.get(Calendar.DAY_OF_MONTH);
       int style= AlertDialog.THEME_HOLO_LIGHT;
        dpd = new DatePickerDialog(getContext(),style,dsl,year,month,day);

    }

    private String makestringdate(int day, int month,int year) {
              return getformat(month) +" "+ day + " " + year;
    }
    private String getformat(int i){
        String month_name= null;

         if(i == 1)
             month_name="January";
        if(i == 2)
            month_name="February";
        if(i == 3)
            month_name="March";
        if(i == 4)
            month_name="April";
        if(i == 5)
            month_name="May";
        if(i == 6)
            month_name="June";
        if(i == 7)
            month_name="July";
        if(i == 8)
            month_name="August";
        if(i == 9)
            month_name="September";
        if(i == 10)
            month_name="October";
        if(i == 11)
            month_name="November";

        if(i == 12)
            month_name="December";

     return month_name;
    }

    private void Opendatepicker(View view){
        dpd.show();;
    }
}