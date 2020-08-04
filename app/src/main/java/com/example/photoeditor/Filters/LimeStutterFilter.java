package com.example.photoeditor.Filters;

import com.zomato.photofilters.geometry.Point;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.ToneCurveSubFilter;

public class LimeStutterFilter
{
    public static String name = "Lime Stutter";

    public static Filter getFilter()
    {
        Point[] blueKnots;
        blueKnots = new Point[3];
        blueKnots[0] = new Point(0, 0);
        blueKnots[1] = new Point(165, 114);
        blueKnots[2] = new Point(255, 255);
        Filter filter = new Filter();
        filter.addSubFilter(new ToneCurveSubFilter(null, null, null, blueKnots));
        return filter;
    }
}
