package backend.academy.flame.transformation.impl;

import backend.academy.flame.image.Point;
import backend.academy.flame.transformation.Transformation;

@SuppressWarnings("RecordComponentNumber")
public record AffineTransformation(
    double a1,
    double a2,
    double a3,
    double b1,
    double b2,
    double b3,
    int r,
    int g,
    int b
) implements Transformation {

    @Override
    public Point apply(Point point) {
        double x = a1 * point.x() + a2 * point.y() + a3;
        double y = b1 * point.x() + b2 * point.y() + b3;

        return new Point(x, y);
    }
}
