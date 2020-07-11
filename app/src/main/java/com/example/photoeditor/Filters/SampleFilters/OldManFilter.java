package com.example.photoeditor.Filters.SampleFilters;

import com.example.photoeditor.Filters.Filter;
import com.example.photoeditor.Filters.Subfilters.BrightnessSubFilter;
import com.example.photoeditor.Filters.Subfilters.ColorOverlaySubFilter;
import com.example.photoeditor.Filters.Subfilters.ContrastSubFilter;
import com.example.photoeditor.Filters.Subfilters.SaturationSubFilter;

public class OldManFilter
{
    public static String name = "Old Man";

    public static Filter getFilter()
    {
        Filter filter = new Filter();
        filter.addSubFilter(new BrightnessSubFilter(30));
        filter.addSubFilter(new SaturationSubFilter(0.8f));
        filter.addSubFilter(new ContrastSubFilter(1.3f));
        filter.addSubFilter(new ColorOverlaySubFilter(100, .2f, .2f, .1f));
        return filter;
    }
}
