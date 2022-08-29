package com.example.socialx;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import java.util.regex.Pattern;

public class signup_tab_fragment extends Fragment {

    View view;
    EditText name, email, phone_number, password;
    TextView signIn, register;
    CountryCodePicker cpp;
    CheckBox checkBox;

    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[@#$%^&+=])" +     // at least 1 special character
                    "(?=\\S+$)" +            // no white spaces
                    ".{8,}" +                // at least 4 characters
                    "$");

    private String nameInput, emailInput, phone_numberInput, passwordInput;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_signup_tab_fragment, container, false);

        name = view.findViewById(R.id.idETNameField);
        email = view.findViewById(R.id.idETEmailField);
        cpp = view.findViewById(R.id.ccp);
        phone_number = view.findViewById(R.id.phone_number_edt);
        password = view.findViewById(R.id.idPasswordEditText);
        signIn = view.findViewById(R.id.idSignIn);
        checkBox = view.findViewById(R.id.idIAgreeWith);
        register = view.findViewById(R.id.idLogin);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);

        sharedPreferences = getContext().getSharedPreferences("LOGIN", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.changeLoginTabColor();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!validateName()) {
                    return;
                }else if(!validateEmail()){
                    return;
                }else if(!validatePhoneNumber()){
                    return;
                }else if(!validatePassword()){
                    return;
                }else if(!checkBox.isChecked()){
                    Toast.makeText(getContext(), "Please agree with the terms and conditions", Toast.LENGTH_SHORT).show();
                    return;
                }
                createUser();
            }
        });

        return view;
    }

    private boolean validateName() {

        nameInput = name.getText().toString();

        if(nameInput.matches("^[\\p{L} .'-]+$")){
            return true;
        }else{
            name.setError("Enter a valid name!");
            return false;
        }

    }

    private boolean validateEmail() {

        // Extract input from EditText
        emailInput = email.getText().toString().trim();

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

    private boolean validatePhoneNumber() {

        phone_numberInput = phone_number.getText().toString().trim();
        if(phone_numberInput.length() == 10){
            return  true;
        }else {
            phone_number.setError("Enter a correct mobile number");
            return  false;
        }
    }

    private boolean validatePassword() {

        passwordInput = password.getText().toString().trim();
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

    private void createUser() {

        progressDialog.show();
        progressDialog.setMessage("Creating user....");

        emailInput = email.getText().toString();
        passwordInput = password.getText().toString();

        firebaseAuth.createUserWithEmailAndPassword(emailInput,passwordInput).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();

                if(!task.isSuccessful()){
                    //Toast.makeText(getContext(), ""+task.getException(), Toast.LENGTH_SHORT).show();
                }else{
                    editor.putString("usermail", emailInput);
                    editor.putBoolean("status", true);
                    editor.commit();

                    Intent intent = new Intent(getContext(), HomeActivity.class);
                    startActivity(intent);

                    Toast.makeText(getContext(), "Successfully Signed Up", Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();

                Toast.makeText(getContext(), ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}