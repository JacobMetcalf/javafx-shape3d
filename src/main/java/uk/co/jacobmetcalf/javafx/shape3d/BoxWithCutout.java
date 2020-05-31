package uk.co.jacobmetcalf.javafx.shape3d;

import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

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
    TriangleMesh mesh = new TriangleMesh();

    float x1 = - width / 2;
    float x2 = x1 + cutOutX;
    float x3 = x2 + cutOutWidth;
    float x4 = width / 2;

    float z1  = depth / 2;
    float z2 = z1 - cutOutZ;
    float z3 = z2 - cutOutDepth;
    float z4 = - depth / 2;

    float y1 = - thickness / 2;  // top
    float y2 = thickness / 2;    // bottom

    mesh.getPoints().addAll(
        x1, y1, z1, // 0
        x2, y1, z1, // 1
        x3, y1, z1, // 2
        x4, y1, z1, // 3
        x2, y1, z2, // 4
        x3, y1, z2, // 5
        x2, y1, z3, // 6
        x3, y1, z3, // 7
        x1, y1, z4, // 8
        x2, y1, z4, // 9
        x3, y1, z4, // 10
        x4, y1, z4, // 11

        x1, y2, z1, // 12
        x2, y2, z1, // 13
        x3, y2, z1, // 14
        x4, y2, z1, // 15
        x2, y2, z2, // 16
        x3, y2, z2, // 17
        x2, y2, z3, // 18
        x3, y2, z3, // 19
        x1, y2, z4, // 20
        x2, y2, z4, // 21
        x3, y2, z4, // 22
        x4, y2, z4  // 23
    );

    // All has to re-expressed relative to 1 with 0, 0 at corner of image
    mesh.getTexCoords().addAll(
        x1 / width + 0.5f, z1 / depth + 0.5f, // 0
        x2 / width + 0.5f, z1 / depth + 0.5f, // 1
        x3 / width + 0.5f, z1 / depth + 0.5f, // 2
        x4 / width + 0.5f, z1 / depth + 0.5f, // 3
        x2 / width + 0.5f, z2 / depth + 0.5f, // 4
        x3 / width + 0.5f, z2 / depth + 0.5f, // 5
        x2 / width + 0.5f, z3 / depth + 0.5f, // 6
        x3 / width + 0.5f, z3 / depth + 0.5f, // 7
        x1 / width + 0.5f, z4 / depth + 0.5f, // 8
        x2 / width + 0.5f, z4 / depth + 0.5f, // 9
        x3 / width + 0.5f, z4 / depth + 0.5f, // 10
        x4 / width + 0.5f, z4 / depth + 0.5f // 11
    );

    // Layout (top)
    // 0....1.....2.....3
    // .....4     5......
    // .....6     7......
    // 8....9....10....11

    mesh.getFaces().addAll(
        // Top face
        0, 0, 8, 8, 1, 1,
        1, 1, 8, 8, 9, 9,
        2, 2, 1, 1, 4, 4,
        2, 2, 4, 4, 5, 5,
        6, 6, 10, 10, 7, 7,
        6, 6, 9, 9, 10, 10,
        3, 3, 2, 2, 10, 10,
        3, 3, 10, 10, 11, 11,

        // Bottom face
        12, 0, 13, 1, 20, 8,
        13, 1, 21, 9, 20, 8,
        14, 2, 16, 4, 13, 1,
        14, 2, 17, 5,16, 4,
        18, 6, 19, 7, 22, 10,
        18, 6, 22, 10,21, 9,
        15, 3, 22, 10, 14, 2,
        15, 3, 23, 11, 22, 10,

        // Front edge
        11, 3, 8, 0, 20, 8,
        11, 3, 20, 8, 23, 11,

        // Back edge
        0, 3, 3, 0, 12, 8,
        12, 3, 3, 8, 15, 11,

        // Left edge
        0, 1, 12, 0, 8, 9,
        8, 9, 12, 0, 20, 8,

        // Right edge
        3, 2, 11, 10, 23, 11,
        3, 2, 23, 11, 15, 3
    );

    // Make all edges hard
    mesh.getFaceSmoothingGroups().setAll(new int[mesh.getFaces().size() / 6]);

    this.setMesh(mesh);
  }
}
