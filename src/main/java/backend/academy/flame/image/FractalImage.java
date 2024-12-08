package backend.academy.flame.image;

public record FractalImage(Pixel[] data, int width, int height
) {

    public static FractalImage create(int width, int height) {
        Pixel[] data = new Pixel[width * height];

        for (int i = 0; i < data.length; i++) {
            data[i] = new Pixel(0, 0, 0, 1, 0);
        }

        return new FractalImage(data, width, height);
    }

    public boolean contains(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public Pixel pixel(int x, int y) {
        return contains(x, y) ? data[x + y * width] : null;
    }

    public void setPixel(int x, int y, Pixel pixel) {
        if (contains(x, y)) {
            data[x + y * width] = pixel;
        }
    }
}
