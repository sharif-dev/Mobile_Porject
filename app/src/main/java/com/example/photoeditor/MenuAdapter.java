package com.example.photoeditor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MyView>
{
    private String[] iconText;
    private int[] icon;

    public class MyView extends RecyclerView.ViewHolder
    {
        TextView iconText;
        ImageView icon;

        public MyView(View view)
        {
            super(view);
            iconText = (TextView)view.findViewById(R.id.icon_name);
            icon = (ImageView)view.findViewById(R.id.icon_image);
        }
    }

    public MenuAdapter(String[] iconText, int[] icon)
    {
        this.icon = icon;
        this.iconText = iconText;
    }

    @Override
    public MyView onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent,  false);
        return new MyView(itemView);
    }

    @Override
    public void onBindViewHolder(final MyView holder, final int position)
    {
        holder.iconText.setText(iconText[position]);
        holder.icon.setImageResource(icon[position]);
    }

    @Override
    public int getItemCount()
    {
        return icon.length;
    }
}
