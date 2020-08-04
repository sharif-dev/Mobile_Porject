package com.example.photoeditor.Filters;


import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubFilter;

public class AdeleFilter
{
    public static String name = "Adele";

    public static Filter getFilter()
    {
        Filter filter = new Filter();
        filter.addSubFilter(new SaturationSubFilter(-100f));
        return filter;
    }
}