package com.example.photoeditor;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity
{

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
                displayEditActivity("pick from gallery");
            }
        });

        camera.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                displayEditActivity("take photo");
            }
        });
    }


    public void displayEditActivity(String method)
    {
        Intent intent = new Intent(MainActivity.this, EditActivity.class);
        intent.putExtra("method", method);
        startActivity(intent);
    }
}