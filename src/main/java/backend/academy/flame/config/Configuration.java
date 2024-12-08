package backend.academy.flame.config;

import backend.academy.flame.image.FractalImage;
import backend.academy.flame.image.ImageFormat;
import backend.academy.flame.image.Rect;
import backend.academy.flame.transformation.Transformation;
import java.util.List;
import lombok.Builder;

@Builder
@SuppressWarnings("RecordComponentNumber")
public record Configuration(
    FractalImage canvas,
    Rect world,
    List<Transformation> transformations,
    int pointCount,
    short iterPerPointCount,
    short symmetryCount,
    int threadCount,
    ImageFormat format,
    String fileName
) {
}
