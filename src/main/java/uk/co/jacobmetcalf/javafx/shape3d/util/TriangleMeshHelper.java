package uk.co.jacobmetcalf.javafx.shape3d.util;

import java.util.stream.IntStream;
import javafx.scene.shape.TriangleMesh;

/**
 * Utilities for dealing with manipulating streams of JavaFx TexCoord
 */
public final class TriangleMeshHelper {

  /**
   * Takes a stream of integers representing a set of faces and adds the reverse side.
   * In JavaFx the outward face is denoted by going through the points in an anti clockwise fashion
   * the inward face in clockwise.
   */
  public static IntStream addReverseFace(final IntStream in) {
    int[] inArray = in.toArray();
    int length = inArray.length;
    int[] outArray = new int[length * 2];
    System.arraycopy(inArray, 0, outArray, 0, length);

    for (int i = 0; i < length; i += 2) {
      outArray[2 * length - (i + 2)] = inArray[i];
      outArray[2 * length - (i + 1)] = inArray[i + 1];
    }
    return IntStream.of(outArray);
  }

  /**
   * If the mesh is double sided the below divides the exterior faces and interior faces into
   * two smoothing groups.
   */
  public static void setSmoothingGroups(final TriangleMesh mesh, boolean doubleSided) {
    if (doubleSided) {
      int numberOfFaces = mesh.getFaces().size() / 6;
      int[] faceSmoothingGroups = IntStream.range(0, mesh.getFaces().size() / 6)
          .map(f -> (f < numberOfFaces / 2) ? 1 : 2).toArray();
      mesh.getFaceSmoothingGroups().setAll(faceSmoothingGroups);
    }
  }

  private TriangleMeshHelper() {}
}
