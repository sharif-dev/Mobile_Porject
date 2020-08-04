package com.example.photoeditor.Filters;


import com.zomato.photofilters.geometry.Point;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ToneCurveSubFilter;

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
