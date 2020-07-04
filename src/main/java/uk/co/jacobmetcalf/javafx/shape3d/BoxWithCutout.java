package uk.co.jacobmetcalf.javafx.shape3d;

import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import uk.co.jacobmetcalf.javafx.shape3d.util.CuboidMeshBuilder;
import uk.co.jacobmetcalf.javafx.shape3d.util.CuboidMeshBuilder.FillFaces;

/**
 * A box with a rectangular cut out
 */
public class BoxWithCutout extends MeshView {

  public BoxWithCutout(float width, float thickness, float depth,
      float cutOutWidth, float cutOutDepth, float cutOutX, float cutOutZ) {

    super();
    setDrawMode(DrawMode.FILL);

    assert ((cutOutX + cutOutWidth) < width) : "Cut out must be within width";
    assert ((cutOutZ + cutOutDepth) < depth) : "Cut out must be within depth";

    double minX = -width / 2;
    double cutOutMinX = minX + cutOutX;
    double cutOutMaxX = minX + cutOutX + cutOutWidth;
    double cutOutRemainderX = width - cutOutWidth - cutOutX;
    double minY = -thickness / 2;
    double minZ = -depth / 2;
    double cutOutFrontDepth = depth - cutOutDepth - cutOutZ;
    double cutOutMaxZ = minZ + cutOutDepth + cutOutFrontDepth;

    CuboidMeshBuilder builder = new CuboidMeshBuilder();

    builder.addCuboid(minX, minY, minZ, cutOutX, thickness, depth)
        .addFrontFace(FillFaces.EXTERIOR)
        .addBackFace(FillFaces.EXTERIOR)
        .addTopFace(FillFaces.EXTERIOR)
        .addBottomFace(FillFaces.EXTERIOR)
        .addLeftFace(FillFaces.EXTERIOR);

    builder.addCuboid(cutOutMinX, minY, minZ, cutOutWidth, thickness, cutOutFrontDepth)
        .addFrontFace(FillFaces.EXTERIOR)
        .addTopFace(FillFaces.EXTERIOR)
        .addBottomFace(FillFaces.EXTERIOR);

    builder.addCuboid(cutOutMinX, minY, cutOutMaxZ, cutOutWidth, thickness, cutOutZ)
        .addBackFace(FillFaces.EXTERIOR)
        .addTopFace(FillFaces.EXTERIOR)
        .addBottomFace(FillFaces.EXTERIOR);

    builder.addCuboid(cutOutMaxX, minY, minZ, cutOutRemainderX, thickness, depth)
        .addFrontFace(FillFaces.EXTERIOR)
        .addBackFace(FillFaces.EXTERIOR)
        .addTopFace(FillFaces.EXTERIOR)
        .addBottomFace(FillFaces.EXTERIOR)
        .addRightFace(FillFaces.EXTERIOR);

    this.setMesh(builder.toMesh());
  }
}
