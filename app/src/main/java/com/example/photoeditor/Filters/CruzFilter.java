package com.example.photoeditor.Filters;


import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubFilter;

public class CruzFilter
{
    public static String name = "Cruz";

    public static Filter getFilter()
    {
        Filter filter = new Filter();
        filter.addSubFilter(new SaturationSubFilter(-100f));
        filter.addSubFilter(new ContrastSubFilter(1.3f));
        filter.addSubFilter(new BrightnessSubFilter(20));
        return filter;
    }
}
