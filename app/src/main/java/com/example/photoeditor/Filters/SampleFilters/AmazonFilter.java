package com.example.photoeditor.Filters.SampleFilters;

import com.example.photoeditor.Filters.Filter;
import com.example.photoeditor.Filters.Geometry.Point;
import com.example.photoeditor.Filters.Subfilters.ContrastSubFilter;
import com.example.photoeditor.Filters.Subfilters.ToneCurveSubFilter;

public class AmazonFilter
{
    public static String name = "Amazon";

    public static Filter getFilter()
    {
        Point[] blueKnots = new Point[6];
        blueKnots[0] = new Point(0f, 0f);
        blueKnots[1] = new Point(11f, 40f);
        blueKnots[2] = new Point(36f, 99f);
        blueKnots[3] = new Point(86f, 151f);
        blueKnots[4] = new Point(167f, 209f);
        blueKnots[5] = new Point(255f, 255f);
        Filter filter = new Filter();
        filter.addSubFilter(new ContrastSubFilter(1.2f));
        filter.addSubFilter(new ToneCurveSubFilter(null, null, null, blueKnots));
        return filter;
    }
}
