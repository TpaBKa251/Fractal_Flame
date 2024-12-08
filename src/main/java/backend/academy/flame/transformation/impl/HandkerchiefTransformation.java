package backend.academy.flame.transformation.impl;

import backend.academy.flame.image.Point;
import backend.academy.flame.transformation.Transformation;

public class HandkerchiefTransformation implements Transformation {

    @Override
    public Point apply(Point point) {
        double r = getRadius(point.x(), point.y());
        double o = getTheta(point.x(), point.y());

        double x = r * Math.sin(o + r);
        double y = r * Math.cos(o - r);

        return new Point(x, y);
    }
}
