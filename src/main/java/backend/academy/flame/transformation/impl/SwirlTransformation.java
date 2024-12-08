package backend.academy.flame.transformation.impl;

import backend.academy.flame.image.Point;
import backend.academy.flame.transformation.Transformation;

public class SwirlTransformation implements Transformation {

    @Override
    public Point apply(Point point) {
        double r = getRadius(point.x(), point.y());

        double x = point.x() * Math.sin(r * r) - point.y() * Math.cos(r * r);
        double y = point.x() * Math.cos(r * r) + point.y() * Math.sin(r * r);

        return new Point(x, y);
    }
}
