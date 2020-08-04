package com.example.photoeditor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class FinalActivity extends AppCompatActivity
{
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        byte[] byteArray = getIntent().getByteArrayExtra("bitmap");
        bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

    }

    private void initUIWidgets()
    {
        ImageView save = findViewById(R.id.ic_save);
        ImageView share = findViewById(R.id.ic_share);
        ImageView imageView = findViewById(R.id.final_image);
        imageView.setImageBitmap(bitmap);

        save.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                saveImage();
            }
        });

        share.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                saveImage();
                //TODO : Share
            }
        });

    }

    private void saveImage()
    {
        MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "photo_" + System.currentTimeMillis(), "");
        Toast.makeText(FinalActivity.this, "Saved in Pictures", Toast.LENGTH_SHORT).show();
    }
}
