package com.example.loginactivityfirebase;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class MainActivity2 extends AppCompatActivity {
    private static final int REQUEST_CONTACT = 1;
    private static final int REQUEST_READ_CONTACTS_PERMISSION = 0;
    private static final int SELECT_PICTURE = 200;
    private Button contact_btn  ;
    private TextView count_text;
    private Button addImage_btn;
    private ImageView profile_img;
    static  int count =0;

    private HashMap<String,String>map;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        requestContactsPermission();

        contact_btn =  findViewById(R.id.btn_Contact);
        count_text =  findViewById(R.id.Contact_counter_text);
        addImage_btn = findViewById(R.id.btn_addImage);
        profile_img =  findViewById(R.id.profile_pic);

        final Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        updateButton(hasContactsPermission());

        map =  new HashMap<String,String>();
        Uri uri =  ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Cursor cursor =  getContentResolver().query(uri,null,null,null,null);
        while(cursor.moveToNext()){
            int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);

            String phoneNo ="";
            phoneNo = cursor.getString(phoneIndex);
            String name = "";
            name = cursor.getString(nameIndex);
            if(!name.equals(null) && !phoneNo.equals(null)){
                //Log.e("names",  "phone" +" "+phoneNo+" "+ nameIndex + " " + name);
                map.put(name,phoneNo);
            }

        }
        //--------------------------------------------------------------------------------
        contact_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(pickContact,REQUEST_CONTACT);
            }
        });
        //---------------------------------------------------------------------------------
        addImage_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChooser();
            }
        });
    }

    void imageChooser() {

        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }



    public void updateButton(boolean enable)
    {
        contact_btn.setEnabled(enable);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CONTACT) {
            String phoneNo = null;
            String name = null;

            Uri uri = data.getData();
            Cursor cursor = this.getContentResolver().query(uri, null, null, null, null);

            if (cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                name = cursor.getString(nameIndex);
                phoneNo = map.get(name);
                Log.e("yep",  "phone" +" "+phoneNo+" "+ nameIndex + " " + name);
                if(!phoneNo.equals(null)){
                    count++;
                    String counter = Integer.toString(count);
                    counter = counter + "number added";
                    count_text.setText(counter);
                    Toast.makeText(this,name+" "+phoneNo+"Added",Toast.LENGTH_SHORT).show();
                }

            }
            cursor.close();
        }

        if (resultCode == RESULT_OK && requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    profile_img.setImageURI(selectedImageUri);
                }
        }
    }

    private boolean hasContactsPermission()
    {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) ==
                PackageManager.PERMISSION_GRANTED;
    }
    private void requestContactsPermission()
    {
        if (!hasContactsPermission())
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_READ_CONTACTS_PERMISSION);
        }
    }
}