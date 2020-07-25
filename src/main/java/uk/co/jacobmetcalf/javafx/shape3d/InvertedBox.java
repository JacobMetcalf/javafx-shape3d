package uk.co.jacobmetcalf.javafx.shape3d;

import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import uk.co.jacobmetcalf.javafx.shape3d.CuboidMeshBuilder.Cuboid.FillFaces;

/**
 * An inside out box with the top removed.
 */
public class InvertedBox extends MeshView {
  public InvertedBox(float width, float height, float depth) {
    super();
    this.setDrawMode(DrawMode.FILL);
    CuboidMeshBuilder builder = new CuboidMeshBuilder();
    builder.addCuboid(width, height, depth)
        .addFrontFace(FillFaces.INTERIOR)
        .addBackFace(FillFaces.INTERIOR)
        .addBottomFace(FillFaces.INTERIOR)
        .addLeftFace(FillFaces.INTERIOR)
        .addRightFace(FillFaces.INTERIOR);

    this.setMesh(builder.build());
  }
}
