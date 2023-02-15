package com.example.loginapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class StartActivity extends AppCompatActivity {

    private String email;
    private String token;
    private boolean isLogged = false;

    private TextView mStartText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        token = intent.getStringExtra("token");
        mStartText = findViewById(R.id.StartText);

        isLogged = sessionValidate();
    }

    private boolean sessionValidate(){
        if (token == null || token.isEmpty()){
            mStartText.setText("No valid Session, please Sign In");
            return false;
        }
        else{
            String result = MyMockAPI.GET_EmailAndToken(email, token);
            if (result.equals("OK")){
                mStartText.setText("Hello " + email + ", you are logged in with the token: "+ token + ".");
                return true;
            }
            else{
                mStartText.setText("Hello " + email + ", no valid session with the token: " + token + ". Please Sign In");
                return false;
            }
        }
    }

    public void signOut(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}