package uk.co.jacobmetcalf.javafx.shape3d;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.shape.TriangleMesh;
import uk.co.jacobmetcalf.javafx.shape3d.util.CuboidFace;
import uk.co.jacobmetcalf.javafx.shape3d.util.CuboidImpl;
import uk.co.jacobmetcalf.javafx.shape3d.util.DoubleToFloatCollector;
import uk.co.jacobmetcalf.javafx.shape3d.util.NumberedPointSet;
import uk.co.jacobmetcalf.javafx.shape3d.util.NumberedTexCoordSet;

/**
 * Builds a 3D shape out of cuboids. For each cuboid you specify a width, height and depth and
 * an optional min position (where the left-top-front vertex is positioned).
 *
 * The cuboid is initially just vertices, you then call add{side}Face() for each side you want filled in.
 * You choose whether to fill in the interior, exterior or both faces of the side.
 */
public class CuboidMeshBuilder {

  private final NumberedPointSet points;
  private final NumberedTexCoordSet texCoords;
  private final List<CuboidImpl> cuboids;

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

      CuboidImpl result = new CuboidImpl(points, texCoords, minX, minY, minZ, width, height, depth);
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
  public TriangleMesh build() {
    TriangleMesh mesh = new TriangleMesh();

    mesh.getPoints().addAll(points.getXyzStream()
        .collect(DoubleToFloatCollector::supplier, DoubleToFloatCollector::accumulator,
            DoubleToFloatCollector::combiner).toArray());

    mesh.getTexCoords().addAll(texCoords.getXyStream()
        .collect(DoubleToFloatCollector::supplier, DoubleToFloatCollector::accumulator,
            DoubleToFloatCollector::combiner).toArray());

    mesh.getFaces().addAll(cuboids.stream()
        .flatMap(CuboidImpl::toFaceStream)
        .flatMapToInt(CuboidFace::toMeshFaces)
        .toArray());

    mesh.getFaceSmoothingGroups().addAll(cuboids.stream()
        .flatMap(CuboidImpl::toFaceStream)
        .flatMapToInt(CuboidFace::toMeshSmoothingGroups)
        .toArray());

    return mesh;
  }

  /**
   * Each cuboid starts off initially empty and then you fill in the faces on each
   * side.
   */
  public interface Cuboid {

    /**
     * Enum for specifying which faces of the cuboid to fill.
     */
    enum FillFaces {EXTERIOR, INTERIOR, BOTH}

    /**
     * Fills in the faces at the front of the cuboid. Specify interior, exterior or both.
     */
    Cuboid addFrontFace(FillFaces fillFaces);

    /**
     * Fills in the faces at the back of the cuboid. Specify interior, exterior or both.
     */
    Cuboid addBackFace(FillFaces fillFaces);

    /**
     * Fills in the faces to the left of the cuboid. Specify interior, exterior or both.
     */
    Cuboid addLeftFace(FillFaces fillFaces);

    /**
     * Fills in the faces to the right of the cuboid. Specify interior, exterior or both.
     */
    Cuboid addRightFace(FillFaces fillFaces);

    /**
     * Fills in the faces on the top of the cuboid. Specify interior, exterior or both.
     */
    Cuboid addTopFace(FillFaces fillFaces);

    /**
     * Fills in the faces on the bottom of the cuboid. Specify interior, exterior or both.
     */
    Cuboid addBottomFace(FillFaces fillFaces);
  }
}
