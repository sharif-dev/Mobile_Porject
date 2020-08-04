package com.example.photoeditor.Filters;

import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter;

public class MarsFilter
{
    public static String name = "Mars";

    public static Filter getFilter()
    {
        Filter filter = new Filter();
        filter.addSubFilter(new ContrastSubFilter(1.5f));
        filter.addSubFilter(new BrightnessSubFilter(10));
        return filter;
    }
}
