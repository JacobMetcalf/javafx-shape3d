package uk.co.jacobmetcalf.javafx.shape3d;

import java.util.stream.Stream;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.shape.Box;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import uk.co.jacobmetcalf.javafx.shape3d.Stack3D.PlacementX;
import uk.co.jacobmetcalf.javafx.shape3d.Stack3D.PlacementY;
import uk.co.jacobmetcalf.javafx.shape3d.Stack3D.PlacementZ;

public class Stack3DTest {

  private static final double BOX_1_SIZE = 100;
  private static final double BOX_2_SIZE = 50;
  private static final double HALF_SUM_OF_SIZES = (BOX_1_SIZE + BOX_2_SIZE) / 2;
  private static final double HALF_DIFFERENCE_IN_SIZES = (BOX_1_SIZE - BOX_2_SIZE) / 2;

  @ParameterizedTest(name = "Can stack {6}")
  @MethodSource("placementOptions")
  public void can_stack(PlacementX placementX, PlacementY placementY, PlacementZ placementZ,
      double expectedCentreX, double expectedCentreY, double expectedCentreZ, String name) {

    Box box1 = new Box(BOX_1_SIZE, BOX_1_SIZE, BOX_1_SIZE);
    Box box2 = new Box(BOX_2_SIZE, BOX_2_SIZE, BOX_2_SIZE);

    Group actual = new Stack3D()
        .add(box1)
        .add(box2, placementX, placementY, placementZ)
        .toGroup();

    Bounds actualBounds = box2.getBoundsInParent();
    MatcherAssert.assertThat(actualBounds.getCenterX(), Matchers.equalTo(expectedCentreX));
    MatcherAssert.assertThat(actualBounds.getCenterY(), Matchers.equalTo(expectedCentreY));
    MatcherAssert.assertThat(actualBounds.getCenterZ(), Matchers.equalTo(expectedCentreZ));
  }

  public static Stream<Arguments> placementOptions() {
    return Stream.of(
        // Y alignment
        Arguments.arguments(PlacementX.ALIGN_CENTRE, PlacementY.ABOVE, PlacementZ.ALIGN_CENTRE,
            0, -HALF_SUM_OF_SIZES, 0, "above"),
        Arguments.arguments(PlacementX.ALIGN_CENTRE, PlacementY.BELOW, PlacementZ.ALIGN_CENTRE,
            0, HALF_SUM_OF_SIZES, 0, "below"),
        Arguments.arguments(PlacementX.ON_LEFT, PlacementY.ALIGN_TOP, PlacementZ.ALIGN_CENTRE,
            -HALF_SUM_OF_SIZES, -HALF_DIFFERENCE_IN_SIZES, 0, "on left with tops aligned"),
        Arguments.arguments(PlacementX.ON_LEFT, PlacementY.ALIGN_BOTTOM, PlacementZ.ALIGN_CENTRE,
            -HALF_SUM_OF_SIZES, HALF_DIFFERENCE_IN_SIZES, 0, "on left with bottoms aligned"),

        // X alignment
        Arguments.arguments(PlacementX.ON_LEFT, PlacementY.ALIGN_CENTRE, PlacementZ.ALIGN_CENTRE,
            -HALF_SUM_OF_SIZES, 0, 0, "on left"),
        Arguments.arguments(PlacementX.ON_RIGHT, PlacementY.ALIGN_CENTRE, PlacementZ.ALIGN_CENTRE,
            HALF_SUM_OF_SIZES, 0, 0, "on right"),
        Arguments.arguments(PlacementX.ALIGN_LEFT, PlacementY.ABOVE, PlacementZ.ALIGN_CENTRE,
            -HALF_DIFFERENCE_IN_SIZES, -HALF_SUM_OF_SIZES, 0, "above with left faces aligned"),
        Arguments.arguments(PlacementX.ALIGN_RIGHT, PlacementY.ABOVE, PlacementZ.ALIGN_CENTRE,
            HALF_DIFFERENCE_IN_SIZES, -HALF_SUM_OF_SIZES, 0, "above with right faces aligned"),

        // Z alignment
        Arguments.arguments(PlacementX.ALIGN_CENTRE, PlacementY.ALIGN_CENTRE, PlacementZ.IN_FRONT,
            0, 0, -HALF_SUM_OF_SIZES, "in front"),
        Arguments.arguments(PlacementX.ALIGN_CENTRE, PlacementY.ALIGN_CENTRE, PlacementZ.BEHIND,
            0, 0, HALF_SUM_OF_SIZES, "behind"),
        Arguments.arguments(PlacementX.ALIGN_CENTRE, PlacementY.ABOVE, PlacementZ.ALIGN_FRONT,
            0, -HALF_SUM_OF_SIZES, -HALF_DIFFERENCE_IN_SIZES, "above with front faces aligned"),
        Arguments.arguments(PlacementX.ALIGN_CENTRE, PlacementY.ABOVE, PlacementZ.ALIGN_BACK,
            0, -HALF_SUM_OF_SIZES, HALF_DIFFERENCE_IN_SIZES, "above with back faces aligned")
        );
  }
}