package creatures;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

import java.awt.Color;
import java.awt.geom.Point2D;


/**
 * Stupid creature
 */
public class StupidCreature extends AbstractCreature {

	protected int currCycle = 0;

	private static final int NUMBER_OF_CYCLES_PER_CHANGE = 4;


	public StupidCreature(IEnvironment environment, Point2D position,
			double direction, double speed, Color color) {
		super(environment, position);
		
		this.direction = direction;
		this.speed = speed;
		this.color = color;
	}

	public void act() {
		applyNoise();
		double incX = speed * cos(direction);
		double incY = speed * sin(direction);
	
		move(incX, incY);
	}

	public void applyNoise() {
		currCycle++;
		currCycle %= NUMBER_OF_CYCLES_PER_CHANGE;

		// every NUMBER_OF_CYCLES_PER_CHANGE we do the change
		if (currCycle == 0) {
			this.speed += ((Math.random() * 2) - 1);

			// maintain the speed within some boundaries
			if (this.speed > 2) {
				this.speed = 2;
			}

			setDirection(this.direction
					+ ((Math.random() * Math.PI / 2) - (Math.PI / 4)));
		}
	}
}
