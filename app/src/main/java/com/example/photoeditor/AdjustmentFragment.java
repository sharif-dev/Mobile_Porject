package com.example.photoeditor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AdjustmentFragment extends Fragment implements SeekBar.OnSeekBarChangeListener
{

    private TextView txtBrightness;
    private TextView txtContrast;
    private TextView txtSaturation;
    private TextView txtVignette;

    public AdjustmentFragment()
    {
    }

    private AdjustmentFragment.Tools tools;

    public interface Tools
    {
        void onBrightnessChanged(int value);
        void onContrastChanged(float value);
        void onSaturationChanged(float value);
        void onVignetteChanged(int value);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.row_adjustment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        SeekBar sbBrightness = view.findViewById(R.id.sbBrightness);
        SeekBar sbContrast = view.findViewById(R.id.sbContrast);
        SeekBar sbSaturation = view.findViewById(R.id.sbSaturation);
        SeekBar sbVignette = view.findViewById(R.id.sbVignette);

        txtBrightness = view.findViewById(R.id.txtBrightness);
        txtContrast = view.findViewById(R.id.txtContrast);
        txtSaturation = view.findViewById(R.id.txtSaturation);
        txtVignette = view.findViewById(R.id.txtVignette);

        sbBrightness.setOnSeekBarChangeListener(this);
        sbContrast.setOnSeekBarChangeListener(this);
        sbSaturation.setOnSeekBarChangeListener(this);
        sbVignette.setOnSeekBarChangeListener(this);
    }

    public void setToolsChangeListener(AdjustmentFragment.Tools tools)
    {
        this.tools = tools;
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
    {
        switch (seekBar.getId())
        {
            case R.id.sbBrightness:
                if( tools != null )
                {
                    tools.onBrightnessChanged(progress - 50);
                    txtBrightness.setText(String.valueOf(progress - 50));
                }
                break;
            case R.id.sbContrast:
                if( tools != null )
                {
                    tools.onContrastChanged((float) progress / 50);
                    txtContrast.setText(String.valueOf((float) progress / 50));
                }
                break;
            case R.id.sbSaturation:
                if( tools != null )
                {
                    tools.onSaturationChanged((float) (progress - 50) / 10);
                    txtSaturation.setText(String.valueOf((float) (progress - 50) / 10));
                }
                break;
            case R.id.sbVignette:
                if( tools != null )
                {
                    tools.onVignetteChanged((int) (progress * 2.25));
                    txtVignette.setText(String.valueOf((int) (progress * 2.25)));
                }
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
