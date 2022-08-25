package com.example.socialx;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

public class login_tab_fragment extends Fragment {

    LinearLayout fragment_layout;
    EditText email, password;
    TextView forgotPassword, googleSignIn, fbSignIn, registerNow, login;


    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[@#$%^&+=])" +     // at least 1 special character
                    "(?=\\S+$)" +            // no white spaces
                    ".{8,}" +                // at least 4 characters
                    "$");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login_tab_fragment, container, false);

        email = view.findViewById(R.id.signInEmailEdtText);
        password = view.findViewById(R.id.signInPasswordEditText);
        forgotPassword = view.findViewById(R.id.forgotPassword);
        googleSignIn = view.findViewById(R.id.googleSignIn);
        fbSignIn = view.findViewById(R.id.fbSignIn);
        registerNow = view.findViewById(R.id.registerNow);
        login = view.findViewById(R.id.login);

        fragment_layout = view.findViewById(R.id.fragment_layout);

        registerNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.changeSignupTabColor();

            }
        });

        googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getContext(), "Google Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        fbSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getContext(), "Facebook Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!validateEmail() | !validatePassword()) {
                    return;
                }

                // if the email and password matches, a toast message
                // with email and password is displayed
//                String input = "Email: " + email.getText().toString();
//                input += "\n";
//                input += "Password: " + password.getText().toString();
//                Toast.makeText(getActivity(), ""+input, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getContext(), HomeActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private boolean validatePassword() {

        // Extract input from EditText
        String emailInput = email.getText().toString().trim();

        // if the email input field is empty
        if (emailInput.isEmpty()) {
            email.setError("Field can not be empty");
            return false;
        }

        // Matching the input email to a predefined email pattern
        else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            email.setError("Please enter a valid email address");
            return false;
        } else {
            email.setError(null);
            return true;
        }
    }

    private boolean validateEmail() {

        String passwordInput = password.getText().toString().trim();
        // if password field is empty
        // it will display error message "Field can not be empty"
        if (passwordInput.isEmpty()) {
            password.setError("Field can not be empty");
            return false;
        }

        // if password does not matches to the pattern
        // it will display an error message "Password is too weak"
        else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            password.setError("Must contain at least 1 special character, no white spaces, at least 8 characters");
            return false;
        } else {
            password.setError(null);
            return true;
        }

    }
}