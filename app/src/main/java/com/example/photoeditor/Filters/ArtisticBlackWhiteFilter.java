package com.example.photoeditor.Filters;


import com.zomato.photofilters.geometry.Point;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ToneCurveSubFilter;

public class ArtisticBlackWhiteFilter
{
    public static String name = "Artistic Black White";

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
        filter.addSubFilter(new SaturationSubFilter((float) -0.99));
        return filter;
    }
}
