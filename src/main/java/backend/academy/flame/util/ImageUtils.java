package backend.academy.flame.util;

import backend.academy.flame.image.FractalImage;
import backend.academy.flame.image.ImageFormat;
import backend.academy.flame.image.Pixel;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.IntStream;
import javax.imageio.ImageIO;
import lombok.experimental.UtilityClass;

@UtilityClass
@SuppressFBWarnings("PATH_TRAVERSAL_IN")
public class ImageUtils {
    private final static int BITS_SHIFT_RED = 16;
    private final static int BITS_SHIFT_GREEN = 8;

    public static void save(FractalImage image, ImageFormat format, String fileName, int threadCount) {
        BufferedImage bufferedImage = getBufferedImage(image, threadCount);
        String filename = Path.of("").toAbsolutePath() + File.separator + fileName + "." + format.extension();

        try {
            File outputFile = new File(filename);
            if (!ImageIO.write(bufferedImage, format.extension(), outputFile)) {
                throw new IOException("Не удалось записать изображение в формате: " + format);
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Ошибка сохранения в файл: " + filename, e);
        }
    }

    private static BufferedImage getBufferedImage(FractalImage image, int threadCount) {
        BufferedImage bufferedImage = new BufferedImage(image.width(), image.height(), BufferedImage.TYPE_INT_RGB);

        int width = image.width();
        int height = image.height();
        int rangeSize = height / threadCount;

        if (threadCount > 1) {
            try (ForkJoinPool pool = new ForkJoinPool(threadCount)) {
                pool.submit(() ->
                    IntStream.range(0, threadCount).parallel().forEach(threadIndex -> {
                        int startY = threadIndex * rangeSize;
                        int endY = (threadIndex == threadCount - 1) ? height : startY + rangeSize;

                        for (int y = startY; y < endY; y++) {
                            setColorForPixels(image, bufferedImage, width, y);
                        }
                    })
                ).join();
            }
        } else {
            for (int y = 0; y < height; y++) {
                setColorForPixels(image, bufferedImage, width, y);
            }
        }


        return bufferedImage;
    }

    private static void setColorForPixels(FractalImage image, BufferedImage bufferedImage, int width, int y) {
        for (int x = 0; x < width; x++) {
            Pixel pixel = image.pixel(x, y);
            if (pixel != null) {
                int color =
                    (pixel.r() << BITS_SHIFT_RED) | (pixel.g() << BITS_SHIFT_GREEN) | pixel.b();
                bufferedImage.setRGB(x, y, color);
            }
        }
    }

}
