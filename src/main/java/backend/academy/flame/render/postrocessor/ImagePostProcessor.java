package backend.academy.flame.render.postrocessor;

import backend.academy.flame.image.FractalImage;

@FunctionalInterface
public interface ImagePostProcessor {

    void postProcess(FractalImage image, int threadCount);
}
