package uk.co.jacobmetcalf.javafx.shape3d.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import javafx.geometry.Point3D;
import uk.co.jacobmetcalf.javafx.shape3d.CuboidMeshBuilder;

/**
 * Encapsulates creating a cuboid using a shared set of points and
 * texture coordinates.
 * Construct it with an origin and width, height and depth. Then add
 * the required faces to it.
 */
public class CuboidImpl implements CuboidMeshBuilder.Cuboid {

  public enum Plane {XY, XZ, YZ}

  private final NumberedPointSet points;
  private final NumberedTexCoordSet texCoords;
  private final List<CuboidFace> faces;

  private final int x1y1z1;
  private final int x2y1z1;
  private final int x1y2z1;
  private final int x2y2z1;
  private final int x1y1z2;
  private final int x2y1z2;
  private final int x1y2z2;
  private final int x2y2z2;

  public CuboidImpl(final NumberedPointSet points, final NumberedTexCoordSet texCoords,
      double minX, double minY, double minZ,
      double width, double height, double depth) {

    this.points = points;
    this.texCoords = texCoords;
    this.faces = new ArrayList<>();

    x1y1z1 = points.addPoint(minX, minY, minZ);
    x2y1z1 = points.addPoint(minX + width, minY, minZ);
    x1y2z1 = points.addPoint(minX, minY + height, minZ);
    x2y2z1 = points.addPoint(minX + width, minY + height, minZ);
    x1y1z2 = points.addPoint(minX, minY, minZ + depth);
    x2y1z2 = points.addPoint(minX + width, minY, minZ + depth);
    x1y2z2 = points.addPoint(minX, minY + height, minZ + depth);
    x2y2z2 = points.addPoint(minX + width, minY + height, minZ + depth);
  }

  public Stream<CuboidFace> toFaceStream() {
    return faces.stream();
  }

  @Override
  public CuboidMeshBuilder.Cuboid addFrontFace(final FillFaces fillFaces) {
    faces.add(createFace(x1y1z1, x2y1z1, x1y2z1, x2y2z1, Plane.XY, fillFaces));
    return this;
  }

  @Override
  public CuboidMeshBuilder.Cuboid addBackFace(final FillFaces fillFaces) {
    faces.add(createFace(x2y1z2, x1y1z2, x2y2z2, x1y2z2, Plane.XY, fillFaces));
    return this;
  }

  @Override
  public CuboidMeshBuilder.Cuboid addLeftFace(final FillFaces fillFaces) {
    faces.add(createFace(x1y1z2, x1y1z1, x1y2z2, x1y2z1, Plane.YZ, fillFaces));
    return this;
  }

  @Override
  public CuboidMeshBuilder.Cuboid addRightFace(final FillFaces fillFaces) {
    faces.add(createFace(x2y1z1, x2y1z2, x2y2z1, x2y2z2, Plane.YZ, fillFaces));
    return this;
  }

  @Override
  public CuboidMeshBuilder.Cuboid addTopFace(final FillFaces fillFaces) {
    faces.add(createFace(x1y1z2, x2y1z2, x1y1z1, x2y1z1, Plane.XZ, fillFaces));
    return this;
  }

  @Override
  public CuboidMeshBuilder.Cuboid addBottomFace(final FillFaces fillFaces) {
    faces.add(createFace(x1y2z1, x2y2z1, x1y2z2, x2y2z2, Plane.XZ, fillFaces));
    return this;
  }

  private CuboidFace createFace(int topLeft, int topRight,
      int bottomLeft, int bottomRight, final Plane plane, final FillFaces fillFaces) {

    int topLeftTex = addTexCoord(topLeft, plane);
    int topRightTex = addTexCoord(topRight, plane);
    int bottomLeftTex = addTexCoord(bottomLeft, plane);
    int bottomRightTex = addTexCoord(bottomRight, plane);

    return new CuboidFace(topLeft, topRight, bottomLeft, bottomRight,
        topLeftTex, topRightTex, bottomLeftTex, bottomRightTex, fillFaces);
  }

  /**
   * Transpose the position in the plane to a coordinate on the XY plane
   */
  private int addTexCoord(int pointIndex, final Plane plane) {

    Point3D point = points.get(pointIndex);
    return switch (plane) {
      case XY -> texCoords.addTexCoord(point.getX(), point.getY());
      case XZ -> texCoords.addTexCoord(point.getX(), point.getZ());
      case YZ -> texCoords.addTexCoord(point.getY(), point.getZ());
    };
  }
}
