package backend.academy.flame.transformation;

import backend.academy.flame.transformation.impl.ExTransformation;
import backend.academy.flame.transformation.impl.ExponentialTransformation;
import backend.academy.flame.transformation.impl.HandkerchiefTransformation;
import backend.academy.flame.transformation.impl.LinearTransformation;
import backend.academy.flame.transformation.impl.SpiralTransformation;
import backend.academy.flame.transformation.impl.SwirlTransformation;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TransformationType {
    EXPO("Экспоненциальная", new ExponentialTransformation()),
    EX("X", new ExTransformation()),
    HANDKERCHIEF("Носовой платок (носовоплаточная)", new HandkerchiefTransformation()),
    LINEAR("Линейная", new LinearTransformation()),
    SPIRAL("Спиральная", new SpiralTransformation()),
    SWIRL("Вихревая", new SwirlTransformation());

    @Getter
    private final String transformationName;
    private final Transformation transformation;

    private static final Map<String, Transformation> TRANSFORMATIONS =
        new HashMap<>(TransformationType.values().length);

    static {
        for (TransformationType t : TransformationType.values()) {
            TRANSFORMATIONS.put(String.valueOf(t.ordinal() + 1), t.transformation);
        }
    }

    public static Transformation getTransformationByNumber(String transformationNumber) {
        return TRANSFORMATIONS.getOrDefault(transformationNumber, LINEAR.transformation);
    }
}
