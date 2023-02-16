package com.example.loginapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class EditInfoActivity extends AppCompatActivity {
    private String email;
    private String token;
    private boolean isLogged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);

        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        token = intent.getStringExtra("token");

        isLogged = sessionValidate();
    }

    private boolean sessionValidate(){
        if (token == null || token.isEmpty()){
            return false;
        }
        else{
            String result = MyMockAPI_Credentials.GET_EmailAndToken(email, token);
            if (result.equals("OK")){
                return true;
            }
            else{
                return false;
            }
        }
    }

    private String generateJsonWithInput(){
        EditText nameEditText = findViewById(R.id.nameInput);
        EditText surnameEditText = findViewById(R.id.surnameInput);
        EditText yearsEditText = findViewById(R.id.yearsInput);

        String name = nameEditText.getText().toString();
        String surname = surnameEditText.getText().toString();
        int years = Integer.parseInt(yearsEditText.getText().toString());

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", name);
            jsonObject.put("surname", surname);
            jsonObject.put("years", years);
        } catch (JSONException e) {
            Log.d("Json", e.getMessage());
        }

        return jsonObject.toString();
    }

    public void enter(View view) {
        if(isLogged){
            String jsonData = generateJsonWithInput();
            String result = MyMockAPI_UserInfo.PUT_UserInfo(email, jsonData, token);

            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        }
        else{
            Toast toast = Toast.makeText(this, "Not Logged", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}