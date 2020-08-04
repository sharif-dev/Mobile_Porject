package com.example.photoeditor.Filters;

import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubFilter;

public class MonoChromeFilter
{
    public static String name = "Mono Chrome";

    public static Filter getFilter()
    {
        Filter filter = new Filter();
        filter.addSubFilter(new SaturationSubFilter(-10));
        return filter;
    }
}
