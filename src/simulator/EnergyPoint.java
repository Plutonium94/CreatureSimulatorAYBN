package simulator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.*;

import creatures.IEnvironment;
import visual.*;

public class EnergyPoint implements IDrawable {
	
	protected final Point2D position;
	
	protected final IEnvironment environment;
	
	protected static final int size = 50;
	
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
	
	@Override
	public void paint(Graphics2D g2) {
		g2.translate(position.getX(), position.getY());
		g2.translate(-size / 2, -size / 2);
		g2.setColor(color);
		g2.fillOval((int)position.getX(), (int)position.getY(), size, size);
	}
	
	@Override
	public Color getColor() {
		return color;
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
