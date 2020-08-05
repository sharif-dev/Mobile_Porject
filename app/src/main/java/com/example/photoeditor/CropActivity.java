package com.example.photoeditor;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;

public class CropActivity extends AppCompatActivity {
    Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initData();

        Button done = findViewById(R.id.crop_done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("cropped_image", convertBitmapToByteArraye(getCroppedImage()));
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

        Button cancel = findViewById(R.id.crop_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("image", convertBitmapToByteArraye(image));
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    private void initData() {
        byte[] byteArray = getIntent().getByteArrayExtra("image");
        image = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        CropImageView cropImageView = findViewById(R.id.cropImageView2);
        cropImageView.setImageBitmap(image);
    }

    Bitmap getCroppedImage() {
        CropImageView cropImageView = findViewById(R.id.cropImageView2);
        return cropImageView.getCroppedImage();
    }

    byte[] convertBitmapToByteArraye(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
}