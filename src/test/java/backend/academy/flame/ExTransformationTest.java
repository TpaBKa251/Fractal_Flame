package backend.academy.flame;

import backend.academy.flame.image.Point;
import backend.academy.flame.transformation.Transformation;
import backend.academy.flame.transformation.impl.ExTransformation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Тесты X трансформации")
class ExTransformationTest {

    private final Transformation transformation = new ExTransformation();

    @ParameterizedTest(name = "Исходная тока: ({0}; {1}), ожидаемая точка: ({2}; {3})")
    @CsvSource({
        "1.0, 0.0, 0.753552, -0.438095",
        "0.0, 1.0, 0.753552, 0.438095",
        "1.0, 1.0, 1.496054, 6.28037e-16"
    })
    void testApply(double x, double y, double expectedX, double expectedY) {
        Point result = transformation.apply(new Point(x, y));

        assertEquals(expectedX, result.x(), 0.0001, "X-координата не совпадает");
        assertEquals(expectedY, result.y(), 0.0001, "Y-координата не совпадает");
    }
}
