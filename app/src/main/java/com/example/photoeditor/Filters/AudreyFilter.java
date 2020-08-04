package com.example.photoeditor.Filters;


import com.zomato.photofilters.geometry.Point;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ToneCurveSubFilter;

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
