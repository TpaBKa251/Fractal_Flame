package backend.academy.flame.image;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ImageFormat {
    JPEG("jpeg"),
    BMP("bmp"),
    PNG("png");

    private final String extension;

    private static final Map<String, ImageFormat> FILE_NAME_MAP = new HashMap<>(ImageFormat.values().length);

    static {
        for (ImageFormat imageFormat : ImageFormat.values()) {
            FILE_NAME_MAP.put(String.valueOf(imageFormat.ordinal() + 1), imageFormat);
        }
    }

    public static ImageFormat getImageFormat(String number) {
        return FILE_NAME_MAP.getOrDefault(number, ImageFormat.PNG);
    }
}
