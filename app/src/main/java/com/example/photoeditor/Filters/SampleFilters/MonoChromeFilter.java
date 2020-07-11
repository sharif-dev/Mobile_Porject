package com.example.photoeditor.Filters.SampleFilters;

import com.example.photoeditor.Filters.Filter;
import com.example.photoeditor.Filters.Subfilters.SaturationSubFilter;

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
