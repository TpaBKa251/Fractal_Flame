package backend.academy.flame.render;

import backend.academy.flame.config.Configuration;
import backend.academy.flame.image.FractalImage;
import backend.academy.flame.image.Pixel;
import backend.academy.flame.image.Point;
import backend.academy.flame.image.Rect;
import backend.academy.flame.transformation.Transformation;
import backend.academy.flame.transformation.impl.AffineTransformation;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class FractalRenderer {
    private final static int ADDITIONALLY_STEPS = -20;
    private final static int COLOUR_RANDOM = 256;
    private ThreadLocalRandom random;
    private Object[][] locks;

    @SuppressFBWarnings("PREDICTABLE_RANDOM")
    public FractalImage render(Configuration configuration) {
        FractalImage canvas = configuration.canvas();
        Rect world = configuration.world();
        List<Transformation> variations = configuration.transformations();
        int samples = configuration.pointCount();
        short iterPerSample = configuration.iterPerPointCount();
        int symmetry = configuration.symmetryCount();
        int threadCount = configuration.threadCount();
        random = ThreadLocalRandom.current();

        createLocks(canvas.width(), canvas.height());
        List<Point> rotations = precomputeRotations(symmetry);
        List<AffineTransformation> affineTransformations = initAffineTransforms(iterPerSample);
        int batchSize = samples / threadCount;

        try (ForkJoinPool pool = new ForkJoinPool(threadCount)) {
            pool.submit(() -> IntStream.range(0, threadCount).parallel().forEach(threadIndex -> {
                int start = threadIndex * batchSize;
                int end = (threadIndex == threadCount - 1) ? samples : start + batchSize;

                for (int i = start; i < end; i++) {
                    processSample(canvas, world, variations, affineTransformations, iterPerSample, symmetry, rotations);
                }
            }));
            pool.shutdown();
            pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return canvas;
    }

    private void processSample(
        FractalImage canvas, Rect world,
        List<Transformation> variations,
        List<AffineTransformation> affineTransformations,
        short iterPerSample,
        int symmetry,
        List<Point> rotations
    ) {
        Point pw = randomPoint(world);

        for (short step = ADDITIONALLY_STEPS; step < iterPerSample; ++step) {
            AffineTransformation affineTransformation =
                    affineTransformations.get(random.nextInt(affineTransformations.size()));

            pw = affineTransformation.apply(pw);
            if (!world.contains(pw)) {
                continue;
            }

            Transformation transformation = variations.get(random.nextInt(variations.size()));
            pw = transformation.apply(pw);
            if (!world.contains(pw)) {
                continue;
            }

            for (int s = 0; s < symmetry; ++s) {
                Point pwr = rotate(pw, rotations, s);

                int[] range = mapRange(world, pwr, canvas);
                if (range == null) {
                    continue;
                }

                int x = 0;
                int y = 1;
                updatePixel(canvas, range[x], range[y], affineTransformation);
            }
        }
    }

    private void updatePixel(FractalImage canvas, int x, int y, AffineTransformation affineTransformation) {
        synchronized (locks[x][y]) {
            Pixel pixel = canvas.pixel(x, y);
            if (pixel == null) {
                return;
            }

            Pixel newPixel;
            if (pixel.hitCount() == 0) {
                newPixel = new Pixel(
                        affineTransformation.r(),
                        affineTransformation.g(),
                        affineTransformation.b(),
                        1.0,
                        1);
            } else {
                newPixel = new Pixel(
                        (pixel.r() + affineTransformation.r()) / 2,
                        (pixel.g() + affineTransformation.g()) / 2,
                        (pixel.b() + affineTransformation.b()) / 2,
                        1.0,
                        pixel.hitCount() + 1);
            }
            canvas.setPixel(x, y, newPixel);
        }
    }

    private int[] mapRange(Rect world, Point p, FractalImage canvas) {
        if (!world.contains(p)) {
            return null;
        }
        int x = (int) ((p.x() - world.x()) / world.width() * canvas.width());
        int y = (int) ((p.y() - world.y()) / world.height() * canvas.height());
        return new int[]{x, y};
    }

    private Point randomPoint(Rect world) {
        double x = random.nextDouble(world.x(), world.width());
        double y = random.nextDouble(world.y(), world.height());
        return new Point(x, y);
    }

    private List<Point> precomputeRotations(int symmetry) {
        List<Point> rotations = new ArrayList<>(symmetry);
        for (int s = 0; s < symmetry; s++) {
            double theta = s * (2 * Math.PI / symmetry);
            rotations.add(new Point(Math.cos(theta), Math.sin(theta)));
        }
        return rotations;
    }

    private Point rotate(Point pw, List<Point> rotations, int index) {
        Point rotation = rotations.get(index);
        double x = pw.x() * rotation.x() - pw.y() * rotation.y();
        double y = pw.x() * rotation.y() + pw.y() * rotation.x();
        return new Point(x, y);
    }

    private List<AffineTransformation> initAffineTransforms(int iterPerSample) {
        int red = 0;
        int green = 1;
        int blue = 2;
        List<AffineTransformation> transforms = new ArrayList<>(iterPerSample);

        for (int iter = 0; iter < iterPerSample; ++iter) {
            int[] rgb = initRandomRgb();
            transforms.add(new AffineTransformation(
                    2 * random.nextDouble() - 1,
                    2 * random.nextDouble() - 1,
                    2 * random.nextDouble() - 1,
                    2 * random.nextDouble() - 1,
                    2 * random.nextDouble() - 1,
                    2 * random.nextDouble() - 1,
                    rgb[red],
                    rgb[green],
                    rgb[blue]
            ));
        }

        return transforms;
    }

    private int[] initRandomRgb() {
        return new int[]{random.nextInt(COLOUR_RANDOM), random.nextInt(COLOUR_RANDOM), random.nextInt(COLOUR_RANDOM)};
    }

    private void createLocks(int width, int height) {
        locks = new Object[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                locks[x][y] = new Object();
            }
        }
    }
}
