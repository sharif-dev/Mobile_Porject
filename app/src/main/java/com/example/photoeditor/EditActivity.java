package com.example.photoeditor;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;


public class EditActivity extends AppCompatActivity
{
    private ImageView image;
    private RecyclerView menu;
    private ImageView cancel;
    private ImageView ok;
    private ImageView undo;
    private ImageView redo;
    private Uri imageUri;
    private Bitmap bitmap;

    private Controller controller = Controller.getInstance();


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

        choosePhoto();
        controller.makeMenu(menu, EditActivity.this);
        controller.controlPanel(cancel, undo, redo, ok, EditActivity.this);
    }

    private void choosePhoto()
    {
        Intent intent = getIntent();
        String method = intent.getStringExtra("method");
        if( method.equals("pick from gallery") )
        {
            Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(gallery, 2);
        }
        else if( method.equals("take photo") )
        {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "New Picture");
            values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
            imageUri = getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null)
                startActivityForResult(takePictureIntent, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            if (requestCode == 1)
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
                image.setImageURI(imageUri);

            }
            else if (requestCode == 2)
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
                image.setImageURI(imageUri);
            }
        }
    }

}