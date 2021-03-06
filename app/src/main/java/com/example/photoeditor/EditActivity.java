package com.example.photoeditor;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.SubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.VignetteSubFilter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import ja.burhanrashid52.photoeditor.OnPhotoEditorListener;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;
import ja.burhanrashid52.photoeditor.SaveSettings;
import ja.burhanrashid52.photoeditor.TextStyleBuilder;
import ja.burhanrashid52.photoeditor.ViewType;


public class EditActivity extends BaseActivity implements OnPhotoEditorListener,
        View.OnClickListener,
        MenuAdapter.OnItemSelected,
        FilterAdapter.OnItemSelected,
        PropertiesBSFragment.Properties,
        EmojiBSFragment.EmojiListener,
        RotationFragment.Tools,
        AdjustmentFragment.Tools,
        FrameAdapter.OnItemSelected
{
    static
    {
        System.loadLibrary("NativeImageProcessor");
    }

    private static final String TAG = EditActivity.class.getSimpleName();

    private RecyclerView menu, filters, frames;
    Uri saveImageUri;
    private Bitmap bitmapBeforeChange, bitmapAfterChange;
    private int height, width;
    private MenuAdapter menuAdapter;
    private FilterAdapter filterAdapter;
    private FrameAdapter frameAdapter;
    private EmojiBSFragment emojiBSFragment;
    private PropertiesBSFragment propertiesBSFragment;
    private RotationFragment rotationFragment;
    private AdjustmentFragment adjustmentFragment;
    private ConstraintLayout rootView;
    private ConstraintSet constraintSet = new ConstraintSet();
    private boolean isFilterVisible, isRotationVisible, isAdjustmentVisible, isFramesVisible;
    private PhotoEditor photoEditor;
    private PhotoEditorView photoEditorView;
    private RelativeLayout rotation, adjustment;
    private Filter adjustmentFilter;
    private ArrayList<ToolType> actions;
    private HashMap<Integer, Bitmap> bitmapsToUndo;
    private HashMap<Integer, Bitmap> bitmapsToRedo;
    private int pointer = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        makeFullScreen();
        setContentView(R.layout.activity_edit);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        Uri imageUri = Uri.parse(getIntent().getStringExtra("uri"));
        Bitmap bitmap = null;
        try
        {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        initUIWidgets();

        photoEditorView.getSource().setImageBitmap(bitmap);
        height = photoEditorView.getSource().getDrawable().getIntrinsicHeight();
        width = photoEditorView.getSource().getDrawable().getIntrinsicWidth();

        emojiBSFragment = new EmojiBSFragment();
        propertiesBSFragment = new PropertiesBSFragment(EditActivity.this);
        rotationFragment = new RotationFragment();
        adjustmentFragment = new AdjustmentFragment();

        emojiBSFragment.setEmojiListener(this);
        propertiesBSFragment.setPropertiesChangeListener(this);
        rotationFragment.setToolsChangeListener(this);
        adjustmentFragment.setToolsChangeListener(this);

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

        frameAdapter = new FrameAdapter(this.getApplication(), this, bitmap);
        LinearLayoutManager framesLinearLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        frames.setLayoutManager(framesLinearLayoutManager);
        frames.setAdapter(frameAdapter);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.rotationBar, rotationFragment);
        fragmentTransaction.add(R.id.adjustmentBar, adjustmentFragment);
        fragmentTransaction.commit();

        adjustmentFilter = new Filter();
        actions = new ArrayList<>();
        bitmapsToUndo = new HashMap<>();
        bitmapsToRedo = new HashMap<>();

        photoEditor = new PhotoEditor.Builder(this, photoEditorView).setPinchTextScalable(true).build();
        photoEditor.setOnPhotoEditorListener(this);
    }

    private void initUIWidgets()
    {
        rootView = findViewById(R.id.rootView);
        filters = findViewById(R.id.rvFilterView);
        frames = findViewById(R.id.rvFrameView);
        photoEditorView = findViewById(R.id.photoEditorView);
        menu = findViewById(R.id.menu);
        rotation = findViewById(R.id.rotationBar);
        adjustment = findViewById(R.id.adjustmentBar);
        ImageView share = findViewById(R.id.share_icon);
        ImageView undo = findViewById(R.id.undo_icon);
        ImageView redo = findViewById(R.id.redo_icon);
        ImageView save = findViewById(R.id.save_icon);

        undo.setOnClickListener(this);
        redo.setOnClickListener(this);
        save.setOnClickListener(this);
        share.setOnClickListener(this);
    }

    @Override
    public void onToolSelected(ToolType toolType)
    {
        if( pointer < actions.size() )
            for( int i = actions.size() - 1 ; i >= pointer ; i-- )
                actions.remove(i);
        actions.add(toolType);
        pointer++;
        switch (toolType)
        {
            case ADJUST:
                showAdjustment(true);
                break;
            case FILTER:
                filterAdapter = new FilterAdapter(this.getApplication(), this, height, width, getImageBitmap());
                LinearLayoutManager filtersLinearLayoutManager =
                        new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                filters.setLayoutManager(filtersLinearLayoutManager);
                filters.setAdapter(filterAdapter);
                showFilter(true);
                break;
            case ROTATE:
                showRotation(true);
                break;
            case BRUSH:
                photoEditor.setBrushDrawingMode(true);
                photoEditor.setBrushColor(0xffffffff);
                propertiesBSFragment.show(getSupportFragmentManager(), propertiesBSFragment.getTag());
                break;
            case TEXT:
                TextEditorDialogFragment textEditorDialogFragment = TextEditorDialogFragment.show(this, EditActivity.this);
                textEditorDialogFragment.setOnTextEditorListener(new TextEditorDialogFragment.TextEditor()
                {
                    @Override
                    public void onDone(String inputText, int colorCode)
                    {
                        final TextStyleBuilder styleBuilder = new TextStyleBuilder();
                        styleBuilder.withTextColor(colorCode);
                        photoEditor.addText(inputText, styleBuilder);
                    }
                });
                break;
            case FRAME:
                frameAdapter = new FrameAdapter(this.getApplication(), this, getImageBitmap());
                LinearLayoutManager framesLinearLayoutManager =
                        new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                frames.setLayoutManager(framesLinearLayoutManager);
                frames.setAdapter(frameAdapter);
                showFrames(true);
                break;
            case ERASER:
                photoEditor.brushEraser();
                break;
            case EMOJI:
                emojiBSFragment.show(getSupportFragmentManager(), emojiBSFragment.getTag());
                break;
        }
    }

    Bitmap getImageBitmap()
    {
        ImageView imageView = photoEditorView.getSource();
        imageView.invalidate();
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        return drawable.getBitmap();
    }

    void showFilter(boolean isVisible)
    {
        isFilterVisible = isVisible;
        constraintSet.clone(rootView);

        if (isVisible)
        {
            bitmapBeforeChange = getImageBitmap().copy(Bitmap.Config.ARGB_8888 , true);
            constraintSet.clear(filters.getId(), ConstraintSet.START);
            constraintSet.connect(filters.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
            constraintSet.connect(filters.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
        }
        else
        {
            constraintSet.connect(filters.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.END);
            constraintSet.clear(filters.getId(), ConstraintSet.END);
            bitmapsToUndo.put(pointer - 1, bitmapBeforeChange.copy(Bitmap.Config.ARGB_8888 , true));
            bitmapsToRedo.put(pointer - 1, getImageBitmap().copy(Bitmap.Config.ARGB_8888 , true));
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
        if( filter != null )
        {
            bitmapAfterChange = filter.processFilter(Bitmap.createScaledBitmap(bitmapBeforeChange, width, height, false));
            photoEditorView.getSource().setImageBitmap(bitmapAfterChange);
        }
        else
        {
            photoEditorView.getSource().setImageBitmap(bitmapBeforeChange);
        }
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.undo_icon:
                undo();
                break;
            case R.id.redo_icon:
                redo();
                break;
            case R.id.share_icon:
                shareImage();
                break;
            case R.id.save_icon:
                saveImage();
                break;
        }
    }

    @Override
    public void onEditTextChangeListener(final View rootView, String text, int colorCode)
    {
        TextEditorDialogFragment textEditorDialogFragment =
                TextEditorDialogFragment.show(this, text, colorCode, EditActivity.this);
        textEditorDialogFragment.setOnTextEditorListener(new TextEditorDialogFragment.TextEditor()
        {
            @Override
            public void onDone(String inputText, int colorCode)
            {
                final TextStyleBuilder styleBuilder = new TextStyleBuilder();
                styleBuilder.withTextColor(colorCode);
                photoEditor.editText(rootView, inputText, styleBuilder);
            }
        });
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
        else if (isRotationVisible)
            showRotation(false);
        else if (isAdjustmentVisible)
            showAdjustment(false);
        else if (isFramesVisible)
            showFrames(false);
        else if (!photoEditor.isCacheEmpty())
            showSaveDialog();
        else
            super.onBackPressed();
    }

    private void showSaveDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.ColorPickerDialogTheme);
        builder.setMessage(getString(R.string.msg_save_image));
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                saveImage();
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

    private void shareImage()
    {
        if (saveImageUri == null)
            saveImage();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, buildFileProviderUri(saveImageUri));
        startActivity(Intent.createChooser(intent, getString(R.string.msg_share_image)));
    }

    private Uri buildFileProviderUri(@NonNull Uri uri)
    {
        return FileProvider.getUriForFile(this, "", new File(uri.getPath()));
    }

    @SuppressLint("MissingPermission")
    private void saveImage()
    {
        if (requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE))
        {
            showLoading();
            File file = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "photo_"
                    + System.currentTimeMillis() + ".png");
            try
            {
                file.createNewFile();

                SaveSettings saveSettings = new SaveSettings.Builder()
                        .setClearViewsEnabled(true)
                        .setTransparencyEnabled(true)
                        .build();

                photoEditor.saveAsFile(file.getAbsolutePath(), saveSettings, new PhotoEditor.OnSaveListener()
                {
                    @Override
                    public void onSuccess(@NonNull String imagePath)
                    {
                        hideLoading();
                        showSnackbar("Image Saved Successfully");
                        saveImageUri = Uri.fromFile(new File(imagePath));
                        photoEditorView.getSource().setImageURI(saveImageUri);
                    }

                    @Override
                    public void onFailure(@NonNull Exception exception)
                    {
                        hideLoading();
                        showSnackbar("Failed to save Image");
                    }
                });
            }
            catch (IOException e)
            {
                e.printStackTrace();
                hideLoading();
                showSnackbar(e.getMessage());
            }
        }
    }

    @Override
    public void onEmojiClick(String emojiUnicode)
    {
        photoEditor.addEmoji(emojiUnicode);
    }

    @Override
    public void onColorChanged(int colorCode)
    {
        photoEditor.setBrushColor(colorCode);
    }

    @Override
    public void onOpacityChanged(int opacity)
    {
        photoEditor.setOpacity(opacity);
    }

    @Override
    public void onBrushSizeChanged(int brushSize)
    {
        photoEditor.setBrushSize(brushSize);
    }

    @Override
    public void onFlipVertical()
    {
        Matrix matrix = new Matrix();
        matrix.postScale(-1, 1, (float) bitmapBeforeChange.getWidth() / 2, (float) bitmapBeforeChange.getHeight() / 2);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmapBeforeChange, bitmapBeforeChange.getWidth(), bitmapBeforeChange.getHeight(), true);
        bitmapAfterChange = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        photoEditorView.getSource().setImageBitmap(bitmapAfterChange);
    }

    @Override
    public void onFlipHorizontal()
    {
        Matrix matrix = new Matrix();
        matrix.postScale(1, -1, (float) bitmapBeforeChange.getWidth() / 2, (float) bitmapBeforeChange.getHeight() / 2);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmapBeforeChange, bitmapBeforeChange.getWidth(), bitmapBeforeChange.getHeight(), true);
        bitmapAfterChange = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        photoEditorView.getSource().setImageBitmap(bitmapAfterChange);
    }

    @Override
    public void onAngleChanged(float degree)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree); Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmapBeforeChange, bitmapBeforeChange.getWidth(), bitmapBeforeChange.getHeight()
       , true);
        scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        photoEditorView.getSource().setImageBitmap(scaledBitmap);
    }

    void showRotation(boolean isVisible)
    {
        isRotationVisible = isVisible;
        constraintSet.clone(rootView);

        if (isVisible)
        {
            bitmapBeforeChange = getImageBitmap().copy(Bitmap.Config.ARGB_8888 , true);
            constraintSet.clear(rotation.getId(), ConstraintSet.START);
            constraintSet.connect(rotation.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
            constraintSet.connect(rotation.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
        }
        else
        {
            constraintSet.connect(rotation.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.END);
            constraintSet.clear(rotation.getId(), ConstraintSet.END);
            bitmapsToUndo.put(pointer - 1, bitmapBeforeChange.copy(Bitmap.Config.ARGB_8888 , true));
            bitmapsToRedo.put(pointer - 1, getImageBitmap().copy(Bitmap.Config.ARGB_8888 , true));
        }

        ChangeBounds changeBounds = new ChangeBounds();
        changeBounds.setDuration(350);
        changeBounds.setInterpolator(new AnticipateOvershootInterpolator(1.0f));
        TransitionManager.beginDelayedTransition(rootView, changeBounds);

        constraintSet.applyTo(rootView);
    }

    @Override
    public void onBrightnessChanged(int value)
    {
        boolean brightnessExists = false;
        for(SubFilter subFilter : adjustmentFilter.getSubFilters())
            if( subFilter instanceof BrightnessSubFilter )
            {
                ((BrightnessSubFilter) subFilter).setBrightness(value);
                brightnessExists = true;
                break;
            }
        if( !brightnessExists )
            adjustmentFilter.addSubFilter(new BrightnessSubFilter(value));
        Bitmap filteredBitmap = adjustmentFilter.processFilter(Bitmap.createScaledBitmap(bitmapBeforeChange, width, height, false));
        photoEditorView.getSource().setImageBitmap(filteredBitmap);
    }

    @Override
    public void onContrastChanged(float value)
    {
        boolean contrastExists = false;
        for(SubFilter subFilter : adjustmentFilter.getSubFilters())
            if( subFilter instanceof ContrastSubFilter )
            {
                ((ContrastSubFilter) subFilter).setContrast(value);
                contrastExists = true;
                break;
            }
        if( !contrastExists )
            adjustmentFilter.addSubFilter(new ContrastSubFilter(value));
        Bitmap filteredBitmap = adjustmentFilter.processFilter(Bitmap.createScaledBitmap(bitmapBeforeChange, width, height, false));
        photoEditorView.getSource().setImageBitmap(filteredBitmap);
    }

    @Override
    public void onSaturationChanged(float value)
    {
        boolean saturationExists = false;
        for(SubFilter subFilter : adjustmentFilter.getSubFilters())
            if( subFilter instanceof SaturationSubFilter )
            {
                ((SaturationSubFilter) subFilter).setLevel(value);
                saturationExists = true;
                break;
            }
        if( !saturationExists )
            adjustmentFilter.addSubFilter(new SaturationSubFilter(value));
        Bitmap filteredBitmap = adjustmentFilter.processFilter(Bitmap.createScaledBitmap(bitmapBeforeChange, width, height, false));
        photoEditorView.getSource().setImageBitmap(filteredBitmap);
    }

    @Override
    public void onVignetteChanged(int value)
    {
        boolean vignetteExists = false;
        for(SubFilter subFilter : adjustmentFilter.getSubFilters())
            if( subFilter instanceof VignetteSubFilter )
            {
                ((VignetteSubFilter) subFilter).setAlpha(value);
                vignetteExists = true;
                break;
            }
        if( !vignetteExists )
            adjustmentFilter.addSubFilter(new VignetteSubFilter(EditActivity.this, value));
        Bitmap filteredBitmap = adjustmentFilter.processFilter(Bitmap.createScaledBitmap(bitmapBeforeChange, width, height, false));
        photoEditorView.getSource().setImageBitmap(filteredBitmap);
    }

    void showAdjustment(boolean isVisible)
    {
        isAdjustmentVisible = isVisible;
        constraintSet.clone(rootView);

        if (isVisible)
        {
            bitmapBeforeChange = getImageBitmap().copy(Bitmap.Config.ARGB_8888 , true);
            constraintSet.clear(adjustment.getId(), ConstraintSet.START);
            constraintSet.connect(adjustment.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
            constraintSet.connect(adjustment.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
        }
        else
        {
            constraintSet.connect(adjustment.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.END);
            constraintSet.clear(adjustment.getId(), ConstraintSet.END);
            bitmapsToUndo.put(pointer - 1, bitmapBeforeChange.copy(Bitmap.Config.ARGB_8888 , true));
            bitmapsToRedo.put(pointer - 1, getImageBitmap().copy(Bitmap.Config.ARGB_8888 , true));
        }

        ChangeBounds changeBounds = new ChangeBounds();
        changeBounds.setDuration(350);
        changeBounds.setInterpolator(new AnticipateOvershootInterpolator(1.0f));
        TransitionManager.beginDelayedTransition(rootView, changeBounds);

        constraintSet.applyTo(rootView);
    }

    void undo()
    {
        if( pointer > 0 )
        {
            ToolType action = actions.get(pointer - 1);
            if( action == ToolType.ADJUST || action == ToolType.FILTER || action == ToolType.ROTATE
                    || action == ToolType.FRAME)
                photoEditorView.getSource().setImageBitmap(bitmapsToUndo.get(pointer - 1));
            else
                photoEditor.undo();
            pointer--;
        }
    }

    void redo()
    {
        if( pointer < actions.size() )
        {
            ToolType action = actions.get(pointer);
            if( action == ToolType.ADJUST || action == ToolType.FILTER || action == ToolType.ROTATE
                    || action == ToolType.FRAME)
                photoEditorView.getSource().setImageBitmap(bitmapsToRedo.get(pointer));
            else
                photoEditor.redo();
            pointer++;
        }
    }

    @Override
    public void onFrameSelected(int i)
    {
        Bitmap frame = BitmapFactory.decodeResource(getResources(), i);
        frame = Bitmap.createScaledBitmap(frame, bitmapBeforeChange.getWidth(), bitmapBeforeChange.getHeight(), true);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setAlpha(255);
        bitmapAfterChange = bitmapBeforeChange.copy(Bitmap.Config.ARGB_8888 , true);
        Canvas comboImage = new Canvas(bitmapAfterChange);
        comboImage.drawBitmap(frame, 0f, 0f, paint);
        photoEditorView.getSource().setImageBitmap(bitmapAfterChange);
    }

    void showFrames(boolean isVisible)
    {
        isFramesVisible = isVisible;
        constraintSet.clone(rootView);

        if (isVisible)
        {
            bitmapBeforeChange = getImageBitmap().copy(Bitmap.Config.ARGB_8888 , true);
            constraintSet.clear(frames.getId(), ConstraintSet.START);
            constraintSet.connect(frames.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
            constraintSet.connect(frames.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
        }
        else
        {
            constraintSet.connect(frames.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.END);
            constraintSet.clear(frames.getId(), ConstraintSet.END);
            bitmapsToUndo.put(pointer - 1, bitmapBeforeChange.copy(Bitmap.Config.ARGB_8888 , true));
            bitmapsToRedo.put(pointer - 1, getImageBitmap().copy(Bitmap.Config.ARGB_8888 , true));
        }

        ChangeBounds changeBounds = new ChangeBounds();
        changeBounds.setDuration(350);
        changeBounds.setInterpolator(new AnticipateOvershootInterpolator(1.0f));
        TransitionManager.beginDelayedTransition(rootView, changeBounds);

        constraintSet.applyTo(rootView);
    }
}