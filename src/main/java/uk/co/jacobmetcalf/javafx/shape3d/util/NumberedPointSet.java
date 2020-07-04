package uk.co.jacobmetcalf.javafx.shape3d.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.DoubleStream;
import javafx.geometry.Point3D;

/**
 * Ordered set of Point3D which return
 */
public class NumberedPointSet {

  private final List<Point3D> points  = new ArrayList<>();

  public int addPoint(double x, double y, double z) {
    Point3D point = new Point3D(x, y, z);
    int index = points.lastIndexOf(point);
    if (index < 0) {
      index = points.size(); // It will be inserted here
      points.add(point);
    }
    return index;
  }

  /**
   * @param pointIndex Array index
   * @return Point at the index
   * @throws IndexOutOfBoundsException If index &lt; 0 or &gt;= size
   */
  public Point3D get(int pointIndex) throws IndexOutOfBoundsException {
    return points.get(pointIndex);
  }

  /**
   * @return Points in a stream of X, Y, Z triplets
   */
  public DoubleStream getXyzStream() {
    return points.stream()
        .flatMapToDouble(p -> DoubleStream.of(p.getX(), p.getY(), p.getZ()));
  }
}
