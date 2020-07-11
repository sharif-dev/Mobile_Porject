package com.example.photoeditor.Filters.SampleFilters;

import com.example.photoeditor.Filters.Filter;
import com.example.photoeditor.Filters.Geometry.Point;
import com.example.photoeditor.Filters.Subfilters.BrightnessSubFilter;
import com.example.photoeditor.Filters.Subfilters.ColorOverlaySubFilter;
import com.example.photoeditor.Filters.Subfilters.SaturationSubFilter;
import com.example.photoeditor.Filters.Subfilters.ToneCurveSubFilter;

public class PurplishFilter
{
    public static String name = "Purplish";

    public static Filter getFilter()
    {
        Point[] greenKnots;
        greenKnots = new Point[8];
        greenKnots[0] = new Point(0, 0);
        greenKnots[1] = new Point(86, 34);
        greenKnots[2] = new Point(117, 41);
        greenKnots[3] = new Point(146, 80);
        greenKnots[4] = new Point(170, 151);
        greenKnots[5] = new Point(200, 214);
        greenKnots[6] = new Point(225, 242);
        greenKnots[7] = new Point(255, 255);
        Filter filter = new Filter();
        filter.addSubFilter(new SaturationSubFilter((float) -1));
        filter.addSubFilter(new ColorOverlaySubFilter(80,(float) 0.1,0,(float) 0.9));
        filter.addSubFilter(new BrightnessSubFilter(-30));
        filter.addSubFilter(new ToneCurveSubFilter(null,null , greenKnots, null));
        return filter;
    }
}
