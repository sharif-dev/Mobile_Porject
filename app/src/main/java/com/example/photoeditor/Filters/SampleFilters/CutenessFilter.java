package com.example.photoeditor.Filters.SampleFilters;

import com.example.photoeditor.Filters.Filter;
import com.example.photoeditor.Filters.Geometry.Point;
import com.example.photoeditor.Filters.Subfilters.SaturationSubFilter;
import com.example.photoeditor.Filters.Subfilters.ToneCurveSubFilter;

public class CutenessFilter
{
    public static String name = "Cuteness";

    public static Filter getFilter()
    {
        Point[] rgbKnots;
        rgbKnots = new Point[3];
        rgbKnots[0] = new Point(0, 0);
        rgbKnots[1] = new Point(65, 34);
        rgbKnots[2] = new Point(255, 255);
        Filter filter = new Filter();
        filter.addSubFilter(new ToneCurveSubFilter(rgbKnots, null, null, null));
        filter.addSubFilter(new SaturationSubFilter((float) 1.72));
        return filter;
    }
}
