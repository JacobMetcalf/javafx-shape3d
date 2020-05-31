package uk.co.jacobmetcalf.javafx.shape3d.util;

import co.unruly.matchers.StreamMatchers;
import java.util.stream.IntStream;
import javafx.scene.shape.TriangleMesh;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class TriangleMeshHelperTest {

  private static final int[] EXAMPLE = {1, 10, 2, 20, 3, 30};

  @Test
  public void can_reverse() {
    IntStream input = IntStream.of(EXAMPLE);
    MatcherAssert.assertThat( TriangleMeshHelper.addReverseFace(input),
        StreamMatchers.startsWithInt(1, 10, 2, 20, 3, 30,
            3, 30, 2, 20, 1, 10));
  }

  @Test
  public void divides_int_two_smoothing_groups() {
    TriangleMesh mesh = new TriangleMesh();
    mesh.getFaces().addAll(TriangleMeshHelper
        .addReverseFace(IntStream.of(EXAMPLE)).toArray());
    TriangleMeshHelper.setSmoothingGroups(mesh, true);

    // Check two faces are found and they are not in the same smoothing group.
    MatcherAssert.assertThat(mesh.getFaceSmoothingGroups().size(), Matchers.equalTo(2));
    MatcherAssert.assertThat(mesh.getFaceSmoothingGroups().get(0),
        Matchers.not(Matchers.equalTo(mesh.getFaceSmoothingGroups().get(1))));
  }
}