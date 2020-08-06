package com.example.photoeditor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class RotationFragment extends Fragment implements SeekBar.OnSeekBarChangeListener, View.OnClickListener
{
    private TextView angle;

    public RotationFragment()
    {
    }

    private RotationFragment.Tools tools;

    public interface Tools
    {
        void onFlipVertical();
        void onFlipHorizontal();
        void onAngleChanged(float degree);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.row_rotation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        SeekBar sbDegree = view.findViewById(R.id.sbDegree);
        angle = view.findViewById(R.id.txtDegree);
        ImageView flipV = view.findViewById(R.id.flip_v);
        ImageView flipH = view.findViewById(R.id.flip_h);

        sbDegree.setOnSeekBarChangeListener(this);
        flipV.setOnClickListener(this);
        flipH.setOnClickListener(this);
    }

    public void setToolsChangeListener(RotationFragment.Tools tools)
    {
        this.tools = tools;
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.flip_h:
                if( tools != null )
                    tools.onFlipHorizontal();
                break;
            case R.id.flip_v:
                if( tools != null )
                    tools.onFlipVertical();
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
    {
        if( tools != null )
        {
            float degree = (float) (3.6 * progress - 180);
            tools.onAngleChanged(degree);
            angle.setText(degree + "°");
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar)
    {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar)
    {

    }
}
