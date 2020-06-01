package uk.co.jacobmetcalf.javafx.shape3d.util;

import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class SpheroidCrossSection {

  public static final int TOP_LEFT_TEX = 0;
  public static final int TOP_RIGHT_TEX = 1;
  public static final int BOTTOM_LEFT_TEX = 2;
  public static final int BOTTOM_RIGHT_TEX = 3;
  private final double theta;
  private final int divisions;
  private final int pointNumberOffset;
  private final SpheroidCrossSection previous;
  private final EllipticalToCartesianConverter converter;

  /**
   * @param theta             (polar) angle from the y axis
   * @param pointNumberOffset Where to start numbering the points from for javaFX
   * @param divisions         The number of segments the circle is divided into.
   * @param previous          The previous cross section, leave null for the first pole.
   */
  public SpheroidCrossSection(double theta, int pointNumberOffset, int divisions,
      @Nullable final SpheroidCrossSection previous,
      @NonNull final EllipticalToCartesianConverter converter) {

    assert theta >= 0 : "Theta must be positive";
    assert theta <= 180 : "Theta must be less than 180";

    this.theta = theta;
    this.pointNumberOffset = pointNumberOffset;
    this.divisions = divisions;
    this.previous = previous;
    this.converter = converter;
  }

  /**
   * @return All the points around the radius of this cross section. JavaFx takes a flat list so the
   * cartesian coordinates are laid out x1, y1, z1, x2, y2, z2...
   */
  public DoubleStream getPoints() {

    // Only a single point at the pole
    if (atPole()) {
      return converter.toCartesian(theta, 0);
    }
    // Else rotate all the way around the circle in divisions
    return DoubleStream.iterate(0, phi -> phi < 360,
        phi -> phi + 360d / divisions)
        .flatMap(phi -> converter.toCartesian(theta, phi));
  }

  /**
   * @return Point index for the given division
   */
  public int getIndex(int divisionNumber) {
    // Cross section at pole so only one point
    if (atPole()) {
      return pointNumberOffset;
    }

    // Modulo to make it easier for the last face to connect points N back to 0
    return divisionNumber % divisions + pointNumberOffset;
  }

  /**
   * @return All the triangular faces between this cross section and the previous
   */
  public IntStream getFaces() {

    // First cross section (typically at pole) has no previous so does not have to plot faces
    if (previous == null) {
      return IntStream.empty();
    }

    return IntStream.range(0, divisions)
        .flatMap(this::toTexCoord);
  }

  private boolean atPole() {
    return theta == 0 || theta == 180;
  }

  private IntStream toTexCoord(int division) {
    assert previous != null;
    int topLeft = previous.getIndex(division);
    int topRight = previous.getIndex(division + 1);
    int bottomLeft = getIndex(division);
    int bottomRight = getIndex(division + 1);

    // If connecting to pole at bottom then we do not need this triangle
    IntStream bottomRightTriangle =
        (atPole())
            ? IntStream.empty()
            : IntStream.of( topLeft, TOP_LEFT_TEX,
                bottomLeft, BOTTOM_LEFT_TEX,
                bottomRight, BOTTOM_RIGHT_TEX);

    // If connecting to pole at top then we do not need this triangle
    IntStream topRightTriangle =
        (previous.atPole())
            ? IntStream.empty()
            : IntStream.of( topRight, TOP_RIGHT_TEX,
                topLeft, TOP_LEFT_TEX,
                bottomRight, BOTTOM_RIGHT_TEX);

    return IntStream.concat(bottomRightTriangle,topRightTriangle);
  }
}
