package creatures.visual;

import java.awt.*;
import java.awt.geom.*;
import creatures.*;


/**
  * Algorithm for drawing a circular ICreature
  */
public class DrawingCircle implements IDrawStrategy {

	@Override
	public void paint(ICreature c, Graphics2D g2) {
		Point2D pos = c.getPosition();
		int size = c.getSize();
		g2.setColor(c.getColor());
		g2.fillOval((int)pos.getX(), (int)pos.getY(), size/2, size/2);
	}


}