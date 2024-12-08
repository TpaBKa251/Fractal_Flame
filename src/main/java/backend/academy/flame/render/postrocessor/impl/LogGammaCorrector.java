package backend.academy.flame.render.postrocessor.impl;

import backend.academy.flame.image.FractalImage;
import backend.academy.flame.image.Pixel;
import backend.academy.flame.render.postrocessor.ImagePostProcessor;

import java.util.concurrent.ForkJoinPool;
import java.util.stream.IntStream;

public class LogGammaCorrector implements ImagePostProcessor {
    private final static double GAMMA = 2.2;

    @Override
    public void postProcess(FractalImage image, int threadCount) {
        int width = image.width();
        int height = image.height();

        double maxLog = findMaxLog(image, width, height, threadCount);
        applyLogGammaCorrection(image, width, height, maxLog, threadCount);
    }

    private double findMaxLog(
        FractalImage image,
        int width,
        int height,
        int threadCount
    ) {
        int rangeSize = width / threadCount;

        try (ForkJoinPool pool = new ForkJoinPool(threadCount)) {
            return pool.submit(() ->
                IntStream.range(0, threadCount).parallel()
                    .mapToDouble(threadIndex -> {
                        int start = threadIndex * rangeSize;
                        int end = (threadIndex == threadCount - 1) ? width : start + rangeSize;

                        double localMaxLog = 0.0;
                        for (int x = start; x < end; x++) {
                            for (int y = 0; y < height; y++) {
                                Pixel pixel = image.pixel(x, y);
                                if (pixel != null && pixel.hitCount() > 0) {
                                    double logValue = Math.log10(1 + pixel.hitCount());
                                    image.setPixel(x, y, new Pixel(
                                        pixel.r(), pixel.g(), pixel.b(), logValue, pixel.hitCount()));
                                    localMaxLog = Math.max(localMaxLog, logValue);
                                }
                            }
                        }
                        return localMaxLog;
                    })
                    .max().orElse(0.0)
            ).join();
        }
    }

    private void applyLogGammaCorrection(
        FractalImage image,
        int width,
        int height,
        double maxLog,
        int threadCount
    ) {
        int rangeSize = width / threadCount;

        try (ForkJoinPool pool = new ForkJoinPool(threadCount)) {
            pool.submit(() ->
                IntStream.range(0, threadCount).parallel().forEach(threadIndex -> {
                    int start = threadIndex * rangeSize;
                    int end = (threadIndex == threadCount - 1) ? width : start + rangeSize;

                    for (int x = start; x < end; x++) {
                        for (int y = 0; y < height; y++) {
                            Pixel pixel = image.pixel(x, y);
                            if (pixel == null) {
                                continue;
                            }

                            Pixel newPixel = getPixel(maxLog, pixel);

                            image.setPixel(x, y, newPixel);
                        }
                    }
                })
            ).join();
        }
    }

    private Pixel getPixel(double maxLog, Pixel pixel) {
        double normalized = pixel.normal() / maxLog;
        double gammaCorrected = Math.pow(normalized, 1.0 / GAMMA);

        int r = (int) (pixel.r() * gammaCorrected);
        int g = (int) (pixel.g() * gammaCorrected);
        int b = (int) (pixel.b() * gammaCorrected);

        return (pixel.hitCount() > 0)
            ? new Pixel(r, g, b, gammaCorrected, pixel.hitCount())
            : new Pixel(0, 0, 0, 0, 0);
    }

}
