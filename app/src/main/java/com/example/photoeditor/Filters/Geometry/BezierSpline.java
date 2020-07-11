package com.example.photoeditor.Filters.Geometry;

import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Build;
import android.view.animation.PathInterpolator;

import androidx.annotation.RequiresApi;

public class BezierSpline
{
    private BezierSpline()
    {
    }

    /**
     * Generates Curve {in a plane ranging from 0-255} using the knots provided
     */
    public static int[] curveGenerator(Point[] knots)
    {
        if (knots == null)
        {
            throw new NullPointerException("Knots cannot be null");
        }

        int n = knots.length - 1;
        if (n < 1)
        {
            throw new IllegalArgumentException("Atleast two points are required");
        }

        if (Build.VERSION.SDK_INT >= 21)
        {
            return getOutputPointsForNewerDevices(knots);
        }
        else
        {
            return getOutputPointsForOlderDevices(knots);
        }
    }

    // This is for lollipop and newer devices
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private static int[] getOutputPointsForNewerDevices(Point[] knots)
    {

        Point[] controlPoints = calculateControlPoints(knots);
        Path path = new Path();
        path.moveTo(0, 0);
        path.lineTo(knots[0].getX() / 255.0f, knots[0].getY() / 255.0f);
        path.moveTo(knots[0].getX() / 255.0f, knots[0].getY() / 255.0f);

        for (int index = 1; index < knots.length; index++)
        {
            path.quadTo(
                    controlPoints[index - 1].getX() / 255.0f,
                    controlPoints[index - 1].getY() / 255.0f,
                    knots[index].getX() / 255.0f,
                    knots[index].getY() / 255.0f
            );
            path.moveTo(knots[index].getX() / 255.0f, knots[index].getY() / 255.0f);
        }

        path.lineTo(1, 1);
        path.moveTo(1, 1);

        float[] allPoints = new float[256];

        for (int x = 0; x < 256; x++)
        {
            PathInterpolator pathInterpolator = new PathInterpolator(path);
            allPoints[x] = 255.0f * pathInterpolator.getInterpolation((float) x / 255.0f);
        }

        allPoints[0] = knots[0].getY();
        allPoints[255] = knots[knots.length - 1].getY();
        return validateCurve(allPoints);
    }


    //This is for devices older than lollipop
    private static int[] getOutputPointsForOlderDevices(Point[] knots)
    {
        Point[] controlPoints = calculateControlPoints(knots);
        Path path = new Path();
        path.moveTo(0, 0);
        path.lineTo(knots[0].getX(), knots[0].getY());
        path.moveTo(knots[0].getX(), knots[0].getY());

        for (int index = 1; index < knots.length; index++)
        {
            path.quadTo(controlPoints[index - 1].getX(), controlPoints[index - 1].getY(), knots[index].getX(), knots[index].getY());
            path.moveTo(knots[index].getX(), knots[index].getY());
        }

        path.lineTo(255, 255);
        path.moveTo(255, 255);

        float[] allPoints = new float[256];

        PathMeasure pm = new PathMeasure(path, false);
        for (int i = 0; i < 256; i++)
        {
            allPoints[i] = -1;
        }

        int x = 0;
        float[] acoordinates = {0, 0};

        do
        {
            float pathLength = pm.getLength();
            for (float i = 0; i <= pathLength; i += 0.08f)
            {
                pm.getPosTan(i, acoordinates, null);
                if ((int) (acoordinates[0]) > x && x < 256)
                {
                    allPoints[x] = acoordinates[1];
                    x++;
                }
                if (x > 255)
                {
                    break;
                }
            }
        } while (pm.nextContour());


        allPoints[0] = knots[0].getY();
        for (int i = 0; i < 256; i++)
        {
            if (allPoints[i] == -1)
            {
                allPoints[i] = allPoints[i - 1];
            }
        }
        allPoints[255] = knots[knots.length - 1].getY();
        return validateCurve(allPoints);
    }

    private static int[] validateCurve(float[] allPoints)
    {
        int[] curvedPath = new int[256];
        for (int x = 0; x < 256; x++)
        {
            if (allPoints[x] > 255.0f)
            {
                curvedPath[x] = 255;
            }
            else if (allPoints[x] < 0.0f)
            {
                curvedPath[x] = 0;
            }
            else
            {
                curvedPath[x] = Math.round(allPoints[x]);
            }
        }
        return curvedPath;
    }

    // Calculates the control points for the specified knots
    private static Point[] calculateControlPoints(Point[] knots)
    {
        int n = knots.length - 1;
        Point[] controlPoints = new Point[n];

        if (n == 1) { // Special case: Bezier curve should be a straight line.
            // 3P1 = 2P0 + P3
            controlPoints[0] = new Point((2 * knots[0].getX() + knots[1].getX()) / 3, (2 * knots[0].getY() + knots[1].getY()) / 3);
            // P2 = 2P1 – P0
            //controlPoints[1][0] = new Point(2*controlPoints[0][0].getX() - knots[0].getX(), 2*controlPoints[0][0].getY()-knots[0].getY());
        }
        else
        {
            // Calculate first Bezier control points
            // Right hand side vector
            float[] rhs = new float[n];

            // Set right hand side x values
            for (int i = 1; i < n - 1; ++i)
            {
                rhs[i] = 4 * knots[i].getX() + 2 * knots[i + 1].getX();
            }
            rhs[0] = knots[0].getX() + 2 * knots[1].getX();
            rhs[n - 1] = (8 * knots[n - 1].getX() + knots[n].getX()) / 2.0f;
            // Get first control points x-values
            float[] x = getFirstControlPoints(rhs);

            // Set right hand side y values
            for (int i = 1; i < n - 1; ++i)
            {
                rhs[i] = 4 * knots[i].getY() + 2 * knots[i + 1].getY();
            }
            rhs[0] = knots[0].getY() + 2 * knots[1].getY();
            rhs[n - 1] = (8 * knots[n - 1].getY() + knots[n].getY()) / 2.0f;
            // Get first control points y-values
            float[] y = getFirstControlPoints(rhs);
            for (int i = 0; i < n; ++i)
            {
                controlPoints[i] = new Point(x[i], y[i]);
            }
        }

        return controlPoints;
    }

    private static float[] getFirstControlPoints(float[] rhs)
    {
        int n = rhs.length;
        float[] x = new float[n]; // Solution vector.
        float[] tmp = new float[n]; // Temp workspace.

        float b = 1.0f;   // Control Point Factor
        x[0] = rhs[0] / b;
        for (int i = 1; i < n; i++) // Decomposition and forward substitution.
        {
            tmp[i] = 1 / b;
            b = (i < n - 1 ? 4.0f : 3.5f) - tmp[i];
            x[i] = (rhs[i] - x[i - 1]) / b;
        }
        for (int i = 1; i < n; i++)
        {
            x[n - i - 1] -= tmp[n - i] * x[n - i]; // Backsubstitution.
        }
        return x;
    }
}
