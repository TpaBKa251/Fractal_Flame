package backend.academy.flame.transformation;

import backend.academy.flame.image.Point;
import java.util.function.Function;

public interface Transformation extends Function<Point, Point> {

    default double getRadius(double x, double y) {
        return Math.sqrt(x * x + y * y);
    }

    default double getTheta(double x, double y) {
        return Math.atan2(x, y);
    }
}
