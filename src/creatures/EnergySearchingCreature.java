package creatures;

import java.awt.geom.Point2D;

import creatures.visual.CreatureSimulator;
import simulator.EnergyPoint;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

import java.awt.Color;

/***
 * A creature that searches and sets off for 
 * the nearest energy point as soon as its energy gets low;
 * @author Plutonium94
 *
 */
public class EnergySearchingCreature extends AbstractCreature {
	
	public static double reduceEnergyFactor = 0.2;
	public static int gainEnergyFactor = 40;

	public EnergySearchingCreature(IEnvironment environment, Point2D position, double speed, double direction, Color color) {
		super(environment, position);
		this.speed = speed;
		this.direction = direction;
		this.color = Color.RED;
	}

	@Override
	public void act() {
		if(this.getEnergy() <= 0) {
			this.setAlive(false); // mourir sans energie
			return;
		}
		if(this.getEnergy() <= 50) {
			CreatureSimulator environment = (CreatureSimulator)getEnvironment();
			EnergyPoint nearestEnergyPoint = environment.getNearestEnergyPoint(this);
			beEnergized(nearestEnergyPoint);
			Point2D nearestEnergyPointCoordinates = nearestEnergyPoint.getPosition();
			this.setDirection(this.directionFormAPoint(nearestEnergyPointCoordinates, Math.PI));
			double incX = speed * cos(direction);
			double incY = speed * sin(direction);
			move(incX, incY);
		} else {
			this.move(3, 3);
		}
<<<<<<< HEAD
		this.setEnergy(this.getEnergy()-10);
		//this.color = cyanify(getColor());
=======
		this.setEnergy(this.getEnergy()-reduceEnergyFactor);
		//this.color = cyanify(getColor());
		cyanify();
>>>>>>> bf5e7b38685df0e82818c64949a02f950029b554
	}
	
	public boolean beEnergized(EnergyPoint ep) {
		if(ep.isInVicinity(this)) {
			ep.energize(this);
			//this.color = redden(getColor());
			redden();
			return true;
		}
		return false;
	}
	
	public void redden() {
		double increaseColor = gainEnergyFactor * 2.55; 
		int g = Math.max(0,(int)(color.getGreen() - increaseColor));
		int b = Math.max(0, (int)(color.getBlue() - increaseColor));
		int r = Math.min(255, (int)(color.getRed() + increaseColor));
		Color res = new Color(r,g, b);
		color =  res;
	}
	
	public void cyanify() {
		double decreaseColor = reduceEnergyFactor * 2.55;
		int g = Math.min(255, (int)(color.getGreen() + decreaseColor));
		int b = Math.min(255, (int)(color.getBlue() + decreaseColor));
		int r = Math.max(0, (int)(color.getRed() - decreaseColor));
		Color res = new Color(r,g,b);
		color = res;
	}
	
	
	static Color redden(Color c) {
		double increaseColor = gainEnergyFactor * 2.55; 
		int g = Math.max(0,(int)(c.getGreen() - increaseColor));
		int b = Math.max(0, (int)(c.getBlue() - increaseColor));
		int r = Math.min(255, (int)(c.getRed() + increaseColor));
		Color res = new Color(r,g, b);
		return res;
	}
	
	static Color cyanify(Color c) {
		double decreaseColor = reduceEnergyFactor * 2.55;
		int g = Math.min(255, (int)(c.getGreen() + decreaseColor));
		int b = Math.min(255, (int)(c.getBlue() + decreaseColor));
		int r = Math.max(0, (int)(c.getRed() - decreaseColor));
		Color res = null;
		try {
			res = new Color(r,g,b);
		} catch(IllegalArgumentException iae) {
			System.err.println("bad r value : " + r + ", red : " + c.getRed());
			System.err.println("bad g value : " + g + ", green : " + c.getGreen());
			System.err.println("bad b value : " + b + ", blue: " + c.getBlue());
		}
		
		return res;
	}
	


}
