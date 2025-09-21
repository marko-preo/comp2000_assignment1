import java.util.Random;

public class PerlinNoise implements NoiseGenerator {
  private final int[] permutation;

  public PerlinNoise(long seed) {
    permutation = new int[512];
    int[] p = new int[256];
    for (int i = 0; i < 256; i++)
      p[i] = i;

    Random rand = new Random(seed);
    for (int i = 0; i < 256; i++) {
      int j = rand.nextInt(256);
      int tmp = p[i];
      p[i] = p[j];
      p[j] = tmp;
    }

    for (int i = 0; i < 512; i++) {
      permutation[i] = p[i & 255];
    }
  }

  @Override
  public double get(double x, double y) {
    x /= 20.0;
    y /= 20.0;

    int xi = (int) Math.floor(x) & 255;
    int yi = (int) Math.floor(y) & 255;
    double xf = x - Math.floor(x);
    double yf = y - Math.floor(y);

    double u = fade(xf);
    double v = fade(yf);

    int aa = permutation[permutation[xi] + yi];
    int ab = permutation[permutation[xi] + yi + 1];
    int ba = permutation[permutation[xi + 1] + yi];
    int bb = permutation[permutation[xi + 1] + yi + 1];

    double x1, x2;
    x1 = lerp(grad(aa, xf, yf), grad(ba, xf - 1, yf), u);
    x2 = lerp(grad(ab, xf, yf - 1), grad(bb, xf - 1, yf - 1), u);

    return (lerp(x1, x2, v) + 1) / 2;
  }

  private double fade(double t) {
    return t * t * t * (t * (t * 6 - 15) + 10);
  }

  private double lerp(double a, double b, double t) {
    return a + t * (b - a);
  }

  private double grad(int hash, double x, double y) {
    int h = hash & 7;
    double u = (h < 4) ? x : y;
    double v = (h < 4) ? y : x;
    return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
  }
}

