package com.example.socialx;

import static com.example.socialx.R.color.black;
import static com.example.socialx.R.color.gray;
import static com.example.socialx.R.color.white;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {

    TextView login, signup;
    RelativeLayout loginRL, signupRL;
    LinearLayout firstLL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = findViewById(R.id.idLogin);
        signup = findViewById(R.id.idSignUp);
        loginRL = findViewById(R.id.loginRL);
        signupRL = findViewById(R.id.signupRL);
        firstLL = findViewById(R.id.idFirstLL);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_layout, new login_tab_fragment());
        ft.commit();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLoginTabColor();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeSignupTabColor();
            }
        });

    }

    public void changeSignupTabColor() {
        replaceFragment(new signup_tab_fragment());
        signupRL.setBackgroundResource(R.drawable.round_corners_bottom_red);
        signup.setTextColor(getResources().getColorStateList(white));
        loginRL.setBackgroundResource(R.drawable.round_corner_leftside_border_red);
        login.setTextColor(getResources().getColorStateList(gray));
    }

    public void changeLoginTabColor() {
        replaceFragment(new login_tab_fragment());
        loginRL.setBackgroundResource(R.drawable.round_corners_bottom_red);
        login.setTextColor(getResources().getColorStateList(white));
        signupRL.setBackgroundResource(R.drawable.round_corner_rightside_border_red);
        signup.setTextColor(getResources().getColorStateList(gray));

    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_layout, fragment);
        ft.commit();
    }

    //onBackPressed
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {

        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            moveTaskToBack(true);
            return true; // return
        }

        return false;
    }

}