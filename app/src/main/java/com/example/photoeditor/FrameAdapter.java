package com.example.photoeditor;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.icu.lang.UProperty;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FrameAdapter extends RecyclerView.Adapter<FrameAdapter.ViewHolder>
{

    private List<Integer> frameList = new ArrayList<>();
    private List<Bitmap> processedFrames = new ArrayList<>();
    private OnItemSelected onItemSelected;

    public FrameAdapter(Context context, OnItemSelected onItemSelected, Bitmap bitmap)
    {
        this.onItemSelected = onItemSelected;
        setFrames();
        setupFrames(context, bitmap);
    }

    private void setFrames()
    {
        frameList.add(R.drawable.frame1);
        frameList.add(R.drawable.frame2);
        frameList.add(R.drawable.frame3);
        frameList.add(R.drawable.frame4);
        frameList.add(R.drawable.frame5);
        frameList.add(R.drawable.frame6);
        frameList.add(R.drawable.frame7);
        frameList.add(R.drawable.frame8);
        frameList.add(R.drawable.frame9);
        frameList.add(R.drawable.frame10);
        frameList.add(R.drawable.frame11);
        frameList.add(R.drawable.frame12);
        frameList.add(R.drawable.frame13);
        frameList.add(R.drawable.frame14);
        frameList.add(R.drawable.frame15);
    }

    public interface OnItemSelected
    {
        void onFrameSelected(int i);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_frames, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        holder.imgFrame.setImageBitmap(processedFrames.get(position));
    }

    @Override
    public int getItemCount()
    {
        return frameList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imgFrame;

        ViewHolder(View itemView)
        {
            super(itemView);
            imgFrame = itemView.findViewById(R.id.imgFrame);
            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    onItemSelected.onFrameSelected(frameList.get(getLayoutPosition()));
                }
            });
        }
    }

    private void setupFrames(Context context, Bitmap bitmap)
    {
        for (Integer i : frameList)
        {
            int size = (int)context.getResources().getDimension(R.dimen.thumbnail_size);
            Bitmap frame = BitmapFactory.decodeResource(context.getResources(), i);
            frame = Bitmap.createScaledBitmap(frame, size, size, true);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setAlpha(255);
            Bitmap processedBitmap = bitmap.copy(Bitmap.Config.ARGB_8888 , true);
            processedBitmap = Bitmap.createScaledBitmap(processedBitmap, size, size, true);
            Canvas comboImage = new Canvas(processedBitmap);
            comboImage.drawBitmap(frame, 0f, 0f, paint);
            processedFrames.add(processedBitmap);
        }
    }
}
