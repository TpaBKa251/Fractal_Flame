package backend.academy.flame.transformation.impl;

import backend.academy.flame.image.Point;
import backend.academy.flame.transformation.Transformation;

public class SpiralTransformation implements Transformation {

    @Override
    public Point apply(Point point) {
        double r = getRadius(point.x(), point.y());
        double o = getTheta(point.x(), point.y());

        double x = (Math.cos(o) + Math.sin(r)) / r;
        double y = (Math.sin(o) + Math.cos(r)) / r;

        return new Point(x, y);
    }
}
