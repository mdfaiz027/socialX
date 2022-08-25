package com.example.socialx;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

public class signup_tab_fragment extends Fragment {

    View view;
    EditText name, email, phone_number, password;
    TextView signIn;
    CountryCodePicker cpp;
    CheckBox checkBox;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_signup_tab_fragment, container, false);

        name = view.findViewById(R.id.idETNameField);
        email = view.findViewById(R.id.idETEmailField);
        phone_number = view.findViewById(R.id.phone_number_edt);
        password = view.findViewById(R.id.idPasswordEditText);
        signIn = view.findViewById(R.id.idSignIn);
        checkBox = view.findViewById(R.id.idIAgreeWith);


        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.changeLoginTabColor();
            }
        });

        return view;
    }
}