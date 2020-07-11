package com.example.photoeditor.Filters.SampleFilters;

import com.example.photoeditor.Filters.Filter;
import com.example.photoeditor.Filters.Geometry.Point;
import com.example.photoeditor.Filters.Subfilters.ToneCurveSubFilter;

public class MayFairFilte
{
    public static String name = "May Fair";

    public static Filter getFilter()
    {
        Point[] rgbKnots = new Point[8];
        rgbKnots[0] = new Point(0f, 0f);
        rgbKnots[1] = new Point(34f, 6f);
        rgbKnots[2] = new Point(69f, 23f);
        rgbKnots[3] = new Point(100f, 58f);
        rgbKnots[4] = new Point(150f, 154f);
        rgbKnots[5] = new Point(176f, 196f);
        rgbKnots[6] = new Point(207f, 233f);
        rgbKnots[7] = new Point(255f, 255f);
        Filter filter = new Filter();
        filter.addSubFilter(new ToneCurveSubFilter(rgbKnots, null, null, null));
        return filter;
    }
}
