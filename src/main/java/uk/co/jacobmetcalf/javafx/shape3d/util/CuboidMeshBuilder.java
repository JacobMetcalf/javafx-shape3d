package uk.co.jacobmetcalf.javafx.shape3d.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import javafx.scene.shape.TriangleMesh;
import uk.co.jacobmetcalf.javafx.shape3d.util.CuboidFace.Plane;

public class CuboidMeshBuilder {

  public enum FillFaces {EXTERIOR, INTERIOR, BOTH}

  private final NumberedPointSet points;
  private final NumberedTexCoordSet texCoords;
  private final List<Cuboid> cuboids;

  public CuboidMeshBuilder() {
    points = new NumberedPointSet();
    texCoords = new NumberedTexCoordSet();
    cuboids = new ArrayList<>();
  }

  /**
   * Add a cuboid to the structure.
   */
  public Cuboid addCuboid(double minX, double minY, double minZ,
      double width, double height, double depth) {

      Cuboid result = new Cuboid(minX, minY, minZ, width, height, depth);
      cuboids.add(result);
      return result;
  }

  /**
   * Convenience method which adds a cuboid in the centre.
   */
  public Cuboid addCuboid(double width, double height, double depth) {
    return addCuboid(-width / 2, -height / 2, -depth / 2,
        width, height, depth);
  }

  /**
   * @return A mesh built up from the cubes and faces.
   */
  public TriangleMesh toMesh() {
    TriangleMesh mesh = new TriangleMesh();

    mesh.getPoints().addAll(points.getXyzStream()
        .collect(DoubleToFloatCollector::supplier, DoubleToFloatCollector::accumulator,
            DoubleToFloatCollector::combiner).toArray());

    mesh.getTexCoords().addAll(texCoords.getXyStream()
        .collect(DoubleToFloatCollector::supplier, DoubleToFloatCollector::accumulator,
            DoubleToFloatCollector::combiner).toArray());

    mesh.getFaces().addAll(cuboids.stream()
        .flatMap(Cuboid::toFaceStream)
        .flatMapToInt(CuboidFace::toMeshFaces)
        .toArray());

    mesh.getFaceSmoothingGroups().addAll(cuboids.stream()
        .flatMap(Cuboid::toFaceStream)
        .flatMapToInt(CuboidFace::toMeshSmoothingGroups)
        .toArray());

    return mesh;
  }

  public class Cuboid {
    private final int x1y1z1;
    private final int x2y1z1;
    private final int x1y2z1;
    private final int x2y2z1;
    private final int x1y1z2;
    private final int x2y1z2;
    private final int x1y2z2;
    private final int x2y2z2;
    private final List<CuboidFace> faces;

    public Cuboid (double minX, double minY, double minZ,
                   double width, double height, double depth) {

      x1y1z1 = points.addPoint(minX, minY, minZ);
      x2y1z1 = points.addPoint(minX + width, minY, minZ);
      x1y2z1 = points.addPoint(minX, minY + height, minZ);
      x2y2z1 = points.addPoint(minX + width, minY + height, minZ);
      x1y1z2 = points.addPoint(minX, minY, minZ + depth);
      x2y1z2 = points.addPoint(minX + width, minY, minZ + depth);
      x1y2z2 = points.addPoint(minX, minY + height, minZ + depth);
      x2y2z2 = points.addPoint(minX + width, minY + height, minZ + depth);

      faces = new ArrayList<>();
    }

    public Stream<CuboidFace> toFaceStream() {
      return faces.stream();
    }

    public Cuboid addFrontFace(final FillFaces fillFaces) {
      faces.add(new CuboidFace(x1y1z1, x2y1z1, x1y2z1, x2y2z1, fillFaces, Plane.XY, points, texCoords));
      return this;
    }

    public Cuboid addBackFace(final FillFaces fillFaces) {
      faces.add(new CuboidFace(x2y1z2, x1y1z2, x2y2z2, x1y2z2, fillFaces, Plane.XY, points, texCoords));
      return this;
    }

    public Cuboid addLeftFace(final FillFaces fillFaces) {
      faces.add(new CuboidFace(x1y1z2, x1y1z1, x1y2z2, x1y2z1, fillFaces, Plane.YZ, points, texCoords));
      return this;
    }

    public Cuboid addRightFace(final FillFaces fillFaces) {
      faces.add(new CuboidFace(x2y1z1, x2y1z2, x2y2z1, x2y2z2, fillFaces, Plane.YZ, points, texCoords));
      return this;
    }

    public Cuboid addTopFace(final FillFaces fillFaces) {
      faces.add(new CuboidFace(x1y1z2, x2y1z2, x1y1z1, x2y1z1, fillFaces, Plane.XZ, points, texCoords));
      return this;
    }

    public Cuboid addBottomFace(final FillFaces fillFaces) {
      faces.add(new CuboidFace(x1y2z1, x2y2z1, x1y2z2, x2y2z2, fillFaces, Plane.XZ, points, texCoords));
      return this;
    }
  }
}
