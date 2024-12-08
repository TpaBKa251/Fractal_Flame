package backend.academy.flame.transformation.impl;

import backend.academy.flame.image.Point;
import backend.academy.flame.transformation.Transformation;

public class LinearTransformation implements Transformation {

    @Override
    public Point apply(Point point) {
        return new Point(point.x(), point.y());
    }
}
