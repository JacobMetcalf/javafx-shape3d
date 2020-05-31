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
 * Draw a spheroid.
 */
public class Spheroid extends MeshView {

  public static final int DIVISIONS = 18;
  public static final double DIV_DEGREES = 360d / DIVISIONS;
  private final EllipticalToCartesianConverter converter;

  public static Spheroid build(double horizontalRadius, double verticalRadius) {
    return new Spheroid(180, false, new EllipticalToCartesianConverter(horizontalRadius, verticalRadius));
  }

  public static Spheroid buildHemispheroid(double horizontalRadius, double verticalRadius) {
    return new Spheroid(90, true, new EllipticalToCartesianConverter(horizontalRadius, verticalRadius));
  }

  Spheroid(double endAtTheta, boolean doubleSided, final EllipticalToCartesianConverter converter) {
    super();
    this.converter = converter;
    setDrawMode(DrawMode.FILL);
    List<SpheroidCrossSection> crossSections = createCrossSections(endAtTheta);
    this.setMesh(createTriangleMesh(crossSections, doubleSided));
  }

  private TriangleMesh createTriangleMesh(final List<SpheroidCrossSection> crossSections, boolean doubleSided) {
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

  private List<SpheroidCrossSection> createCrossSections(double endAtTheta) {
    var crossSections = new ArrayList<SpheroidCrossSection>();
    SpheroidCrossSection previous = new SpheroidCrossSection(0, 0, null, converter);
    crossSections.add(previous);
    int index = 1;
    for (double theta = DIV_DEGREES; theta <= endAtTheta; theta += DIV_DEGREES / 2) {
      SpheroidCrossSection current = new SpheroidCrossSection(theta, index, previous, converter);
      crossSections.add(current);
      index += DIVISIONS;
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
