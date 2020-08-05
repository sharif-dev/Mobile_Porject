package com.example.photoeditor;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zomato.photofilters.imageprocessors.Filter;

import ja.burhanrashid52.photoeditor.OnPhotoEditorListener;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;
import ja.burhanrashid52.photoeditor.TextStyleBuilder;
import ja.burhanrashid52.photoeditor.ViewType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class EditActivity extends AppCompatActivity implements OnPhotoEditorListener,
        View.OnClickListener,
        MenuAdapter.OnItemSelected,
        FilterAdapter.OnItemSelected
{
    static
    {
        System.loadLibrary("NativeImageProcessor");
    }

    private static final String TAG = EditActivity.class.getSimpleName();

    private ImageView image;
    private RecyclerView menu, filters;
    private ImageView cancel;
    private ImageView ok;
    private ImageView undo;
    private ImageView redo;
    private Uri imageUri;
    private Bitmap bitmap;
    private int height, width;
    private MenuAdapter menuAdapter;
    private FilterAdapter filterAdapter;
    private ConstraintLayout rootView;
    private ConstraintSet constraintSet = new ConstraintSet();
    private boolean isFilterVisible;
    PhotoEditor photoEditor;
    private PhotoEditorView photoEditorView;
    private static final int CROP_ACTIVITY_CODE = 8000;


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

        imageUri = Uri.parse(getIntent().getStringExtra("uri"));
        byte[] byteArray = getIntent().getByteArrayExtra("bitmap");
        bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        initUIWidgets();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CROP_ACTIVITY_CODE:
                    photoEditorView.getSource().setImageBitmap(extractCroppedImage(data));
                    break;
            }
        }
    }

    private Bitmap extractCroppedImage(Intent intent) {
        byte[] byteArray = intent.getByteArrayExtra("cropped_image");
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

    private void initUIWidgets()
    {
        rootView = findViewById(R.id.rootView);
        filters = findViewById(R.id.rvFilterView);
        image = findViewById(R.id.image_holder);
        menu = findViewById(R.id.menu);
        cancel = findViewById(R.id.cancel_icon);
        undo = findViewById(R.id.undo_icon);
        redo = findViewById(R.id.redo_icon);
        ok = findViewById(R.id.checked_icon);
        image.setImageURI(imageUri);
        height = image.getDrawable().getIntrinsicHeight();
        width = image.getDrawable().getIntrinsicWidth();

        menuAdapter = new MenuAdapter(this);
        LinearLayoutManager toolsLinearLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        menu.setLayoutManager(toolsLinearLayoutManager);
        menu.setAdapter(menuAdapter);

        filterAdapter = new FilterAdapter(this.getApplication(), this, height, width, bitmap);
        LinearLayoutManager filtersLinearLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        filters.setLayoutManager(filtersLinearLayoutManager);
        filters.setAdapter(filterAdapter);
    }

    @Override
    public void onToolSelected(ToolType toolType)
    {
        switch (toolType)
        {
            case ADJUST:
                break;
            case CROP:
                showCropActivity();
                break;
            case FILTER:
                showFilter(true);
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

    Bitmap getImageBitmap() {
        ImageView imageView = photoEditorView.getSource();
        imageView.invalidate();
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        return drawable.getBitmap();
    }

    byte[] convertBitmapToByteArraye(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    void showCropActivity() {
        Intent intent = new Intent(this, CropActivity.class);
        intent.putExtra("image", convertBitmapToByteArraye(getImageBitmap()));
        startActivityForResult(intent, CROP_ACTIVITY_CODE);
    }

    void showFilter(boolean isVisible)
    {
        isFilterVisible = isVisible;
        constraintSet.clone(rootView);

        if (isVisible)
        {
            constraintSet.clear(filters.getId(), ConstraintSet.START);
            constraintSet.connect(filters.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
            constraintSet.connect(filters.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
        }
        else
        {
            constraintSet.connect(filters.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.END);
            constraintSet.clear(filters.getId(), ConstraintSet.END);
        }

        ChangeBounds changeBounds = new ChangeBounds();
        changeBounds.setDuration(350);
        changeBounds.setInterpolator(new AnticipateOvershootInterpolator(1.0f));
        TransitionManager.beginDelayedTransition(rootView, changeBounds);

        constraintSet.applyTo(rootView);
    }

    @Override
    public void onFilterSelected(Bitmap bitmap, Filter filter)
    {
        Bitmap filteredBitmap = filter.processFilter(Bitmap.createScaledBitmap(bitmap, width, height, false));
        image.setImageBitmap(filteredBitmap);
    }

    @Override
    public void onClick(View v)
    {

    }

    @Override
    public void onEditTextChangeListener(View rootView, String text, int colorCode)
    {

    }

    @Override
    public void onAddViewListener(ViewType viewType, int numberOfAddedViews)
    {
        Log.d(TAG, "onAddViewListener() called with: viewType = [" + viewType + "], numberOfAddedViews = [" + numberOfAddedViews + "]");
    }

    @Override
    public void onRemoveViewListener(ViewType viewType, int numberOfAddedViews)
    {
        Log.d(TAG, "onRemoveViewListener() called with: viewType = [" + viewType + "], numberOfAddedViews = [" + numberOfAddedViews + "]");
    }

    @Override
    public void onStartViewChangeListener(ViewType viewType)
    {
        Log.d(TAG, "onStartViewChangeListener() called with: viewType = [" + viewType + "]");
    }

    @Override
    public void onStopViewChangeListener(ViewType viewType)
    {
        Log.d(TAG, "onStopViewChangeListener() called with: viewType = [" + viewType + "]");
    }

    @Override
    public void onBackPressed()
    {
        if (isFilterVisible)
            showFilter(false);
        else if (!photoEditor.isCacheEmpty())
            showSaveDialog();
        else
            super.onBackPressed();
    }

    private void showSaveDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.msg_save_image));
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                Intent intent = new Intent(EditActivity.this, FinalActivity.class);
                intent.putExtra("bitmap", byteArray);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });

        builder.setNeutralButton("Discard", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Intent intent = new Intent(EditActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        builder.create().show();
    }

}