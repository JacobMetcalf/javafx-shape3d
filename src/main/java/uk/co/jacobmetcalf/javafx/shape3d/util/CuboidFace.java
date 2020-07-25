package uk.co.jacobmetcalf.javafx.shape3d.util;

import java.util.Objects;
import java.util.stream.IntStream;
import uk.co.jacobmetcalf.javafx.shape3d.CuboidMeshBuilder.Cuboid.FillFaces;

/**
 * Encapsulates the face of a cuboid and describing it in a Triangular Mesh array.
 */
public class CuboidFace {

  private final int topLeft;
  private final int topLeftTex;
  private final int topRight;
  private final int topRightTex;
  private final int bottomLeft;
  private final int bottomLeftTex;
  private final int bottomRight;
  private final int bottomRightTex;
  private FillFaces fillFaces;

  public CuboidFace(int topLeft, int topRight, int bottomLeft, int bottomRight,
      int topLeftTex, int topRightTex, int bottomLeftTex, int bottomRightTex,
      final FillFaces fillFaces) {
    this.topLeft = topLeft;
    this.topLeftTex = topLeftTex;
    this.topRight = topRight;
    this.topRightTex = topRightTex;
    this.bottomLeft = bottomLeft;
    this.bottomLeftTex = bottomLeftTex;
    this.bottomRight = bottomRight;
    this.bottomRightTex = bottomRightTex;
    this.fillFaces = fillFaces;
  }

  public IntStream toMeshFaces() {
    return IntStream.concat(
        !FillFaces.EXTERIOR.equals(fillFaces)
          ? IntStream.of(
          this.topRight, this.topRightTex,
          this.bottomLeft, this.bottomLeftTex,
          this.topLeft, this.topLeftTex,
          this.topRight, this.topRightTex,
          this.bottomRight, this.bottomRightTex,
          this.bottomLeft, this.bottomLeftTex)
          : IntStream.empty(),
        !FillFaces.INTERIOR.equals(fillFaces)
          ? IntStream.of(
          this.topRight, this.topRightTex,
          this.topLeft, this.topLeftTex,
          this.bottomLeft, this.bottomLeftTex,
          this.topRight, this.topRightTex,
          this.bottomLeft, this.bottomLeftTex,
          this.bottomRight, this.bottomRightTex)
          : IntStream.empty());
  }

  /**
   * Set all edges as hard.
   */
  public IntStream toMeshSmoothingGroups () {
    return FillFaces.BOTH.equals(fillFaces) ? IntStream.of(0, 0, 0, 0) : IntStream.of(0, 0);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CuboidFace that = (CuboidFace) o;
    return topLeft == that.topLeft &&
        topLeftTex == that.topLeftTex &&
        topRight == that.topRight &&
        topRightTex == that.topRightTex &&
        bottomLeft == that.bottomLeft &&
        bottomLeftTex == that.bottomLeftTex &&
        bottomRight == that.bottomRight &&
        bottomRightTex == that.bottomRightTex &&
        fillFaces == that.fillFaces;
  }

  @Override
  public int hashCode() {
    return Objects
        .hash(topLeft, topLeftTex, topRight, topRightTex, bottomLeft,
            bottomLeftTex, bottomRight, bottomRightTex, fillFaces);
  }

  @Override
  public String toString() {
    return "CuboidFace{" +
        "topLeft=" + topLeft +
        ", topLeftTex=" + topLeftTex +
        ", topRight=" + topRight +
        ", topRightTex=" + topRightTex +
        ", bottomLeft=" + bottomLeft +
        ", bottomLeftTex=" + bottomLeftTex +
        ", bottomRight=" + bottomRight +
        ", bottomRightTex=" + bottomRightTex +
        ", fillFaces=" + fillFaces +
        '}';
  }
}
