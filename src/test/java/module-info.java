open module uk.co.jacobmetcalf.javafx.shape3d {
  requires javafx.graphics;
  requires org.checkerframework.checker.qual;
  requires org.junit.jupiter.api;
  requires org.junit.jupiter.params;
  requires org.hamcrest;
  requires org.mockito;
  requires mockito.junit.jupiter;
  requires net.bytebuddy;

  exports uk.co.jacobmetcalf.javafx.shape3d;
  exports uk.co.jacobmetcalf.javafx.shape3d.util;
}