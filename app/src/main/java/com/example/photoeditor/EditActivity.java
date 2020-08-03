package com.example.photoeditor;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;


public class EditActivity extends AppCompatActivity implements MenuAdapter.OnItemSelected
{
    private ImageView image;
    private RecyclerView menu;
    private ImageView cancel;
    private ImageView ok;
    private ImageView undo;
    private ImageView redo;
    private Uri imageUri;
    private Bitmap bitmap;
    private MenuAdapter menuAdapter = new MenuAdapter(this);


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        image = findViewById(R.id.image_holder);
        menu = findViewById(R.id.menu);
        cancel = findViewById(R.id.cancel_icon);
        undo = findViewById(R.id.undo_icon);
        redo = findViewById(R.id.redo_icon);
        ok = findViewById(R.id.checked_icon);
        imageUri = Uri.parse(getIntent().getStringExtra("uri"));
        byte[] byteArray = getIntent().getByteArrayExtra("bitmap");
        bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        image.setImageURI(imageUri);
        LinearLayoutManager toolsLinearLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        menu.setLayoutManager(toolsLinearLayoutManager);
        menu.setAdapter(menuAdapter);
    }



    @Override
    public void onToolSelected(ToolType toolType)
    {
        switch (toolType)
        {
            case ADJUST:
                break;
            case CROP:
                break;
            case FILTER:
                break;
            case ROTATE:
                break;
            case BRUSH:
                break;
            case TEXT:
                break;
            case FRAME:
                break;
            case BLUR:
                break;
            case ERASER:
                break;
            case EMOJI:
                break;
        }
    }
}