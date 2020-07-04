package uk.co.jacobmetcalf.javafx.shape3d.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.DoubleStream;
import javafx.util.Pair;

/**
 * When covering JavaFx 3d shapes with a material each face vertex needs an x,y
 * coordinate of where it maps to in the two dimensional world of the Material.
 * To put this into perspective imagine wrapping a shape with an jpeg.
 * <br/>
 * This class helps with this task in two ways:<ul>
 *   <li>It stores the set of points and numbers them, ensuring that there are no duplicates.</li>
 *   <li>It normalises the coordinates to a 0 to 1 scale.</li>
 * </ul>
 */
public class NumberedTexCoordSet {

  private final List<Pair<Double, Double>> coords = new ArrayList<>();
  private double minX = Double.MAX_VALUE;
  private double maxX = Double.MIN_VALUE;
  private double minY = Double.MAX_VALUE;
  private double maxY = Double.MIN_VALUE;

  /**
   * If the coord does not exist then creates it and returns a new index.
   * If it does exist returns the new index.
   */
  public int addTexCoord(double x, double y) {
    var coord = new Pair<>(x, y);
    int index = coords.lastIndexOf(coord);
    if (index < 0) {
      updateBounds(x, y);
      index = coords.size(); // It will be inserted here
      coords.add(coord);
    }
    return index;
  }

  /**
   * @return Coordinates as a flat stream i.e. X1, Y1, X2, Y2
   */
  public DoubleStream getXyStream() {
    var transform = createTransform();
    return coords.stream().flatMapToDouble(transform);
  }

  private void updateBounds(double x, double y) {
    minX = Math.min(minX, x);
    maxX = Math.max(maxX, x);
    minY = Math.min(minY, y);
    maxY = Math.max(maxY, y);
  }

  private Function<Pair<Double, Double>, DoubleStream> createTransform() {
    double offsetX = -minX;
    double factorX = 1 / (maxX - minX);
    double offsetY = -minY;
    double factorY = 1 / (maxY - minY);

    return p -> DoubleStream.of(
        (p.getKey() + offsetX) * factorX,
        (p.getValue() + offsetY) * factorY);
  }
}
