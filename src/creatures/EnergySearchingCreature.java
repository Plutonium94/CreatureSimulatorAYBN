package creatures;

import java.awt.geom.Point2D;

import creatures.visual.CreatureSimulator;
import java.awt.Color;

/***
 * A creature that searches and sets off for 
 * the nearest energy point as soon as its energy gets low;
 * @author Daniel
 *
 */
public class EnergySearchingCreature extends AbstractCreature {

	public EnergySearchingCreature(IEnvironment environment, Point2D position, double speed, double direction, Color color) {
		super(environment, position);
		this.speed = speed;
		this.direction = direction;
		this.color = Color.RED;
	}

	@Override
	public void act() {
		if(this.getEnergy() <= 40) {
			CreatureSimulator environment = (CreatureSimulator)getEnvironment();
			Point2D nearestEnergyPointCoordinates = environment.getNearestEnergyPointCoordinates(this);
			this.setDirection(this.directionFormAPoint(nearestEnergyPointCoordinates, Math.PI));
			this.move(2,2);
		} else {
			this.move(3, 3);
		}
		this.setEnergy(this.getEnergy()-10);
		this.color = cyanify(getColor());
	}
	
	
	private static Color redden(Color c) {
		int g = c.getGreen() - 10;
		int b = c.getBlue() - 10;
		int r = c.getRed() + 40;
		Color res = new Color(r,g, b);
		return res;
	}
	
	private static Color cyanify(Color c) {
		int g = c.getGreen() + 10;
		int b = c.getBlue() + 10;
		int r = c.getRed() - 10;
		Color res = new Color(r,g,b);
		return res;
	}

}
