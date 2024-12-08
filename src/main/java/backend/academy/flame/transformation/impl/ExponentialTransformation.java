package backend.academy.flame.transformation.impl;

import backend.academy.flame.image.Point;
import backend.academy.flame.transformation.Transformation;

public class ExponentialTransformation implements Transformation {

    @Override
    public Point apply(Point point) {
        double x = Math.exp(point.x() - 1) * Math.cos(Math.PI * point.y());
        double y = Math.exp(point.x() - 1) * Math.sin(Math.PI * point.y());

        return new Point(x, y);
    }
}
