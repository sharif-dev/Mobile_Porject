package com.example.photoeditor.Filters;


import android.graphics.Bitmap;

import com.example.photoeditor.Filters.Subfilters.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Filter
{
    private List<SubFilter> subFilters = new ArrayList<>();

    public Filter(Filter filter)
    {
        this.subFilters = filter.subFilters;
    }

    public Filter()
    {
    }

    public void setSubFilters(List<SubFilter> subFilters)
    {
        this.subFilters = subFilters;
    }

    public void addSubFilter(SubFilter subFilter)
    {
        subFilters.add(subFilter);
    }

    public void addSubFilters(List<SubFilter> subFilterList)
    {
        subFilters.addAll(subFilterList);
    }

    public List<SubFilter> getSubFilters()
    {
        if (subFilters == null || subFilters.isEmpty())
            return new ArrayList<>(0);
        return new ArrayList<>(subFilters);
    }

    public void clearSubFilters()
    {
        subFilters.clear();
    }

    public void removeSubFilterWithTag(String tag)
    {
        Iterator<SubFilter> iterator = subFilters.iterator();
        while (iterator.hasNext())
        {
            SubFilter subFilter = iterator.next();
            if (subFilter.getTag().equals(tag))
            {
                iterator.remove();
            }
        }
    }

    public SubFilter getSubFilterByTag(String tag)
    {
        for (SubFilter subFilter : subFilters)
        {
            if (subFilter.getTag().equals(tag))
            {
                return subFilter;
            }
        }
        return null;
    }

    public Bitmap processFilter(Bitmap inputImage)
    {
        Bitmap outputImage = inputImage;
        if (outputImage != null)
        {
            for (SubFilter subFilter : subFilters)
            {
                try
                {
                    outputImage = subFilter.process(outputImage);
                }
                catch (OutOfMemoryError oe)
                {
                    System.gc();
                    try
                    {
                        outputImage = subFilter.process(outputImage);
                    }
                    catch (OutOfMemoryError ignored)
                    {
                    }
                }
            }
        }

        return outputImage;
    }

}
