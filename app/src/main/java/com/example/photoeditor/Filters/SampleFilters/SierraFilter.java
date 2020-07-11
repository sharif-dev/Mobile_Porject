package com.example.photoeditor.Filters.SampleFilters;

import com.example.photoeditor.Filters.Filter;
import com.example.photoeditor.Filters.Geometry.Point;
import com.example.photoeditor.Filters.Subfilters.BrightnessSubFilter;
import com.example.photoeditor.Filters.Subfilters.ContrastSubFilter;
import com.example.photoeditor.Filters.Subfilters.ToneCurveSubFilter;

public class SierraFilter
{
    public static String name = "Sierra";

    public static Filter getFilter()
    {
        Point[] rgbKnots = new Point[2];
        Point[] redKnots = new Point[2];

        rgbKnots[0] = new Point(0f, 54f);
        rgbKnots[1] = new Point(255f, 255f);

        redKnots[0] = new Point(0f, 21f);
        redKnots[1] = new Point(255f, 255f);


        Filter filter = new Filter();
        filter.addSubFilter(new ToneCurveSubFilter(rgbKnots, redKnots, null, null));
        filter.addSubFilter(new ContrastSubFilter(1.33f));
        filter.addSubFilter(new BrightnessSubFilter(-30));
        return filter;
    }
}
