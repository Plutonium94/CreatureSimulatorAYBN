package creatures.visual;

import java.awt.*;
import java.awt.geom.*;
import creatures.*;


/**
  * Algorithm for drawing an AbstractCreature shaped as a sector (slice of a pizza or area between 2 radii of a circle)
  */
public class DrawingSector implements IDrawStrategy {

	@Override
	public void paint(ICreature c, Graphics2D g2) {
		AbstractCreature ac = (AbstractCreature)c;
		int size = ac.getSize();
		Point2D position = ac.getPosition();
		double fieldOfView = ac.getFieldOfView();
		double direction = ac.getDirection();
		Color color = ac.getColor();

		//g2.fillOval((int)position.getX(), (int) position.getY(), 14, 14);
		// center the point
		g2.translate(position.getX(), position.getY());
		// center the surrounding rectangle
		g2.translate(-size / 2, -size / 2);
		// center the arc
		// rotate towards the direction of our vector
		g2.rotate(-direction, size / 2, size / 2);

		// useful for debugging
		//g2.drawRect(0, 0, size, size);

		// set the color
		g2.setColor(color);
		// we need to do PI - FOV since we want to mirror the arc
		g2.fillArc(0, 0, size, size, (int) Math.toDegrees(-fieldOfView / 2),
				(int) Math.toDegrees(fieldOfView));
		
		//g2.setColor(Color.BLACK);
	}


}
