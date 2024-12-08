package backend.academy.flame;

import backend.academy.flame.config.Configuration;
import backend.academy.flame.config.Configurator;
import backend.academy.flame.image.FractalImage;
import backend.academy.flame.render.FractalRenderer;
import backend.academy.flame.render.postrocessor.impl.LogGammaCorrector;
import backend.academy.flame.util.ImageUtils;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Scanner;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);
             PrintWriter out =
                 new PrintWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8), true)
        ) {
            Configuration configuration = new Configurator(scanner, out).getConfiguration();
            log.info("Начинаем генерацию");
            Instant start = Instant.now();
            FractalImage fractalImage = new FractalRenderer().render(configuration);
            log.info("Корректируем");
            new LogGammaCorrector().postProcess(fractalImage, configuration.threadCount());
            log.info("Сохраняем");
            ImageUtils.save(
                fractalImage,
                configuration.format(),
                configuration.fileName(),
                configuration.threadCount()
            );
            Instant end = Instant.now();
            log.info("Время выполнения программы: {}мс", Duration.between(start, end).toMillis());
        } catch (Exception e) {
            log.error("Произошла ошибка: {}", e.getMessage());
        }
    }
}
