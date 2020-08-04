package com.example.photoeditor.Filters;

import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ColorOverlaySubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubFilter;

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
