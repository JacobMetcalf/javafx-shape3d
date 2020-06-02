package uk.co.jacobmetcalf.javafx.shape3d;

import javafx.scene.shape.TriangleMesh;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

public class SpheroidTest {

  static final int TEST_DIVISIONS = 6;
  @Test
  public void build_spheroid() {
    Spheroid unit = Spheroid.build(100, 200, TEST_DIVISIONS);
    assert_spheroid_mesh(unit, TEST_DIVISIONS);

    unit = Spheroid.build(100, 200);
    assert_spheroid_mesh(unit, Spheroid.DEFAULT_DIVISIONS);
  }

  @Test
  public void build_hemispheroid() {
    Spheroid unit = Spheroid.buildHemispheroid(100, 200, TEST_DIVISIONS);
    assert_hemisphere_mesh(unit, TEST_DIVISIONS);

    unit = Spheroid.buildHemispheroid(100, 200);
    assert_hemisphere_mesh(unit, Spheroid.DEFAULT_DIVISIONS);
  }

  private void assert_spheroid_mesh(Spheroid unit, int divisions) {
    TriangleMesh mesh = (TriangleMesh)unit.getMesh();
    int actualPoints = mesh.getPoints().size() / 3;
    int actualFaces = mesh.getFaces().size() / 6; // Tex coord format

    // We would expect 2 cross sections each with m points and two poles
    int expectedCrossSections = (divisions / 2 - 1);
    MatcherAssert.assertThat(actualPoints,
        Matchers.equalTo(divisions * expectedCrossSections + 2));

    // And 2 faces per division per cross section
    // (well its #cross sections -1 but then you add 2 sets of single triangles for each of the poles)
    MatcherAssert.assertThat(actualFaces,
        Matchers.equalTo(divisions * expectedCrossSections * 2));
  }

  private void assert_hemisphere_mesh(Spheroid unit, int divisions) {
    TriangleMesh mesh = (TriangleMesh)unit.getMesh();
    int actualPoints = mesh.getPoints().size() / 3;
    int actualFaces = mesh.getFaces().size() / 6; // Tex coord format

    // We would expect 2 cross sections each with n points and one pole
    int expectedCrossSections = (int)Math.ceil((double)divisions / 4);
    MatcherAssert.assertThat(actualPoints,
        Matchers.equalTo(divisions * expectedCrossSections + 1));

    // But since we also have to put interior faces on we would
    // get double the number of faces per cross section we expect compared to the sphere
    MatcherAssert.assertThat(actualFaces,
        Matchers.equalTo(divisions * expectedCrossSections * 4 - 2 * divisions));
  }
}