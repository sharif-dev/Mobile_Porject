package com.example.photoeditor.Filters.SampleFilters;

import com.example.photoeditor.Filters.Filter;
import com.example.photoeditor.Filters.Subfilters.SaturationSubFilter;

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