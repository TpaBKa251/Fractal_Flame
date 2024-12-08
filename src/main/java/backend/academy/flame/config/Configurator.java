package backend.academy.flame.config;

import backend.academy.flame.image.FractalImage;
import backend.academy.flame.image.ImageFormat;
import backend.academy.flame.image.Rect;
import backend.academy.flame.transformation.Transformation;
import backend.academy.flame.transformation.TransformationType;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Configurator {

    private static final String SPACES = "\\s++";

    private static final double RECTANGLE_X_Y = -2.0;
    private static final double RECTANGLE_WIDTH_HEIGHT = 4.0;

    private static final int MAX_IMAGE_SIZE = 10000;
    private static final int MIN_IMAGE_SIZE = 144;

    private static final double MIN_POINT_COUNT_FACTOR = 0.01;
    private static final int MAX_POINT_COUNT_FACTOR = 20;

    private static final short MIN_ITERATION_COUNT = 1;
    private static final short MAX_ITERATION_COUNT = 10;

    private static final short MIN_SYMMETRY_COUNT = 1;
    private static final short MAX_SYMMETRY_COUNT = 10;

    private static final short MIN_THREAD_COUNT = 1;
    private static final int MAX_THREAD_COUNT = Runtime.getRuntime().availableProcessors();

    private final Scanner scanner;
    private final PrintWriter writer;
    private Configuration.ConfigurationBuilder configurationBuilder;

    private int currentImageHeight = 0;
    private int currentImageWidth = 0;

    public Configuration getConfiguration() {
        configurationBuilder = new Configuration.ConfigurationBuilder();

        setCanvas();
        setWorld();
        setPointCount();
        setIterationCount();
        setSymmetryCount();
        setTransformations();
        setFileName();
        setFormat();
        setThreadCount();

        return configurationBuilder.build();
    }

    private void setCanvas() {
        String[] imageSizesArray;
        int width = 0;
        int height = 1;
        int[] imageSizes = new int[2];

        while (true) {
            writer.println(ConfigurationMessage.getMessageForImageSize(MIN_IMAGE_SIZE, MAX_IMAGE_SIZE));
            imageSizesArray = scanner.nextLine().trim().split(SPACES);

            try {
                imageSizes[width] = Integer.parseInt(imageSizesArray[width]);
                imageSizes[height] = Integer.parseInt(imageSizesArray[height]);

                if (imageSizes[width] < MIN_IMAGE_SIZE
                    || imageSizes[height] < MIN_IMAGE_SIZE
                    || imageSizes[width] > MAX_IMAGE_SIZE
                    || imageSizes[height] > MAX_IMAGE_SIZE
                ) {
                    continue;
                }
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                continue;
            }

            currentImageWidth = imageSizes[width];
            currentImageHeight = imageSizes[height];

            break;
        }

        configurationBuilder.canvas(FractalImage.create(currentImageWidth, currentImageHeight));
    }

    private void setWorld() {
        configurationBuilder.world(new Rect(
            RECTANGLE_X_Y,
            RECTANGLE_X_Y,
            RECTANGLE_WIDTH_HEIGHT,
            RECTANGLE_WIDTH_HEIGHT
        ));
    }

    private void setTransformations() {
        writer.println(ConfigurationMessage.getMessageForTransformations());

        String[] transformationNumbers = scanner.nextLine().trim().split(SPACES);

        List<Transformation> transformations = new ArrayList<>(transformationNumbers.length);

        for (String transformationNumber : transformationNumbers) {
            transformations.add(TransformationType.getTransformationByNumber(transformationNumber));
        }

        configurationBuilder.transformations(transformations);
    }

    private void setPointCount() {
        int pointCount;

        while (true) {
            int maxPointCount = currentImageWidth * currentImageHeight * MAX_POINT_COUNT_FACTOR;
            int minPointCount = (int) (maxPointCount * MIN_POINT_COUNT_FACTOR);

            writer.println(ConfigurationMessage.getMessageForPointCount(minPointCount, maxPointCount));

            try {
                String line = scanner.nextLine().trim();
                pointCount = Integer.parseInt(line);

                if (pointCount < minPointCount || pointCount > maxPointCount) {
                    continue;
                }
            } catch (NumberFormatException e) {
                continue;
            }

            break;
        }

        configurationBuilder.pointCount(pointCount);
    }

    private void setIterationCount() {
        short iterationCount;

        while (true) {
            writer.println(ConfigurationMessage.getMessageForIterationsCount(MIN_ITERATION_COUNT, MAX_ITERATION_COUNT));

            try {
                String line = scanner.nextLine().trim();
                iterationCount = Short.parseShort(line);

                if (iterationCount < MIN_ITERATION_COUNT || iterationCount > MAX_ITERATION_COUNT) {
                    continue;
                }
            } catch (NumberFormatException e) {
                continue;
            }

            break;
        }

        configurationBuilder.iterPerPointCount(iterationCount);
    }

    private void setSymmetryCount() {
        short symmetryCount;

        while (true) {
            writer.println(ConfigurationMessage.getMessageForSymmetryCount(MIN_SYMMETRY_COUNT, MAX_SYMMETRY_COUNT));

            try {
                String line = scanner.nextLine().trim();
                symmetryCount = Short.parseShort(line);

                if (symmetryCount < MIN_SYMMETRY_COUNT || symmetryCount > MAX_SYMMETRY_COUNT) {
                    continue;
                }
            } catch (NumberFormatException e) {
                continue;
            }

            break;
        }

        configurationBuilder.symmetryCount(symmetryCount);
    }

    private void setThreadCount() {
        int threadCount;

        while (true) {
            writer.println(ConfigurationMessage.getMessageForThreadCount(MIN_THREAD_COUNT, MAX_THREAD_COUNT));

            try {
                String line = scanner.nextLine().trim();
                threadCount = Integer.parseInt(line);

                if (threadCount < MIN_THREAD_COUNT || threadCount > MAX_THREAD_COUNT) {
                    continue;
                }
            } catch (NumberFormatException e) {
                continue;
            }

            break;
        }

        configurationBuilder.threadCount(threadCount);
    }

    private void setFormat() {
        writer.println(ConfigurationMessage.getMessageForFormat());

        String format = scanner.nextLine().trim();

        configurationBuilder.format(ImageFormat.getImageFormat(format));
    }

    private void setFileName() {
        writer.println(ConfigurationMessage.getMessageForFile());

        String fileName = scanner.nextLine().trim();

        configurationBuilder.fileName(fileName);
    }
}
