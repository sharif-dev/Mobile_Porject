package com.example.photoeditor.Filters;


import com.zomato.photofilters.geometry.Point;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ToneCurveSubFilter;

public class HaanFilter
{
    public static String name = "Haan";

    public static Filter getFilter()
    {
        Point[] greenKnots = new Point[3];
        greenKnots[0] = new Point(0f, 0f);
        greenKnots[1] = new Point(113f, 142f);
        greenKnots[2] = new Point(255f, 255f);

        Filter filter = new Filter();
        filter.addSubFilter(new ContrastSubFilter(1.3f));
        filter.addSubFilter(new BrightnessSubFilter(60));
        filter.addSubFilter(new ToneCurveSubFilter(null, null, greenKnots, null));
        return filter;
    }
}
