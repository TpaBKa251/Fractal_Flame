package backend.academy.flame;

import backend.academy.flame.image.Point;
import backend.academy.flame.transformation.impl.AffineTransformation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Тесты аффинного преобразования")
class AffineTransformationTest {

    @ParameterizedTest(name = "Изначальная точка: ({0}; {1}), " +
        "коэффициенты: a1={2}; a2={3}; a3={4}; b1={5}; b2={6}; b3={7}, " +
        "ожидаемая точка: ({8}; {9})")
    @CsvSource({
        "1.0, 1.0, 2.0, 0.0, 1.0, 0.0, 2.0, 1.0, 3.0, 3.0",
        "2.0, 3.0, 1.0, 2.0, 0.0, 0.0, 1.0, 2.0, 8.0, 5.0",
        "0.0, 0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0"
    })
    void testApply(
        double x, double y,
        double a1, double a2, double a3,
        double b1, double b2, double b3,
        double expectedX, double expectedY
    ) {
        AffineTransformation transformation = new AffineTransformation(a1, a2, a3, b1, b2, b3, 255, 255, 255);

        Point result = transformation.apply(new Point(x, y));

        assertEquals(expectedX, result.x(), 0.0001, "X-координата не совпадает");
        assertEquals(expectedY, result.y(), 0.0001, "Y-координата не совпадает");
    }
}
