package com.example.photoeditor;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zomato.photofilters.imageprocessors.Filter;

import java.io.IOException;


public class EditActivity extends AppCompatActivity implements MenuAdapter.OnItemSelected, FilterAdapter.OnItemSelected
{
    static {
        System.loadLibrary("NativeImageProcessor");
    }

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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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
}