package com.example.photoeditor.Filters;

import android.graphics.Bitmap;


public interface SubFilter
{
    Bitmap process(Bitmap inputImage);

    Object getTag();

    void setTag(Object tag);
}
