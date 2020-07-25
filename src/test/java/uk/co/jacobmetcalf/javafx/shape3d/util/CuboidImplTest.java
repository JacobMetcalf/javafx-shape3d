package uk.co.jacobmetcalf.javafx.shape3d.util;

import java.util.List;
import java.util.stream.Collectors;
import javafx.geometry.Point3D;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jacobmetcalf.javafx.shape3d.CuboidMeshBuilder.Cuboid.FillFaces;

@ExtendWith(MockitoExtension.class)
public class CuboidImplTest {

  @Mock
  public NumberedPointSet mockPoints;

  @Mock
  public NumberedTexCoordSet mockTexCoords;

  public CuboidImpl unit;

  @BeforeEach
  public void init() {
    Mockito.when(mockPoints.addPoint(-10, -10,-10)).thenReturn(0);
    Mockito.when(mockPoints.addPoint(10, -10, -10)).thenReturn(1);
    Mockito.when(mockPoints.addPoint(-10, 10, -10)).thenReturn(2);
    Mockito.when(mockPoints.addPoint(10, 10, -10)).thenReturn(3);
    Mockito.when(mockPoints.addPoint(-10, -10, 10)).thenReturn(4);
    Mockito.when(mockPoints.addPoint(10, -10, 10)).thenReturn(5);
    Mockito.when(mockPoints.addPoint(-10, 10, 10)).thenReturn(6);
    Mockito.when(mockPoints.addPoint(10, 10, 10)).thenReturn(7);

    unit = new CuboidImpl(mockPoints, mockTexCoords, -10, -10, -10,
        20, 20,20);

    // Each face will always create these 4 texCoords
    Mockito.when(mockTexCoords.addTexCoord(-10, -10)).thenReturn(0);
    Mockito.when(mockTexCoords.addTexCoord(10, -10)).thenReturn(1);
    Mockito.when(mockTexCoords.addTexCoord(-10, 10)).thenReturn(2);
    Mockito.when(mockTexCoords.addTexCoord(10, 10)).thenReturn(3);
  }

  @Test
  public void add_front_face() {

    Mockito.when(mockPoints.get(0)).thenReturn(new Point3D(-10, -10, -10));
    Mockito.when(mockPoints.get(1)).thenReturn(new Point3D(10, -10, -10));
    Mockito.when(mockPoints.get(2)).thenReturn(new Point3D(-10, 10, -10));
    Mockito.when(mockPoints.get(3)).thenReturn(new Point3D(10, 10, -10));

    unit.addFrontFace(FillFaces.EXTERIOR);
    Mockito.verifyNoMoreInteractions(mockPoints, mockTexCoords);

    List<CuboidFace> faces = unit.toFaceStream().collect(Collectors.toList());
    MatcherAssert.assertThat(faces.get(0), Matchers.equalTo(
        new CuboidFace(0,1,2,3,
            0,1,2,3, FillFaces.EXTERIOR)));
  }

  @Test
  public void add_back_face() {

    Mockito.when(mockPoints.get(4)).thenReturn(new Point3D(-10, -10, 10));
    Mockito.when(mockPoints.get(5)).thenReturn(new Point3D(10, -10, 10));
    Mockito.when(mockPoints.get(6)).thenReturn(new Point3D(-10, 10, 10));
    Mockito.when(mockPoints.get(7)).thenReturn(new Point3D(10, 10, 10));

    unit.addBackFace(FillFaces.EXTERIOR);
    Mockito.verifyNoMoreInteractions(mockPoints, mockTexCoords);

    List<CuboidFace> faces = unit.toFaceStream().collect(Collectors.toList());
    MatcherAssert.assertThat(faces.get(0), Matchers.equalTo(
        new CuboidFace(5,4,7,6,
            1,0,3,2, FillFaces.EXTERIOR)));
  }

  @Test
  public void add_top_face() {

    Mockito.when(mockPoints.get(0)).thenReturn(new Point3D(-10, -10, -10));
    Mockito.when(mockPoints.get(1)).thenReturn(new Point3D(10, -10, -10));
    Mockito.when(mockPoints.get(4)).thenReturn(new Point3D(-10, -10, 10));
    Mockito.when(mockPoints.get(5)).thenReturn(new Point3D(10, -10, 10));

    unit.addTopFace(FillFaces.EXTERIOR);
    Mockito.verifyNoMoreInteractions(mockPoints, mockTexCoords);

    List<CuboidFace> faces = unit.toFaceStream().collect(Collectors.toList());
    MatcherAssert.assertThat(faces.get(0), Matchers.equalTo(
        new CuboidFace(4,5,0,1,
            2,3,0,1, FillFaces.EXTERIOR)));
  }

  @Test
  public void add_bottom_face() {

    Mockito.when(mockPoints.get(2)).thenReturn(new Point3D(-10, 10, -10));
    Mockito.when(mockPoints.get(3)).thenReturn(new Point3D(10, 10, -10));
    Mockito.when(mockPoints.get(6)).thenReturn(new Point3D(-10, 10, 10));
    Mockito.when(mockPoints.get(7)).thenReturn(new Point3D(10, 10, 10));

    unit.addBottomFace(FillFaces.EXTERIOR);
    Mockito.verifyNoMoreInteractions(mockPoints, mockTexCoords);

    List<CuboidFace> faces = unit.toFaceStream().collect(Collectors.toList());
    MatcherAssert.assertThat(faces.get(0), Matchers.equalTo(
        new CuboidFace(2,3,6,7,
            0,1,2, 3, FillFaces.EXTERIOR)));
  }

  @Test
  public void add_left_face() {

    Mockito.when(mockPoints.get(0)).thenReturn(new Point3D(-10, -10, -10));
    Mockito.when(mockPoints.get(2)).thenReturn(new Point3D(-10, 10, -10));
    Mockito.when(mockPoints.get(4)).thenReturn(new Point3D(-10, -10, 10));
    Mockito.when(mockPoints.get(6)).thenReturn(new Point3D(-10, 10, 10));

    unit.addLeftFace(FillFaces.EXTERIOR);
    Mockito.verifyNoMoreInteractions(mockPoints, mockTexCoords);

    List<CuboidFace> faces = unit.toFaceStream().collect(Collectors.toList());
    MatcherAssert.assertThat(faces.get(0), Matchers.equalTo(
        new CuboidFace(4,0,6,2,
            2,0,3, 1, FillFaces.EXTERIOR)));
  }

  @Test
  public void add_right_face() {

    Mockito.when(mockPoints.get(1)).thenReturn(new Point3D(10, -10, -10));
    Mockito.when(mockPoints.get(3)).thenReturn(new Point3D(10, 10, -10));
    Mockito.when(mockPoints.get(5)).thenReturn(new Point3D(10, -10, 10));
    Mockito.when(mockPoints.get(7)).thenReturn(new Point3D(10, 10, 10));

    unit.addRightFace(FillFaces.EXTERIOR);
    Mockito.verifyNoMoreInteractions(mockPoints, mockTexCoords);

    List<CuboidFace> faces = unit.toFaceStream().collect(Collectors.toList());
    MatcherAssert.assertThat(faces.get(0), Matchers.equalTo(
        new CuboidFace(1,5,3,7,
            0,2,1, 3, FillFaces.EXTERIOR)));
  }
}