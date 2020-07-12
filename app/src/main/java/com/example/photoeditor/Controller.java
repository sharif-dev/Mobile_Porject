package com.example.photoeditor;

import android.content.Context;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Controller
{
    private static Controller controller;

    private Controller()
    {
    }

    public static Controller getInstance()
    {
        if(controller == null)
            controller = new Controller();
        return controller;
    }

    public void makeMenu(RecyclerView menu, Context context)
    {
        String[] iconText = {"Adjust", "Crop", "Rotate", "Filters", "Frames", "Text", "Blur", "Sticker"};
        int[] icon = {R.drawable.adjust, R.drawable.crop, R.drawable.rotate, R.drawable.filter,
                R.drawable.frame, R.drawable.text, R.drawable.blur, R.drawable.sticker};
        RecyclerView.LayoutManager recyclerViewLayoutManager = new LinearLayoutManager(context);
        menu.setLayoutManager(recyclerViewLayoutManager);
        MenuAdapter menuAdapter = new MenuAdapter(iconText, icon);
        LinearLayoutManager horizontalLayout = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        menu.setLayoutManager(horizontalLayout);
        menu.setAdapter(menuAdapter);
    }

    public void controlPanel(ImageView cancel, ImageView undo, ImageView redo, ImageView ok, Context context)
    {

    }
}
