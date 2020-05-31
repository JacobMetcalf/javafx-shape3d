package uk.co.jacobmetcalf.javafx.shape3d.util;

import java.util.stream.DoubleStream;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jacobmetcalf.javafx.shape3d.Spheroid;

@ExtendWith(MockitoExtension.class)
public class SpheroidCrossSectionImplTest {

  @Mock
  public EllipticalToCartesianConverter mockConverter;

  @Test
  public void cross_section_creates_correct_points() {
    SpheroidCrossSection pole = new SpheroidCrossSection(0,0, null, mockConverter);
    SpheroidCrossSection unit = new SpheroidCrossSection(30, 1, pole, mockConverter);

    // We will just return x, y, z of all zero as we are not concerned about actual results
    Mockito.when(mockConverter.toCartesian(Mockito.anyDouble(), Mockito.anyDouble()))
        .thenAnswer((a) -> DoubleStream.of(0,0,0));

    double[] result = unit.getPoints().toArray();
    MatcherAssert.assertThat(result.length, Matchers.equalTo(Spheroid.DIVISIONS * 3));

    // We are not interested in the cartesian conversion as already tested
    // so just check that the plotter was called with the correct arguments, once for each division
    for (int i = 0; i < Spheroid.DIVISIONS; i++) {
      Mockito.verify(mockConverter).toCartesian(30, Spheroid.DIV_DEGREES * i);
    }
    Mockito.verifyNoMoreInteractions(mockConverter);
  }
}