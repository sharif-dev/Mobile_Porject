package com.example.photoeditor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;


import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.burhanrashid52.photoeditor.college.CollageView;
import com.burhanrashid52.photoeditor.college.MultiTouchListener;
import com.burhanrashid52.photoeditor.tools.ActivityType;
import com.burhanrashid52.photoeditor.tools.EditingToolsAdapter;
import com.burhanrashid52.photoeditor.tools.ToolType;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CollegeActivity extends AppCompatActivity implements EditingToolsAdapter.OnItemSelected{
    private static final int PICK_REQUEST = 53;
    private static final int CAMERA_REQUEST = 52;
    private EditingToolsAdapter toolsAdapter = new EditingToolsAdapter(this, ActivityType.COLLAGE);
    private RecyclerView tools;
    Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_college);

        initTools();
        findViewById(R.id.collageBgView).setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                return true;
            }
        });
        setBundleData();
        TextView done_txt = findViewById(R.id.collage_done_txt);
        done_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                done();
            }
        });

        ImageView cancel = findViewById(R.id.collage_cancel);
        done_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

    }

    void initTools() {
        tools = findViewById(R.id.collageTools);
        LinearLayoutManager llmTools = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        tools.setLayoutManager(llmTools);
        tools.setAdapter(toolsAdapter);
    }

    Bitmap getCollageImage() {
        RelativeLayout view = findViewById(R.id.colrlid);
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    byte[] convertBitmapToByteArraye(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    private void setBundleData() {
        byte[] byteArray = getIntent().getByteArrayExtra("image");
        image = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        newCollageImageView(image);
    }

    private void newCollageImageView(Bitmap image) {
        RelativeLayout myLayout = findViewById(R.id.colrlid);
        CollageView collageView = new CollageView(this);
        collageView.setImageBitmap(image);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.addRule(RelativeLayout.CENTER_VERTICAL);

        myLayout.addView(collageView, params);
        collageView.setOnTouchListener(new MultiTouchListener());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CAMERA_REQUEST:
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    newCollageImageView(photo);
                    break;
                case PICK_REQUEST:
                    try {
                        Uri uri = data.getData();
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        newCollageImageView(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    @Override
    public void onToolSelected(ToolType toolType) {
        switch (toolType) {
            case CAMERA:
                importImageFromCamera();
                break;
            case GALLERY:
                importImageFromGallery();
                break;
        }
    }

    private void cancel() {
        Intent intent = new Intent();
        intent.putExtra("image", convertBitmapToByteArraye(image));
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private void done() {
        Intent intent = new Intent();
        intent.putExtra("image", convertBitmapToByteArraye(getCollageImage()));
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private void importImageFromCamera() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    private void importImageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_REQUEST);
    }
}
