package com.example.loginapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class EditInfoActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;

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
        ImageView imageView = findViewById(R.id.photoInput);

        String name = nameEditText.getText().toString();
        String surname = surnameEditText.getText().toString();
        String years = yearsEditText.getText().toString();
        String photo = bitmapToString(((BitmapDrawable)imageView.getDrawable()).getBitmap());

        JSONObject jsonObject = new JSONObject();
        try {
            if (!name.isEmpty() && name != null){
                jsonObject.put("name", name);
            }
            if (!surname.isEmpty() && surname != null){
                jsonObject.put("surname", surname);
            }
            if (!years.isEmpty() && years.toString() != null){
                if (Integer.parseInt(years) > 0){
                    jsonObject.put("years", Integer.parseInt(years));
                }
            }
            if (!photo.isEmpty() && photo != null){
                jsonObject.put("photo", photo);
            }
        } catch (JSONException e) {
            Log.d("Json", e.getMessage());
        }

        return jsonObject.toString();
    }

    private String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] byteArray = baos.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public void editInfo(View view) {
        if(isLogged){
            String jsonData = generateJsonWithInput();
            MyMockAPI_UserInfo.PUT_UserInfo(email, jsonData, token);

            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        }
        else{
            showToast("Not Logged");
        }
    }

    public void cancel(View view) {
        if(isLogged){
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        }
        else{
            showToast("Not Logged");
        }
    }

    private void showToast(String text){
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void selectImage(Activity activity) {
        final CharSequence[] options = { "Take photo", "Select from gallery", "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Select image");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take photo")) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
                        activity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                    else{
                        showToast("No access to the camera, use select from gallery");
                    }
                } else if (options[item].equals("Select from gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    activity.startActivityForResult(Intent.createChooser(intent, "Select image"), REQUEST_IMAGE_PICK);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                showImage(imageBitmap);
            } else if (requestCode == REQUEST_IMAGE_PICK) {
                Uri selectedImage = data.getData();
                try {
                    Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    showImage(imageBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void showImage(Bitmap image){
        ImageView imageView = findViewById(R.id.photoInput);
        imageView.setImageBitmap(image);
    }

    public void buttonPhoto(View view) {
        selectImage(EditInfoActivity.this);
    }
}