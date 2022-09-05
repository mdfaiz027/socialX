package com.example.socialx;

import static java.util.TimeZone.getTimeZone;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String email;
    TextView mail;
    ImageView logout;

    //String newsUrl = "https://newsapi.org/v2/everything?q=apple&from=2022-08-27&to=2022-08-27&sortBy=popularity&apiKey=dfe2196f8dac4ebc8576aff06ffe2521";
    String newsUrl = "https://run.mocky.io/v3/5bdc64a9-a3b8-4ddd-ba43-4a486a204cf4";

    List<NewsModel> newsModelArrayList = new ArrayList<>();

    RecyclerView recyclerView;
    NewsModel newsModel;
    NewsAdapter newsAdapter;
    RequestQueue queue;
    ProgressDialog progressDialog;

    FirebaseAuth auth = FirebaseAuth.getInstance();

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportActionBar().hide();

        mail = findViewById(R.id.usermail);
        logout = findViewById(R.id.logout);

        sharedPreferences = getSharedPreferences("LOGIN", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        email = sharedPreferences.getString("usermail","");
        mail.setText(""+email);

        //NewsApi
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        queue = Volley.newRequestQueue(this);

        email = auth.getCurrentUser().getEmail();
        mail.setText(""+email);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if(account!=null){
            String emailStr = account.getEmail();
            mail.setText(emailStr);
        }

        loadData();

        //Logout functionality
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Using Shared Preferences
                sharedPreferences = getSharedPreferences("LOGIN", Context.MODE_PRIVATE);
                editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                finish();

                startActivity(new Intent(HomeActivity.this, MainActivity.class));

                //Firebase logout
                signout();

            }
        });
    }

    private void signout() {
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                startActivity(new Intent(HomeActivity.this, MainActivity.class));
                finish();
            }
        });
    }


    private void loadData() {

        progressDialog.show();
        progressDialog.setMessage("Loading...");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, newsUrl, null, new Response.Listener<JSONObject>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();

                try {
                    JSONArray jsonArray = response.getJSONArray("articles");

                    for(int i=0; i<jsonArray.length(); i++){

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String title = jsonObject.getString("title");
                        String description = jsonObject.getString("description");
                        String publishedAt = jsonObject.getString("publishedAt");

                        TimeAgo2 timeAgo2 = new TimeAgo2();
                        String MyFinalValue = timeAgo2.covertTimeToText(publishedAt);

                        String urlToImage = jsonObject.getString("urlToImage");

                        JSONObject source = jsonObject.getJSONObject("source");
                        String name = source.getString("name");

                        newsModel = new NewsModel(""+title, ""+description, ""+MyFinalValue, ""+name, ""+urlToImage);
                        newsModelArrayList.add(newsModel);
                        newsAdapter = new NewsAdapter(getApplicationContext(), newsModelArrayList);
                        recyclerView.setAdapter(newsAdapter);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(HomeActivity.this, ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(HomeActivity.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonObjectRequest);
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