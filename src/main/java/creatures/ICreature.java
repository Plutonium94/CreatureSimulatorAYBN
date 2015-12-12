package creatures;

import java.awt.geom.Point2D;
import plug.IPlugin;
import simulator.IActionable;
import creatures.visual.CreatureShape;
import visual.IDrawable;

public interface ICreature extends IDrawable, IActionable, IPlugin {

	public IEnvironment getEnvironment();

	public double getSpeed();

	public double getDirection();

	public Point2D getPosition();

	public void setForme(CreatureShape forme);

	public abstract double distanceFromAPoint(Point2D p);
	public double getEnergy() ;
	public void setEnergy( double energy ) ;
	public boolean isAlive();
	public void  setAlive(boolean isalive);

	public abstract double directionFormAPoint(Point2D p, double axis);

}