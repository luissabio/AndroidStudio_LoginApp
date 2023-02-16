package com.example.loginapplication;

import static android.Manifest.permission.READ_CONTACTS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mEmailView = (AutoCompleteTextView) findViewById(R.id.mEmailView);
        mPasswordView = (EditText) findViewById(R.id.mPasswordView);

        emailAutoComplete();


    }

    public void enter(View view) {
        if (mPasswordView.getText().toString().length() == 0 ){
            showToast("Empty Password");
        }
        else if (!isValidEmail(String.valueOf(mEmailView.getText()))){
            mEmailView.setText("");
            mPasswordView.setText("");
            showToast("Invalid Email Format");
        }
        else{
            String result = MyMockAPI_Credentials.POST_EmailAndPassword(mEmailView.getText().toString(), mPasswordView.getText().toString());
            if (result.equals("OK")){
                Intent intent = new Intent(this, SignInActivity.class);
                startActivity(intent);
            }
            else{
                mEmailView.setText("");
                mPasswordView.setText("");
                showToast(result.split("\\|")[1]);
            }
        }
    }

    private void showToast(String text){
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        toast.show();
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    private void emailAutoComplete(){
        if (ContextCompat.checkSelfPermission(this, READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { READ_CONTACTS }, 1);
        }
        else{
            emailAutoCompleteWithPermissions();
        }
    }

    private void emailAutoCompleteWithPermissions(){
        String[] emailList = getAllEmailsFromContacts();
        //Creating the instance of ArrayAdapter containing list of fruit names
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, emailList);
        //Getting the instance of AutoCompleteTextView
        mEmailView = (AutoCompleteTextView) findViewById(R.id.mEmailView);
        mEmailView.setThreshold(1);//will start working from first character
        mEmailView.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
    }

    private String[] getAllEmailsFromContacts() {
        List<String> emailList = new ArrayList<>();
        ContentResolver cr = getContentResolver();
        String[] projection = {ContactsContract.CommonDataKinds.Email.CONTACT_ID, ContactsContract.CommonDataKinds.Email.ADDRESS};
        Cursor cursor = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, projection, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int emailColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS);
            do {
                String email = cursor.getString(emailColumnIndex);
                if (email != null) {
                    emailList.add(email);
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
        return emailList.toArray(new String[0]);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == requestCode) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                emailAutoComplete();
            } else {
                showToast("Permissions were to autofill the mail");
            }
        }
    }
}