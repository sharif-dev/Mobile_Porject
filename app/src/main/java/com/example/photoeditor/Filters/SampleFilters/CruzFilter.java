package com.example.photoeditor.Filters.SampleFilters;

import com.example.photoeditor.Filters.Filter;
import com.example.photoeditor.Filters.Subfilters.BrightnessSubFilter;
import com.example.photoeditor.Filters.Subfilters.ContrastSubFilter;
import com.example.photoeditor.Filters.Subfilters.SaturationSubFilter;

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
