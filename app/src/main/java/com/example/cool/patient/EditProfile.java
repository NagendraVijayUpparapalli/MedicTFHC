package com.example.cool.patient;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;


import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditProfile extends AppCompatActivity implements View.OnClickListener {

    public static final CharSequence[] BloodGroups  = {"---Select your Blood Group ---", "A+", "A-", "B+", "B-", "AB+", "AB-","O+","O-"};
    public static final CharSequence[] states  = {"---Select your State ---", "Andhra Pradesh", "Telangana"};


    EditText tv;
    Calendar mCurrentDate;
    int day,month,year;

    private ImageView imageView;
    private Button btnShare;
    private Button btnSave;
    private AlertDialog dialog;


    final int REQUEST_CODE_GALLERY = 999;

    Button btnadd, btnchoose;
    EditText editText;
    ImageView editImage;
    EditText text, text1, text2, Mobile_Number, Address, pincode, Aadhar_number, date_of_birth;
    Button gen_btn;
    ImageView image;
    String text2Qr;
    String text3Qr;
    String text4Qr;
    String mb;
    String Add;
    String pin;
    String Adhar;
    String dob;
    String text5Qr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_toolbar_arrow);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(EditProfile.this, "clicking the Back!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditProfile.this,MainActivity.class);
                        startActivity(intent);

                    }
                }

        );
        setTitle(getString(R.string.app_name_edit_profile));
        tv = (EditText)findViewById(R.id.date_of_birth);
        mCurrentDate = Calendar.getInstance();
        day =mCurrentDate.get(Calendar.DAY_OF_MONTH);
        month =mCurrentDate.get(Calendar.MONTH);
        year = mCurrentDate.get(Calendar.YEAR);
        month = month+1;
        tv.setText(day+"/"+month+"/"+year);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditProfile.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        monthOfYear = monthOfYear+1;
                        tv.setText(dayOfMonth+"/"+monthOfYear+"/"+year);

                    }
                }, year,month,day);
                datePickerDialog.show();
            }
        });
        init();
        setupView();

        btnchoose = (Button) findViewById(R.id.btn_choose);

        editImage = (ImageView) findViewById(R.id.imageView);


        Spinner mySpinner = (Spinner) findViewById(R.id.spinner1);


        ArrayAdapter<CharSequence> myadapter = new ArrayAdapter<CharSequence> (this, android.R.layout.simple_spinner_item, BloodGroups);
        myadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Specify the layout to use when the list of choices appears
        mySpinner.setAdapter(myadapter); // Apply the adapter to the spinner


        Spinner spinner1 = (Spinner) findViewById(R.id.Blood_group);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence> (this, android.R.layout.simple_spinner_item, states);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Specify the layout to use when the list of choices appears
        spinner1.setAdapter(adapter); // Apply the adapter to the spinner




        text = (EditText) findViewById(R.id.text1);
        Mobile_Number = (EditText) findViewById(R.id.Mobile_Number);
        text1 = (EditText) findViewById(R.id.Last_name);
        text2 = (EditText) findViewById(R.id.Email);
        gen_btn = (Button) findViewById(R.id.gen_btn);
        image = (ImageView) findViewById(R.id.image);
        Address = (EditText) findViewById(R.id.Address);
        pincode = (EditText) findViewById(R.id.pincode);
        Aadhar_number = (EditText) findViewById(R.id.Aadhar_number);
        date_of_birth = (EditText) findViewById(R.id.date_of_birth);


        btnchoose.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityCompat.requestPermissions(
                                EditProfile.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                REQUEST_CODE_GALLERY
                        );
                    }
                });
        gen_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text2Qr = text.getText().toString().trim();
                text3Qr = text1.getText().toString().trim();
                text4Qr = text2.getText().toString().trim();
                mb = Mobile_Number.getText().toString().trim();
                Add = Address.getText().toString().trim();
                pin = pincode.getText().toString().trim();
                Adhar = Aadhar_number.getText().toString().trim();
                dob = date_of_birth.getText().toString().trim();






                String text5qr = " 'Name:-' " + text2Qr + " " + text3Qr + " 'Email:-' " + text4Qr + " 'Mobile_number:-' " + mb + " 'Address:-' " + Add + " 'pincode:-' " + pin + " 'Adhar_number:-' " + Adhar + " 'date_of_birth:-' " + dob;
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();


                try {
                    BitMatrix bitMatrix = multiFormatWriter.encode(text5qr, BarcodeFormat.QR_CODE, 200, 200);

                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();


                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

                    image.setImageBitmap(bitmap);

                } catch (WriterException e) {
                    e.printStackTrace();
                }


            }


        });


    }

    public void setupView() {
        btnShare.setOnClickListener(this);
        btnSave.setOnClickListener(this);
    }

    public void init() {
        imageView = (ImageView) findViewById(R.id.image);
        btnShare = (Button) findViewById(R.id.btn_share);
        btnSave  = (Button) findViewById(R.id.btn_save);
    }


    private void Toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    public byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE_GALLERY) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            } else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                editImage.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    public static Bitmap viewToBitmap(View view,int width,int height){
        Bitmap bitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_share: {
                startShare();
                break;
            }
            case R.id.btn_save: {
                dialog = new AlertDialog.Builder(this).create();
                dialog.setTitle("Save image");
                dialog.setMessage("Are you sure to want to save the QRCode?");
                dialog.setButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startSave();
                    }
                });

                dialog.setButton2("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;

            }
        }
    }

    public void startSave() {
        FileOutputStream fileOutputStream = null;
        File file = getDisc();
        if (!file.exists() && !file.mkdirs()){
            Toast.makeText(this,"can't create directory to save image",Toast.LENGTH_SHORT).show();
            return;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyymmsshhmmss");
        String date = simpleDateFormat.format(new Date());
        String name ="Img"+date+".jpg";
        String file_name = file.getAbsolutePath()+"/"+name;
        File new_file = new File(file_name);
        try {
            fileOutputStream=new FileOutputStream(new_file);
            Bitmap bitmap = viewToBitmap(imageView,imageView.getWidth(),imageView.getHeight());
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
            Toast.makeText(this,"save image success",Toast.LENGTH_SHORT).show();
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e){
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
        refreshGallery(new_file);
    }
    public void refreshGallery(File file){
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        sendBroadcast(intent);

    }
    private File getDisc() {
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        return  new File(file,"QR Code");
    }
    public void startShare()
    {
        Bitmap bitmap =viewToBitmap(imageView,imageView.getWidth(),imageView.getHeight());
        Intent shareIntent=new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/jpeg");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        File file = new File(Environment.getExternalStorageDirectory()+ File.separator+"QRCode.jpg");
        try {
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(byteArrayOutputStream.toByteArray());
        } catch ( IOException e) {
            e.printStackTrace();
        }
        shareIntent.putExtra(Intent.EXTRA_STREAM,Uri.parse("file:///sdcard/QRCode.jpg"));
        startActivity(Intent.createChooser(shareIntent,"Share Image"));
    }





}

