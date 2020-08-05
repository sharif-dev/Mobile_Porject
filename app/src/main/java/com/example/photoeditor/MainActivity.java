package com.example.photoeditor;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.photoeditor.college.CollageView;
import com.example.photoeditor.college.CollegeActivity;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.Semaphore;


public class MainActivity extends AppCompatActivity
{
    static {
        System.loadLibrary("NativeImageProcessor");
    }

    private Uri imageUri;
    private Bitmap bitmap;
    private static final int CAMERA_REQUEST = 1;
    private static final int PICK_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        ImageView gallery = findViewById(R.id.gallery);
        ImageView camera = findViewById(R.id.camera);
        ImageView collage = findViewById(R.id.collage);

        gallery.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, PICK_REQUEST);
            }
        });

        camera.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "New Picture");
                values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                imageUri = getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null)
                    startActivityForResult(takePictureIntent, CAMERA_REQUEST);
            }
        });

        collage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this, CollegeActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            if (requestCode == CAMERA_REQUEST)
            {
                try
                {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                assert bitmap != null;
                bitmap = bitmap.copy(Bitmap.Config.ARGB_8888 , true);
                displayEditActivity();
            }
            else if (requestCode == PICK_REQUEST)
            {
                imageUri = data.getData();
                try
                {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                assert bitmap != null;
                bitmap = bitmap.copy(Bitmap.Config.ARGB_8888 , true);
                displayEditActivity();
            }
        }
    }

    public void displayEditActivity()
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        Intent intent = new Intent(MainActivity.this, EditActivity.class);
        intent.putExtra("bitmap", byteArray);
        intent.putExtra("uri", imageUri.toString());
        startActivity(intent);
    }
}