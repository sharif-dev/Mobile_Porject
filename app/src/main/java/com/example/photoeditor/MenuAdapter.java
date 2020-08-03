package com.example.photoeditor;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder>
{

    private List<ToolModel> toolList = new ArrayList<>();
    private OnItemSelected onItemSelected;

    public MenuAdapter(OnItemSelected onItemSelected)
    {
        this.onItemSelected = onItemSelected;
        toolList.add(new ToolModel("Adjust", R.drawable.adjust, ToolType.ADJUST));
        toolList.add(new ToolModel("Crop", R.drawable.crop, ToolType.CROP));
        toolList.add(new ToolModel("Filter", R.drawable.filter, ToolType.FILTER));
        toolList.add(new ToolModel("Rotate", R.drawable.rotate, ToolType.ROTATE));
        toolList.add(new ToolModel("Brush", R.drawable.brush, ToolType.BRUSH));
        toolList.add(new ToolModel("Text", R.drawable.text, ToolType.TEXT));
        toolList.add(new ToolModel("Frame", R.drawable.frame, ToolType.FRAME));
        toolList.add(new ToolModel("Blur", R.drawable.blur, ToolType.BLUR));
        toolList.add(new ToolModel("Eraser", R.drawable.eraser, ToolType.ERASER));
        toolList.add(new ToolModel("Emoji", R.drawable.emoji, ToolType.EMOJI));
    }

    public interface OnItemSelected
    {
        void onToolSelected(ToolType toolType);
    }

    class ToolModel
    {
        private String toolName;
        private int toolIcon;
        private ToolType toolType;

        ToolModel(String toolName, int toolIcon, ToolType toolType)
        {
            this.toolName = toolName;
            this.toolIcon = toolIcon;
            this.toolType = toolType;
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_editing_tools, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        ToolModel item = toolList.get(position);
        holder.txtTool.setText(item.toolName);
        holder.imgToolIcon.setImageResource(item.toolIcon);
    }

    @Override
    public int getItemCount()
    {
        return toolList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imgToolIcon;
        TextView txtTool;

        ViewHolder(View itemView)
        {
            super(itemView);
            imgToolIcon = itemView.findViewById(R.id.imgToolIcon);
            txtTool = itemView.findViewById(R.id.txtTool);
            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    onItemSelected.onToolSelected(toolList.get(getLayoutPosition()).toolType);
                }
            });
        }
    }
}
