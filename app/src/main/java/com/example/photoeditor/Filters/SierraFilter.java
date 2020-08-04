package com.example.photoeditor.Filters;

import com.zomato.photofilters.geometry.Point;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ToneCurveSubFilter;

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
