package uk.co.jacobmetcalf.javafx.shape3d.util;

import java.util.stream.IntStream;
import javafx.geometry.Point3D;
import uk.co.jacobmetcalf.javafx.shape3d.util.CuboidMeshBuilder.FillFaces;

/**
 * Encapsulates the face of a cuboid, managing the translation to TexCoords
 * and the creation of a Triangular Mesh array.
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

  public enum Plane {XY, XZ, YZ}

  public CuboidFace(int topLeft, int topRight, int bottomLeft, int bottomRight,
      final FillFaces fillFaces, final Plane plane,
      final NumberedPointSet pointSet, final NumberedTexCoordSet texCoordSet) {
    this.topLeft = topLeft;
    this.topLeftTex = addTexCoord(topLeft, plane, pointSet, texCoordSet);
    this.topRight = topRight;
    this.topRightTex = addTexCoord(topRight, plane, pointSet, texCoordSet);
    this.bottomLeft = bottomLeft;
    this.bottomLeftTex = addTexCoord(bottomLeft, plane, pointSet, texCoordSet);
    this.bottomRight = bottomRight;
    this.bottomRightTex = addTexCoord(bottomRight, plane, pointSet, texCoordSet);
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

  /**
   * Transpose the position in the plane to a coordinate on the XY plane
   */
  private int addTexCoord(int pointIndex, final Plane plane,
      final NumberedPointSet pointSet, final NumberedTexCoordSet texCoordSet) {

    Point3D point = pointSet.get(pointIndex);
    return switch (plane) {
      case XY -> texCoordSet.addTexCoord(point.getX(), point.getY());
      case XZ -> texCoordSet.addTexCoord(point.getX(), point.getZ());
      case YZ -> texCoordSet.addTexCoord(point.getY(), point.getZ());
    };
  }
}
