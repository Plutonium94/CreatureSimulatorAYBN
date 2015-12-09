package simulator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.*;

import creatures.ICreature;
import creatures.IEnvironment;
import visual.*;

/***
 * @author Plutonium94
 */
public class EnergyPoint implements IDrawable {
	
	protected final Point2D position;
	
	protected final IEnvironment environment;
	
	protected static final int DEFAULT_SIZE = 40;

	protected int size = DEFAULT_SIZE;
	
	public static final int DEFAULT_ENERGY = 50;
	
	private int energy = DEFAULT_ENERGY;
	
	private static final Color color = new Color(255,165,0,128);
	
	public EnergyPoint(IEnvironment environment, Point2D position) {
		this.environment = environment;
		
		double x = position.getX();
		double y = position.getY();
		Dimension s = environment.getSize();
		
		if (x > s.getWidth() / 2) {
			x = -s.getWidth() / 2;
		} else if (x < -s.getWidth() / 2) {
			x = s.getWidth() / 2;
		}

		if (y > s.getHeight() / 2) {
			y = -s.getHeight() / 2;
		} else if (y < -s.getHeight() / 2) {
			y = s.getHeight() / 2;
		}

		this.position = new Point2D.Double(x, y);
	}

	public EnergyPoint(IEnvironment environment, Point2D position, int size, int power) {
		this(environment, position);
		this.size = size;
		this.energy = power;
	}
	
	@Override
	public void paint(Graphics2D g2) {
		int largeur = environment.getSize().width; int hauteur = environment.getSize().height;
		g2.drawRect(-largeur/2, -hauteur/2, largeur-3, hauteur-3);
		//g2.translate(position.getX(), position.getY());
		g2.translate(-size / 2, -size / 2);
		g2.setColor(color);

		g2.fillOval((int)position.getX(), (int)position.getY(), size, size);
		//g2.fillOval(0, 0, size, size);
		
	}
	
	
	public boolean isInVicinity(ICreature creature) {
		return getPosition().distance(creature.getPosition()) <= getSize()/2;
	}
	
	public void energize(ICreature creature) {
		creature.setEnergy(creature.getEnergy() + this.getEnergy());
		java.awt.Toolkit.getDefaultToolkit().beep();
	}
	
	@Override
	public Color getColor() {
		return color;
	}
	
	
	public int getEnergy() {
		return energy;
	}
	
	public void setEnergy(int energy) {
		this.energy = energy;
	}
	
	
	
	@Override
	public int getSize() {
		return size;
	}
	
	public Point2D getPosition() {
		return position;
	}
	
	
	@Override
	public String toString() {
		return "EnergyPoint";
	}
	


}
