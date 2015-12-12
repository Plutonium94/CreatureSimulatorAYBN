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
	public static int gainEnergyFactor = EnergyPoint.DEFAULT_ENERGY;

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
		if(this.getEnergy() <= 70) {
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

		this.setEnergy(this.getEnergy()-reduceEnergyFactor);
		//this.color = cyanify(getColor());
		cyanify();

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
		int g = Math.max(0,(int)Math.round(color.getGreen() - increaseColor));
		int b = Math.max(0, (int)Math.round(color.getBlue() - increaseColor));
		int r = Math.min(255, (int)Math.round(color.getRed() + increaseColor));
		Color res = new Color(r,g, b);
		color =  res;
	}
	
	public void cyanify() {
		double decreaseColor = reduceEnergyFactor * 2.55;
		int g = Math.min(255, (int)Math.round(color.getGreen() + decreaseColor));
		int b = Math.min(255, (int)Math.round(color.getBlue() + decreaseColor));
		int r = Math.max(0, (int)Math.round(color.getRed() - decreaseColor));
		Color res = new Color(r,g,b);
		color = res;
	}
	
	
	static Color redden(Color c) {
		double increaseColor = gainEnergyFactor * 2.55; 
		int g = Math.max(0,(int)Math.round(c.getGreen() - increaseColor));
		int b = Math.max(0, (int)Math.round(c.getBlue() - increaseColor));
		int r = Math.min(255, (int)Math.round(c.getRed() + increaseColor));
		Color res = new Color(r,g, b);
		return res;
	}
	
	static Color cyanify(Color c) {
		double decreaseColor = reduceEnergyFactor * 2.55;
		int g = Math.min(255, (int)Math.round((c.getGreen() + decreaseColor)));
		int b = Math.min(255, (int)Math.round(c.getBlue() + decreaseColor));
		int r = Math.max(0, (int)Math.round(c.getRed() - decreaseColor));
		Color res =  new Color(r,g,b);
		
		
		return res;
	}
	


}