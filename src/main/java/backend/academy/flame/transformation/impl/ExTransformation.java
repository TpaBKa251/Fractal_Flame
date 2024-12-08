package backend.academy.flame.transformation.impl;

import backend.academy.flame.image.Point;
import backend.academy.flame.transformation.Transformation;

public class ExTransformation implements Transformation {

    private final static int POW_NUMBER = 3;

    @Override
    public Point apply(Point point) {
        double r = getRadius(point.x(), point.y());
        double o = getTheta(point.x(), point.y());
        double p0 = Math.sin(o + r);
        double p1 = Math.cos(o - r);

        double x = r * (Math.pow(p0, POW_NUMBER) + Math.pow(p1, POW_NUMBER));
        double y = r * (Math.pow(p0, POW_NUMBER) - Math.pow(p1, POW_NUMBER));

        return new Point(x, y);
    }
}
