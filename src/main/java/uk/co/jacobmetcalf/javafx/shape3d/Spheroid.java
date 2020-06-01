package uk.co.jacobmetcalf.javafx.shape3d;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import uk.co.jacobmetcalf.javafx.shape3d.util.DoubleToFloatCollector;
import uk.co.jacobmetcalf.javafx.shape3d.util.EllipticalToCartesianConverter;
import uk.co.jacobmetcalf.javafx.shape3d.util.SpheroidCrossSection;
import uk.co.jacobmetcalf.javafx.shape3d.util.TriangleMeshHelper;

/**
 * Draw a spheroid or a hemispheroid (bowl).
 *
 * A spheroid differs from a sphere in that it is spherical in horizontal cross section but
 * elliptical in vertical cross section.
 *
 * With a hemispheroid the interior is empty so will be lined with faces.
 */
public class Spheroid extends MeshView {

  public static final int DEFAULT_DIVISIONS = 18;
  private final EllipticalToCartesianConverter converter;

  public static Spheroid build(double horizontalRadius, double verticalRadius) {
    return build(horizontalRadius, verticalRadius, DEFAULT_DIVISIONS);
  }

  public static Spheroid build(double horizontalRadius, double verticalRadius, int divisions) {
    return new Spheroid(180, divisions,false,
        new EllipticalToCartesianConverter(horizontalRadius, verticalRadius));
  }

  public static Spheroid buildHemispheroid(double horizontalRadius, double verticalRadius) {
    return buildHemispheroid(horizontalRadius, verticalRadius, DEFAULT_DIVISIONS);
  }

  public static Spheroid buildHemispheroid(double horizontalRadius, double verticalRadius, int divisions) {
    return new Spheroid(90, divisions,true,
        new EllipticalToCartesianConverter(horizontalRadius, verticalRadius));
  }

  Spheroid(double endAtTheta, int divisions, boolean doubleSided,
      final EllipticalToCartesianConverter converter) {
    super();
    assert (divisions >= 6 && divisions <= 50): "Divisions must be in range 6-50";
    this.converter = converter;
    setDrawMode(DrawMode.FILL);
    List<SpheroidCrossSection> crossSections = createCrossSections(endAtTheta, divisions);
    this.setMesh(createTriangleMesh(crossSections, doubleSided));
  }

  private TriangleMesh createTriangleMesh(final List<SpheroidCrossSection> crossSections,
      boolean doubleSided) {
    TriangleMesh mesh = new TriangleMesh();

    mesh.getPoints().addAll(crossSections.stream()
        .flatMapToDouble(SpheroidCrossSection::getPoints)
        .collect(DoubleToFloatCollector::supplier, DoubleToFloatCollector::accumulator,
            DoubleToFloatCollector::combiner).toArray());

    IntStream faces = crossSections.stream()
        .flatMapToInt(SpheroidCrossSection::getFaces);

    if (doubleSided) {
      faces = TriangleMeshHelper.addReverseFace(faces);
    }

    mesh.getFaces().addAll(faces.toArray());
    mesh.getTexCoords().addAll(getTexPoints());

    // If we are double sided then we need to divide into two smoothing groups
    TriangleMeshHelper.setSmoothingGroups(mesh, doubleSided);
    return mesh;
  }

  private List<SpheroidCrossSection> createCrossSections(double endAtTheta, int divisions) {
    var crossSections = new ArrayList<SpheroidCrossSection>();
    SpheroidCrossSection previous = new SpheroidCrossSection(0, 0,
        divisions,null, converter);
    crossSections.add(previous);
    int index = 1;
    double theta = 0;

    // Vertically we only need half the number of cross sections as divisions
    // so for a sphere we stop at 180 and for a hemisphere at 90. Note the
    // min so that the last cross section is always on 90.
    while (theta < endAtTheta) {
      theta = Math.min(theta + 360d / divisions, endAtTheta);
      SpheroidCrossSection current = new SpheroidCrossSection(theta, index,
          divisions, previous, converter);
      crossSections.add(current);
      index += divisions;
      previous = current;
    }
    return crossSections;
  }

  /**
   * @return Basic square approach to texture mapping
   */
  private float[] getTexPoints() {
    return new float[]{
        0, 0, // TOP_LEFT_TEX
        1, 0, // TOP_RIGHT_TEX
        0, 1, // BOTTOM_LEFT_TEX
        1, 1  // BOTTOM_RIGHT_TEX
    };
  }
}
