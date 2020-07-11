package com.example.photoeditor.Filters.Subfilters;

import android.graphics.Bitmap;

import com.example.photoeditor.Filters.ImageProcessor;
import com.example.photoeditor.Filters.SubFilter;


public class BrightnessSubFilter implements SubFilter
{
    private static String tag = "";
    // Value is in integer
    private int brightness = 0;

    /**
     * Takes Brightness of the image
     *
     * @param brightness Integer brightness value {value 0 has no effect}
     */
    public BrightnessSubFilter(int brightness)
    {
        this.brightness = brightness;
    }

    @Override
    public Bitmap process(Bitmap inputImage)
    {
        return ImageProcessor.doBrightness(brightness, inputImage);
    }

    @Override
    public String getTag()
    {
        return tag;
    }

    @Override
    public void setTag(Object tag)
    {
        BrightnessSubFilter.tag = (String) tag;
    }

    public void setBrightness(int brightness)
    {
        this.brightness = brightness;
    }

    /**
     * Changes the brightness by the value passed as parameter
     */
    public void changeBrightness(int value)
    {
        this.brightness += value;
    }

    /**
     * Get current Brightness level
     */
    public int getBrightness()
    {
        return brightness;
    }
}
