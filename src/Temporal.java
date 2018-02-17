import java.awt.geom.Point2D;

public interface Temporal {
	void die();
	void timePassed();
	String getImageName();
	Point2D getCenterPoint();
}
