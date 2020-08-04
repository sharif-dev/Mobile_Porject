package com.example.photoeditor.Filters;

import com.zomato.photofilters.geometry.Point;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ToneCurveSubFilter;

public class ClarendonFilter
{
    public static String name = "Clarendon";

    public static Filter getFilter()
    {
        Point[] redKnots = new Point[4];
        Point[] greenKnots = new Point[4];
        Point[] blueKnots = new Point[4];

        redKnots[0] = new Point(0f, 0f);
        redKnots[1] = new Point(56f, 68f);
        redKnots[2] = new Point(196f, 206f);
        redKnots[3] = new Point(255f, 255f);


        greenKnots[0] = new Point(0f, 0f);
        greenKnots[1] = new Point(46f, 77f);
        greenKnots[2] = new Point(160f, 200f);
        greenKnots[3] = new Point(255f, 255f);


        blueKnots[0] = new Point(0f, 0f);
        blueKnots[1] = new Point(33f, 86f);
        blueKnots[2] = new Point(126f, 220f);
        blueKnots[3] = new Point(255f, 255f);

        Filter filter = new Filter();
        filter.addSubFilter(new ContrastSubFilter(1.5f));
        filter.addSubFilter(new BrightnessSubFilter(-10));
        filter.addSubFilter(new ToneCurveSubFilter(null, redKnots, greenKnots, blueKnots));
        return filter;
    }
}
