package com.example.cool.patient;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ConvertImageBase64 extends AppCompatActivity {

    ImageView imageView,imageView1;
    TextView editText;
    FloatingActionButton addImageFloatingButton;
    final int REQUEST_CODE_GALLERY1 = 999;
    String encodedImage,encoded;
    Uri selectedImageUri ;
    Bitmap selectedImageBitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convert_image_base64);
        imageView = (ImageView) findViewById(R.id.aadharimage);
        imageView1 = (ImageView) findViewById(R.id.decodeimage);
        editText = (TextView) findViewById(R.id.encoded);
        addImageFloatingButton = (FloatingActionButton) findViewById(R.id.addImageIcon);

        addImageFloatingButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityCompat.requestPermissions(
                                ConvertImageBase64.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                REQUEST_CODE_GALLERY1
                        );

                    }
                });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE_GALLERY1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY1);
            } else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }


        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if(result.getContents()==null){
                Toast.makeText(this, "You cancelled the scanning", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(this, result.getContents(),Toast.LENGTH_LONG).show();
                System.out.println("qr code data..."+result.getContents());
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }

        if (requestCode == REQUEST_CODE_GALLERY1) {
            // Make sure the request was successful
            Log.d("hello","I'm out.");
            if (resultCode == RESULT_OK && data != null && data.getData() != null) {

                selectedImageUri = data.getData();
                try {
                    selectedImageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                    final InputStream imageStream = getContentResolver().openInputStream(selectedImageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    encodedImage = myEncodeImage(selectedImage);
//                    selectedImageBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImageUri));
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

                System.out.print("text.."+encodedImage);
                imageView.setImageBitmap(selectedImageBitmap);



//                editText.setText(encodedImage);

                Log.d("hello","I'm in.");

            }
        }

        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    private String myEncodeImage(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        //decode base64 string to image
        b = Base64.decode(encImage, Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(b, 0, b.length);
        imageView1.setImageBitmap(decodedImage);

        return encImage;
    }

//    private String myDecodeImage(Bitmap bm)
//    {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
//        byte[] b = baos.toByteArray();
//        String encImage = Base64.decode(b, Base64.DEFAULT);
//
//        return encImage;
//    }
}
