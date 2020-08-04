package com.example.photoeditor.Filters;

import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubFilter;

public class MetropolisFilter
{
    public static String name = "Metropolis";

    public static Filter getFilter()
    {
        Filter filter = new Filter();
        filter.addSubFilter(new SaturationSubFilter(-1f));
        filter.addSubFilter(new ContrastSubFilter(1.7f));
        filter.addSubFilter(new BrightnessSubFilter(70));
        return filter;
    }
}
