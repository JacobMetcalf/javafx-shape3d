package uk.co.jacobmetcalf.javafx.shape3d.util;

import java.util.stream.Stream;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import uk.co.jacobmetcalf.javafx.shape3d.CuboidMeshBuilder.Cuboid.FillFaces;

public class CuboidFaceTest {

  @ParameterizedTest(name = "{0}")
  @MethodSource("facesAndPoints")
  public void get_faces(final FillFaces fillFaces, final Integer[] expected) {
    CuboidFace unit = new CuboidFace(1,2,3,4,
        1,2,3,4, fillFaces);

    Integer[] actual = unit.toMeshFaces().boxed().toArray(Integer[]::new);
    MatcherAssert.assertThat(actual, Matchers.arrayContaining(expected));
  }

  @Test
  public void get_smoothing_group() {
    CuboidFace unit = new CuboidFace(1,2,3,4,
        1,2,3,4, FillFaces.BOTH);

    Integer[] actual = unit.toMeshSmoothingGroups().boxed().toArray(Integer[]::new);
    MatcherAssert.assertThat(actual, Matchers.arrayContaining(0, 0, 0, 0));
  }

  // Make sure for exterior faces the points are listed anti clockwise
  public static Stream<Arguments> facesAndPoints() {
    return Stream.of(
        Arguments.arguments(FillFaces.EXTERIOR,
            new Integer[] {2, 2, 1, 1, 3, 3, 2, 2, 3, 3, 4, 4}),
        Arguments.arguments(FillFaces.INTERIOR,
            new Integer[] {2, 2, 3, 3, 1, 1, 2, 2, 4, 4, 3, 3}),
        Arguments.arguments(FillFaces.BOTH,
            new Integer[] {2,2,3,3,1,1,2,2,4,4,3,3,2,2,1,1,3,3,2,2,3,3,4,4})
    );
  }
}