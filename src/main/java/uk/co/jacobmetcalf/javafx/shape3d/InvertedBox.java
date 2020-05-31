package uk.co.jacobmetcalf.javafx.shape3d;

import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

/**
 * An inside out box with the top removed.
 */
public class InvertedBox extends MeshView {
  public InvertedBox(float width, float height, float depth) {
    super();
    setDrawMode(DrawMode.FILL);
    TriangleMesh mesh = new TriangleMesh();

    float x1 = - width / 2;
    float x2 = width / 2;

    float y1 = - height / 2;
    float y2 = height / 2;

    float z1 = depth / 2;
    float z2 = - depth / 2;

    mesh.getPoints().addAll(
        x1, y1, z1, // 0
        x2, y1, z1, // 1
        x1, y1, z2, // 2
        x2, y1, z2, // 3

        x1, y2, z1, // 4
        x2, y2, z1, // 5
        x1, y2, z2, // 6
        x2, y2, z2 // 7
    );

    mesh.getTexCoords().addAll(
        0, 0, // 0
        1, 0, // 1
        0, 1, // 2
        1, 1  // 3
    );

    // Top 0 1 Bottom 4 5
    //     2 3        6 7
    mesh.getFaces().addAll(
        // Left face
        0, 0, 2, 2, 6, 3,
        0, 0, 6, 3, 4, 1,

        // Right face
        1, 1, 7, 2, 3, 3,
        1, 0, 5, 1, 7, 3,

        // Back face
        0, 0, 4, 2, 1, 1,
        1, 1, 4, 2, 5, 3,

        // Front face
        2, 0, 3, 1, 6, 2,
        6, 2, 3, 3, 7, 1,

        // Bottom
        5, 1, 4, 0, 6, 2,
        5, 1, 6, 2, 7, 3
    );
    // Make all edges hard
    mesh.getFaceSmoothingGroups().setAll(new int[mesh.getFaces().size() / 6]);
    this.setMesh(mesh);
  }
}
