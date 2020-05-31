package uk.co.jacobmetcalf.javafx.shape3d.util;

import java.util.stream.Stream;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class EllipticalToCartesianConverterTest {

  @ParameterizedTest(name = "{5}: theta: {0}, phi: {1}")
  @MethodSource("ellipticalCoordinates")
  public void sphere_convert_to_cartesian(double theta, double phi, double x, double y, double z, String name) {
    EllipticalToCartesianConverter unit = new EllipticalToCartesianConverter(100, 100);
    double[] actual = unit.toCartesian(theta, phi).toArray();
    MatcherAssert.assertThat("X differed", actual[0], Matchers.closeTo(x,0.0001));
    MatcherAssert.assertThat("Y differed", actual[1], Matchers.closeTo(y,0.0001));
    MatcherAssert.assertThat("Z differed", actual[2], Matchers.closeTo(z,0.0001));
  }

  public static Stream<Arguments> ellipticalCoordinates() {
    return Stream.of(
        Arguments.arguments(0d, 0d, 0, -100, 0, "Top pole"),
        Arguments.arguments(180d, 0d, 0, 100, 0, "Bottom pole"),
        Arguments.arguments(90d, 0d, 100, 0, 0, "Equator 0 deg"),
        Arguments.arguments(90d, 90d, 0, 0, 100, "Equator 90 deg"),
        Arguments.arguments(90d, 45d, 70.7107, 0, 70.7107, "Equator at forty fives")
    );
  }
}