package com.example.loginapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class StartActivity extends AppCompatActivity {

    private String email;
    private String token;
    private boolean isLogged = false;

    private TextView mStartText;
    private LinearLayout mLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        token = intent.getStringExtra("token");
        mStartText = findViewById(R.id.StartText);
        mLinearLayout = findViewById(R.id.linear_layout);

        isLogged = sessionValidate();

        if(isLogged){
            showUserInformation();
        }
    }

    private void showUserInformation(){
        try {
            JSONObject jsonObject = new JSONObject(MyMockAPI_UserInfo.GET_UserInfo(email, token));

            Iterator<String> keys = jsonObject.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                Object value = jsonObject.get(key);

                TextView textViewKey = new TextView(this);
                textViewKey.setText(key + ": ");
                mLinearLayout.addView(textViewKey);

                if (key.equals("photo")) {
                    ImageView imageView = new ImageView(this);
                    imageView.setImageBitmap(stringToBitmap((String) value));
                    mLinearLayout.addView(imageView);
                }
                else if (value instanceof String) {
                    TextView textViewValue = new TextView(this);
                    textViewValue.setText((String) value);
                    mLinearLayout.addView(textViewValue);
                } else if (value instanceof Boolean) {
                    TextView textViewValue = new TextView(this);
                    textViewValue.setText(value.toString());
                    mLinearLayout.addView(textViewValue);
                } else if (value instanceof Integer) {
                    TextView textViewValue = new TextView(this);
                    textViewValue.setText(Integer.toString((int) value));
                    mLinearLayout.addView(textViewValue);
                } else if (value instanceof JSONArray) {
                    JSONArray jsonArray = (JSONArray) value;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject item = jsonArray.getJSONObject(i);

                        TextView textViewItemKey = new TextView(this);
                        textViewItemKey.setText("- " + item.getString("name") + ": ");
                        mLinearLayout.addView(textViewItemKey);

                        TextView textViewItemValue = new TextView(this);
                        textViewItemValue.setText(item.getString("level"));
                        mLinearLayout.addView(textViewItemValue);
                    }
                }
            }
        } catch (Exception e) {
            Log.d("Json", e.getMessage());
        }
    }

    private Bitmap stringToBitmap(String bitmapString) {
        byte[] decodedString = Base64.decode(bitmapString, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    private boolean sessionValidate(){
        if (token == null || token.isEmpty()){
            mStartText.setText("No valid Session, please Sign In");
            return false;
        }
        else{
            String result = MyMockAPI_Credentials.GET_EmailAndToken(email, token);
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
        MyMockAPI_Credentials.DELETE_EmailAndToken(email);
        email = "";
        token = "";
        isLogged = false;
        finish();
        //Intent intent = new Intent(this, MainActivity.class);
        //startActivity(intent);
    }

    public void editInfo(View view) {
        if (isLogged){
            Intent intent = new Intent(this, EditInfoActivity.class);
            intent.putExtra("email", email);
            intent.putExtra("token", token);
            startActivityForResult(intent, 1);
            //startActivity(intent);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("email", email);
        outState.putString("token", token);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        email = savedInstanceState.getString("email");
        token = savedInstanceState.getString("email");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            mLinearLayout.removeAllViews();
            showUserInformation();
        }
    }
}