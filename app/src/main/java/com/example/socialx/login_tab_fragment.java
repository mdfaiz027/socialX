package com.example.socialx;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.regex.Pattern;

public class login_tab_fragment extends Fragment{

    LinearLayout fragment_layout;
    EditText email, password;
    TextView forgotPassword, googleSignIn, fbSignIn, registerNow, login;

    ProgressDialog progressDialog;

    private String emailInput, passwordInput;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    FirebaseAuth firebaseAuth;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

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

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);

        sharedPreferences = getContext().getSharedPreferences("LOGIN", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        firebaseAuth = FirebaseAuth.getInstance();

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(getContext(), gso);

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
                signIn();
            }
        });

/*        fbSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getContext(), "Facebook Clicked", Toast.LENGTH_SHORT).show();
            }
        });*/

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!validateEmail()) {
                    return;
                }else if(!validatePassword()){
                    return;
                }

                checkUser();
            }
        });

        return view;
    }

    private void signIn() {
        Intent intent = gsc.getSignInIntent();
        startActivityForResult(intent, 100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100){
            Task<GoogleSignInAccount>  task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                navigateToHomeActivity();
            } catch (ApiException e) {
                Toast.makeText(getContext(), ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void navigateToHomeActivity() {
        Intent intent = new Intent(getContext(), HomeActivity.class);
        startActivity(intent);
        Toast.makeText(getContext(), "Successfully Logged In", Toast.LENGTH_SHORT).show();
    }

    private void checkUser() {

        progressDialog.show();
        progressDialog.setMessage("loading....");

        emailInput = email.getText().toString();
        passwordInput = password.getText().toString();

        firebaseAuth.signInWithEmailAndPassword(emailInput,passwordInput).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();

                if(!task.isSuccessful()){
                    //Toast.makeText(getContext(), ""+task.getException(), Toast.LENGTH_SHORT).show();
                }else{

                    editor.putString("usermail", emailInput);
                    editor.putBoolean("status", true);
                    editor.commit();

                    navigateToHomeActivity();
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

    @Override
    public void onStart() {
        super.onStart();

        if(sharedPreferences.getBoolean("status", false) == true){
            startActivity(new Intent(getContext(), HomeActivity.class));
        }

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());

        if(account!=null){
            Intent intent = new Intent(getContext(), HomeActivity.class);
            startActivity(intent);
        }

    }

}