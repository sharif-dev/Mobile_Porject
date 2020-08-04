package com.example.photoeditor.Filters;

import com.zomato.photofilters.geometry.Point;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.ColorOverlaySubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ToneCurveSubFilter;

public class SepiaFilter
{
    public static String name = "Sepia";

    public static Filter getFilter()
    {
        Point[] rgbKnots;
        rgbKnots = new Point[3];
        rgbKnots[0] = new Point(0, 0);
        rgbKnots[1] = new Point(73, 38);
        rgbKnots[2] = new Point(255, 255);
        Filter filter = new Filter();
        filter.addSubFilter(new SaturationSubFilter((float) -0.99));
        filter.addSubFilter(new ColorOverlaySubFilter(50,0,(float)0.38,0));
        filter.addSubFilter(new ToneCurveSubFilter(rgbKnots, null, null, null));
        return filter;
    }
}
