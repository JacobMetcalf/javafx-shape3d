package uk.co.jacobmetcalf.javafx.shape3d.util;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

public class NumberedTexCoordSetTest {

  public NumberedTexCoordSet unit = new NumberedTexCoordSet();

  @Test
  public void converts_to_scaled_double_stream() {
    MatcherAssert.assertThat(unit.addTexCoord(10, 20), Matchers.equalTo(0));
    MatcherAssert.assertThat(unit.addTexCoord(20, 30), Matchers.equalTo(1));

    Double[] result = unit.getXyStream().boxed().toArray(Double[]::new);
    MatcherAssert.assertThat(result, Matchers.arrayContaining(0d, 0d, 1d, 1d));
  }

  @Test public void spots_duplicate() {
    MatcherAssert.assertThat(unit.addTexCoord(10, 20), Matchers.equalTo(0));
    MatcherAssert.assertThat(unit.addTexCoord(20, 30 ), Matchers.equalTo(1));
    MatcherAssert.assertThat(unit.addTexCoord(10, 20 ), Matchers.equalTo(0));

    Double[] result = unit.getXyStream().boxed().toArray(Double[]::new);
    MatcherAssert.assertThat(result, Matchers.arrayContaining(0d, 0d, 1d, 1d));
  }

  @Test
  public void scales_over_origin_correctly() {
    MatcherAssert.assertThat(unit.addTexCoord(-10, -10), Matchers.equalTo(0));
    MatcherAssert.assertThat(unit.addTexCoord(10, 10), Matchers.equalTo(1));
    MatcherAssert.assertThat(unit.addTexCoord(0, 0), Matchers.equalTo(2));
    MatcherAssert.assertThat(unit.addTexCoord(0, 5), Matchers.equalTo(3));
    MatcherAssert.assertThat(unit.addTexCoord(-5, 0), Matchers.equalTo(4));

    Double[] result = unit.getXyStream().boxed().toArray(Double[]::new);
    MatcherAssert.assertThat(result,
        Matchers.arrayContaining(0d, 0d, 1d, 1d, 0.5d, 0.5d, 0.5d, 0.75d, 0.25d, 0.5d));
  }
}