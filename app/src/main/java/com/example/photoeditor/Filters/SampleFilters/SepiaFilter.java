package com.example.photoeditor.Filters.SampleFilters;

import com.example.photoeditor.Filters.Filter;
import com.example.photoeditor.Filters.Geometry.Point;
import com.example.photoeditor.Filters.Subfilters.ColorOverlaySubFilter;
import com.example.photoeditor.Filters.Subfilters.SaturationSubFilter;
import com.example.photoeditor.Filters.Subfilters.ToneCurveSubFilter;

public class SepiaFilter
{
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
