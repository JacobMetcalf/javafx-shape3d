package uk.co.jacobmetcalf.javafx.shape3d;

import static org.hamcrest.MatcherAssert.assertThat;

import java.util.stream.IntStream;
import javafx.scene.shape.TriangleMesh;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import uk.co.jacobmetcalf.javafx.shape3d.CuboidMeshBuilder.Cuboid.FillFaces;

public class CuboidMeshBuilderTest {
  @Test
  public void can_construct_cube() {
    CuboidMeshBuilder unit = new CuboidMeshBuilder();
    unit.addCuboid(10, 10, 10)
        .addTopFace(FillFaces.EXTERIOR)
        .addBottomFace(FillFaces.EXTERIOR)
        .addLeftFace(FillFaces.EXTERIOR)
        .addRightFace(FillFaces.EXTERIOR)
        .addFrontFace(FillFaces.EXTERIOR)
        .addBackFace(FillFaces.EXTERIOR);
    TriangleMesh result = unit.build();

    float[] pointArray = result.getPoints().toArray(null);
    int[] faceArray = result.getFaces().toArray(null);

    // Should have 8 vertices with 3 coords
    assertThat(pointArray.length, Matchers.equalTo(8 * 3));

    // Check that each of the vertices 3 coords are +/-5
    IntStream.range(0, pointArray.length)
        .forEach(i -> assertThat(Math.abs(pointArray[i]), Matchers.equalTo(5f)));

    // Each triangular face is described by 6 points, Each of a cubes 6 sides
    // is composed of two triangles
    assertThat(faceArray.length, Matchers.equalTo(6 * 6 * 2));
  }
}