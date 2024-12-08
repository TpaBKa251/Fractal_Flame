package backend.academy.flame.config;

import backend.academy.flame.image.ImageFormat;
import backend.academy.flame.transformation.TransformationType;
import java.util.ArrayList;
import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
class ConfigurationMessage {

    static String getMessageForImageSize(int minSize, int maxSize) {
        return String.format("Введите ширину и высоту изображения в виде двух чисел между %d и %d через пробел:",
            minSize, maxSize);
    }

    static String getMessageForTransformations() {
        String baseMessage = "Введите номера трансформаций через пробел (можно повторяться):\n";
        int buffer = baseMessage.length();
        TransformationType[] transformationTypes = TransformationType.values();
        List<String> typesForChoice = new ArrayList<>(transformationTypes.length);

        for (TransformationType type : transformationTypes) {
            String line = "\t" + (type.ordinal() + 1) + ") " + type.transformationName()
                + (type.ordinal() == transformationTypes.length - 1
                ? ""
                : '\n');
            buffer += line.length();
            typesForChoice.add(line);
        }

        StringBuilder builder = new StringBuilder(buffer);
        builder.append(baseMessage);
        typesForChoice.forEach(builder::append);

        return builder.toString();
    }


    static String getMessageForPointCount(int minPointCount, int maxPointCount) {
        return String.format("Введите количество точек в виде числа между %d и %d:", minPointCount, maxPointCount);
    }

    static String getMessageForIterationsCount(short minIterCount, short maxIterCount) {
        return String.format("Введите количество итераций в виде числа между %d и %d:", minIterCount, maxIterCount);
    }

    static String getMessageForSymmetryCount(short minSymCount, short maxSymCount) {
        return String.format("Введите количество симметрий в виде числа между %d и %d:", minSymCount, maxSymCount);
    }

    static String getMessageForThreadCount(int minThreadCount, int maxThreadCount) {
        return
            String.format("Введите количество потоков в виде числа между %d и %d:", minThreadCount, maxThreadCount);
    }

    static String getMessageForFormat() {
        String baseMessage = "Введите номер формата изображения:\n";
        int buffer = baseMessage.length();
        ImageFormat[] formats = ImageFormat.values();
        List<String> formatsForChoice = new ArrayList<>(formats.length);

        for (ImageFormat format : formats) {
            String line = "\t" + (format.ordinal() + 1) + ") " + format.name()
                + (format.ordinal() == formats.length - 1
                ? ""
                : '\n');
            buffer += line.length();
            formatsForChoice.add(line);
        }

        StringBuilder builder = new StringBuilder(buffer);
        builder.append(baseMessage);
        formatsForChoice.forEach(builder::append);

        return builder.toString();
    }

    static String getMessageForFile() {
        return "Введите название выходного файла";
    }
}
