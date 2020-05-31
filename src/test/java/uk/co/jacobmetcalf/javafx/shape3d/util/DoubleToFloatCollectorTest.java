package uk.co.jacobmetcalf.javafx.shape3d.util;

import java.util.stream.DoubleStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DoubleToFloatCollectorTest {

  @Test
  public void can_convert_to_float_array() {

    float[] actual = DoubleStream.of(0.5, 1.0, 1.5)
        .collect( DoubleToFloatCollector::supplier,
            DoubleToFloatCollector::accumulator,
            DoubleToFloatCollector::combiner)
        .toArray();

    Assertions.assertEquals(3, actual.length);
    Assertions.assertEquals(0.5f, actual[0]);
    Assertions.assertEquals(1.0f, actual[1]);
    Assertions.assertEquals(1.5f, actual[2]);
  }
}