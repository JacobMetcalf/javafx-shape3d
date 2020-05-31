package uk.co.jacobmetcalf.javafx.shape3d.util;

public class DoubleToFloatCollector {

  private float[] result = new float[]{};

  public static DoubleToFloatCollector supplier() {
    return new DoubleToFloatCollector();
  }

  public static void accumulator(final DoubleToFloatCollector collector, double value) {
    float[] in = collector.result;
    float[] out = new float[in.length + 1];
    System.arraycopy(in, 0, out, 0, in.length);
    out[in.length] = (float)value;
    collector.result = out;
  }

  public static void combiner(final DoubleToFloatCollector collector1,
      final DoubleToFloatCollector collector2) {
    float[] in1 = collector1.result;
    float[] in2 = collector2.result;
    float[] out = new float[in1.length + in2.length];
    System.arraycopy(in1, 0, out, 0, in1.length);
    System.arraycopy(in2, 0, out, in1.length, in2.length);
    collector1.result = out;
  }

  public float[] toArray() {
    return result;
  }

  private DoubleToFloatCollector() {}
}
