package uk.co.jacobmetcalf.javafx.shape3d.util;

import javafx.geometry.Point3D;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class NumberedPointSetTest {

  public NumberedPointSet unit = new NumberedPointSet();

  @BeforeEach
  public void before() {
    unit.addPoint(1, 2, 3);
    unit.addPoint(4, 5, 6);
  }

  @Test
  public void converts_to_double_stream() {

    Double[] result = unit.getXyzStream().boxed().toArray(Double[]::new);
    MatcherAssert.assertThat(result, Matchers.arrayContaining(1d, 2d, 3d, 4d, 5d, 6d));
  }

  @Test
  public void can_get_by_index() {
    Point3D result = unit.get(1);
    MatcherAssert.assertThat(result.getX(), Matchers.equalTo(4d));
    MatcherAssert.assertThat(result.getY(), Matchers.equalTo(5d));
    MatcherAssert.assertThat(result.getZ(), Matchers.equalTo(6d));
  }

  @Test public void spots_duplicate() {
    MatcherAssert.assertThat(unit.addPoint(1, 2, 3 ), Matchers.equalTo(0));

    Double[] result = unit.getXyzStream().boxed().toArray(Double[]::new);
    MatcherAssert.assertThat(result, Matchers.arrayContaining(1d, 2d, 3d, 4d, 5d, 6d));
  }
}