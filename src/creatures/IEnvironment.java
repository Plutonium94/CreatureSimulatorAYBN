package creatures;

import java.awt.Dimension;
import java.awt.geom.Point2D;


public interface IEnvironment {

	public Dimension getSize();

	public Iterable<ICreature> getCreatures();
	public Iterable<ICreature> creaturesNearByAPoint(Point2D point,  double radius) ;
}
