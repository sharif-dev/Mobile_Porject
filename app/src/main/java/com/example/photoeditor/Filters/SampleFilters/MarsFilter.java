package com.example.photoeditor.Filters.SampleFilters;

import com.example.photoeditor.Filters.Filter;
import com.example.photoeditor.Filters.Subfilters.BrightnessSubFilter;
import com.example.photoeditor.Filters.Subfilters.ContrastSubFilter;

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
