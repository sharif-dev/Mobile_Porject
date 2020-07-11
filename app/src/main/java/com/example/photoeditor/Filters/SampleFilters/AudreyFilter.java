package com.example.photoeditor.Filters.SampleFilters;

import com.example.photoeditor.Filters.Filter;
import com.example.photoeditor.Filters.Geometry.Point;
import com.example.photoeditor.Filters.Subfilters.BrightnessSubFilter;
import com.example.photoeditor.Filters.Subfilters.ContrastSubFilter;
import com.example.photoeditor.Filters.Subfilters.SaturationSubFilter;
import com.example.photoeditor.Filters.Subfilters.ToneCurveSubFilter;

public class AudreyFilter
{
    public static String name = "Audrey";

    public static Filter getFilter()
    {
        Filter filter = new Filter();

        Point[] redKnots = new Point[3];
        redKnots[0] = new Point(0f, 0f);
        redKnots[1] = new Point(124f, 138f);
        redKnots[2] = new Point(255f, 255f);

        filter.addSubFilter(new SaturationSubFilter(-100f));
        filter.addSubFilter(new ContrastSubFilter(1.3f));
        filter.addSubFilter(new BrightnessSubFilter(20));
        filter.addSubFilter(new ToneCurveSubFilter(null, redKnots, null, null));
        return filter;
    }
}
