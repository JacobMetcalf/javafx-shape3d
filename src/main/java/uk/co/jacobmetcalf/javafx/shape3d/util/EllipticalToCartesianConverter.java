package uk.co.jacobmetcalf.javafx.shape3d.util;

import java.util.stream.DoubleStream;

/**
 * Uses a bit of maths to convert the spherical coordinates to cartesian. We will however
 * transpose the y and z axis to be more compatible with JavaFXs orientation of axis.
 **/
public class EllipticalToCartesianConverter {

  private final double verticalRadius;
  private final double horizontalRadius;

  public EllipticalToCartesianConverter(double horizontalRadius, double verticalRadius) {
    assert (horizontalRadius >= 0 && verticalRadius >= 0) : "Radii must be positive";
    this.verticalRadius = verticalRadius;
    this.horizontalRadius = horizontalRadius;
  }

  /**
   * @param theta (polar angle) angle in degrees from the y axis
   * @param phi (azimuth) angle from the x axis in the x-z plane
   * @return Stream of x,y,z
   */
  public DoubleStream toCartesian(double theta, double phi) {
    double thetaRadians = Math.toRadians(theta);
    double phiRadians = Math.toRadians(phi);
    double x = horizontalRadius * Math.sin(thetaRadians) * Math.cos(phiRadians);
    double y = -verticalRadius * Math.cos(thetaRadians); // In JavaFx up is -y
    double z = horizontalRadius * Math.sin(thetaRadians) * Math.sin(phiRadians);
    return DoubleStream.of(x, y, z);
  }
}
