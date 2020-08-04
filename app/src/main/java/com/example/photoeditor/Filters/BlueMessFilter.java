package com.example.photoeditor.Filters;

import com.zomato.photofilters.geometry.Point;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ToneCurveSubFilter;

public class BlueMessFilter
{
    public static String name = "Blue Mess";

    public static Filter getFilter()
    {
        Point[] redKnots;
        redKnots = new Point[8];
        redKnots[0] = new Point(0, 0);
        redKnots[1] = new Point(86, 34);
        redKnots[2] = new Point(117, 41);
        redKnots[3] = new Point(146, 80);
        redKnots[4] = new Point(170, 151);
        redKnots[5] = new Point(200, 214);
        redKnots[6] = new Point(225, 242);
        redKnots[7] = new Point(255, 255);
        Filter filter = new Filter();
        filter.addSubFilter(new ToneCurveSubFilter(null, redKnots, null, null));
        filter.addSubFilter(new BrightnessSubFilter(30));
        filter.addSubFilter(new ContrastSubFilter(1f));
        return filter;
    }
}
