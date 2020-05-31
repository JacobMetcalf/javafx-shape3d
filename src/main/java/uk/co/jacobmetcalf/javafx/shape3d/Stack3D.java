package uk.co.jacobmetcalf.javafx.shape3d;

import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.transform.Translate;

/**
 * 3D layout container which places objects relative to the objects already in the stack.
 */
public class Stack3D {

  public enum PlacementX {
    ON_LEFT,
    ALIGN_LEFT,
    ALIGN_CENTRE,
    ALIGN_RIGHT,
    ON_RIGHT
  }

  public enum PlacementY {
    ABOVE,
    ALIGN_TOP,
    ALIGN_CENTRE,
    ALIGN_BOTTOM,
    BELOW
  }

  public enum PlacementZ {
    BEHIND,
    ALIGN_BACK,
    ALIGN_CENTRE,
    ALIGN_FRONT,
    IN_FRONT
  }

  final private Group stack;

  public Stack3D() {
    this.stack = new Group();
  }

  /**
   * Convenience method for adding the first node.
   */
  public Stack3D add(final Node child) {
      return add(child, PlacementX.ALIGN_CENTRE, PlacementY.ALIGN_CENTRE,
          PlacementZ.ALIGN_CENTRE);
  }

  /**
   * Adds a 3D shape to the stack positioning it relative to what is already in the stack
   */
  public Stack3D add(final Node child,
      final PlacementX placementX,
      final PlacementY placementY,
      final PlacementZ placementZ) {

    Bounds myBounds = stack.getBoundsInParent();
    stack.getChildren().add(child);
    Bounds childBounds = child.getBoundsInParent();

    child.getTransforms().add(new Translate(
        calculateTranslationX(placementX, myBounds, childBounds),
        calculateTranslationY(placementY, myBounds, childBounds),
        calculateTranslationZ(placementZ, myBounds, childBounds)));
    return this;
  }

  private double calculateTranslationX(PlacementX placementX,
      final Bounds myBounds, final Bounds childBounds) {
    return switch (placementX) {
      case ON_LEFT -> myBounds.getMinX() - childBounds.getMaxX();
      case ALIGN_LEFT -> myBounds.getMinX() - childBounds.getMinX();
      case ALIGN_CENTRE -> myBounds.getCenterX() - childBounds.getCenterX();
      case ALIGN_RIGHT -> myBounds.getMaxX() - childBounds.getMaxX();
      case ON_RIGHT -> myBounds.getMaxX() - childBounds.getMinX();
    };
  }

  private double calculateTranslationY(PlacementY placementY,
      final Bounds myBounds, final Bounds childBounds) {
    return switch (placementY) {
      // In JavaFX minY (-Y) is top
      case ABOVE -> myBounds.getMinY() - childBounds.getMaxY();
      case ALIGN_TOP -> myBounds.getMinY() - childBounds.getMinY();
      case ALIGN_CENTRE -> myBounds.getCenterY() - childBounds.getCenterY();
      case ALIGN_BOTTOM -> myBounds.getMaxY() - childBounds.getMaxY();
      case BELOW -> myBounds.getMaxY() - childBounds.getMinY();
    };
  }

  private double calculateTranslationZ(PlacementZ placementZ,
      final Bounds myBounds, final Bounds childBounds) {
    return switch (placementZ) {
      // In JavaFX minZ is front
      case IN_FRONT -> myBounds.getMinZ() - childBounds.getMaxZ();
      case ALIGN_FRONT -> myBounds.getMinZ() - childBounds.getMinZ();
      case ALIGN_CENTRE -> myBounds.getCenterZ() - childBounds.getCenterZ();
      case ALIGN_BACK -> myBounds.getMaxZ() - childBounds.getMaxZ();
      case BEHIND -> myBounds.getMaxZ() - childBounds.getMinZ();
    };
  }

  /**
   * @return Complete stack and return.
   */
  public Group toGroup() {
    return stack;
  }
}
