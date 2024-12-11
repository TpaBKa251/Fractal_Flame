package backend.academy.flame;

import backend.academy.flame.image.Point;
import backend.academy.flame.transformation.Transformation;
import backend.academy.flame.transformation.impl.ExponentialTransformation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Тесты Exponential (экспоненциальной) трансформации")
class ExponentialTransformationTest {

    private final Transformation transformation = new ExponentialTransformation();

    @ParameterizedTest(name = "Исходная тока: ({0}; {1}), ожидаемая точка: ({2}; {3})")
    @CsvSource({
        "1.0, 0.0, 1.0, 0.0",
        "0.0, 1.0, -0.367879, 4.505224e-17",
        "1.0, 1.0, -1.0, 1.224647e-16"
    })
    void testApply(double x, double y, double expectedX, double expectedY) {
        Point result = transformation.apply(new Point(x, y));

        assertEquals(expectedX, result.x(), 0.0001, "X-координата не совпадает");
        assertEquals(expectedY, result.y(), 0.0001, "Y-координата не совпадает");
    }
}
