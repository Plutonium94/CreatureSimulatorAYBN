package creatures;

import static commons.Utils.filter;
import static commons.Utils.mkString;
import static java.lang.Math.PI;
import static java.lang.Math.atan;
import static java.lang.Math.toDegrees;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

import commons.Utils.Predicate;
import creatures.SmartCreature.CreaturesAroundCreature;
import creatures.visual.*;

/***
 * @author Daniel
 */
public abstract class AbstractCreature implements ICreature {

	public static final int DEFAULT_SIZE = 80;
	public static final int DEFAULT_VISION_DISTANCE = 50;
	public static final double MAX_ENERGY = 100;
	public static final double MIN_ENERGY = 0;
	public static final double ENERGY_LOST = 0.05;
	

	/**
	 * The field of view (FOV) is the extent of the observable world that is
	 * seen at any given moment by a creature in radians.
	 */
	protected double fieldOfView = (PI / 4);

	/**
	 * The distance indicating how far a creature see in front of itself in
	 * pixels.
	 */
	protected double visionDistance = DEFAULT_VISION_DISTANCE;

	/** Position */
	protected Point2D position;

	/** Speed in pixels */
	protected double speed;

	/** Direction in radians (0,2*pi) */
	protected double direction;

	/** Color of the creature */
	protected Color color;

	/** Shape of the creature */
	protected CreatureShape forme = CreatureShape.DEFAULT;

	/** Reference to the environment */
	protected final IEnvironment environment;

	/** Size of the creature in pixels */
	protected final int size = DEFAULT_SIZE;
	protected double energy = MAX_ENERGY ;
	protected boolean isalive = true ;

	public AbstractCreature(IEnvironment environment, Point2D position) {
		this.environment = environment;

		setPosition(position);
	}

	public AbstractCreature(IEnvironment environment, Point2D position, CreatureShape forme) {
		this(environment, position);
		this.forme = forme;
	}
	
	/*public AbstractCreature(IEnvironment environment, Point2D position, double speed, double direction, Color color) {
		this(environment, position);
		this.speed = speed;
		this.direction = direction;
		this.color = color;
	}*/

	// ----------------------------------------------------------------------------
	// Getters and Setters
	// ----------------------------------------------------------------------------

	@Override
	public IEnvironment getEnvironment() {
		return environment;
	}
	
	public double getFieldOfView() {
		return fieldOfView;
	}

	public double getLengthOfView() {
		return visionDistance;
	}

	@Override
	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	@Override
	public double getDirection() {
		return direction;
	}

	public void setDirection(double direction) {
		this.direction = direction % (PI * 2);
	}

	@Override
	public Color getColor() {
		return color;
	}


	@Override
	public int getSize() {
		return size;
	}
	
	@Override
	public double getEnergy(){
		return energy;
	}

	public void setForme(CreatureShape forme) {
		this.forme = forme;
	}
	
	@Override
	public void setEnergy( double energy){
		CreatureSimulator env = (CreatureSimulator)environment;
		if(energy <= MIN_ENERGY) {
			this.energy = MIN_ENERGY;
			env.removeCreature(this);
			
		} else if(energy >= MAX_ENERGY) {
			
			this.energy = MAX_ENERGY;
			
		} else {
			this.energy = energy;
		}
		
	}
	public boolean isAlive(){
		return isalive ;
	}
	public void setAlive(boolean isalive){
		this.isalive = isalive;
	}

	@Override
	public Point2D getPosition() {
		return new Point2D.Double(position.getX(), position.getY());
	}

	public void setPosition(Point2D position) {
		setPosition(position.getX(), position.getY());
	}

	public void setPosition(double x, double y) {
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

	// ----------------------------------------------------------------------------
	// Positioning methods
	// ----------------------------------------------------------------------------

	protected void move(double incX, double incY) {
		setPosition(position.getX() + incX, position.getY() + incY);
		setEnergy(getEnergy() - getLostEnergy());
		
	}

	protected void rotate(double angle) {
		this.direction += angle;
	}

	// ----------------------------------------------------------------------------
	// Methods for calculating the direction
	// ----------------------------------------------------------------------------

	/**
	 * Computes the direction between the given point {@code (x1, y1)} and the
	 * current position in respect to a given {@code axis}.
	 * 
	 * @return direction in radians between given point and current position in
	 *         respect to a given {@code axis}.
	 */
	@Override
	public double directionFormAPoint(Point2D p, double axis) {
		double b = 0d;

		// use a inverse trigonometry to get the angle in an orthogonal triangle
		// formed by the points (x,y) and (x1,y1)
		if (position.getX() != p.getX()) {
			// if we are not in the same horizontal axis
			b = atan((position.getY() - p.getY()) / (position.getX() - p.getX()));
		} else if (position.getY() < p.getY()) {
			// below -pi/2
			b = -PI / 2;
		} else {
			// above +pi/2
			b = PI / 2;
		}

		// make a distinction between the case when the (x1, y1)
		// is right from the (x,y) or left
		if (position.getX() < p.getX()) {
			b += PI;
		}

		// align with the axis of the origin (x1,y1)
		b = b - axis;

		// make sure we always take the smaller angle
		// keeping the range between (-pi, pi)
		if (b >= PI)
			b = b - PI * 2;
		else if (b < -PI)
			b = b + PI * 2;

		return b % (PI * 2);
	}

	/**
	 * Distance between the current position and a given point {@code(x1, y1)}.
	 * 
	 * @return distance between the current position and a given point.
	 */
	@Override
	public double distanceFromAPoint(Point2D p) {
		return getPosition().distance(p);
	}

	// ----------------------------------------------------------------------------
	// Painting
	// ----------------------------------------------------------------------------

	@Override
	public void paint(Graphics2D g2) {
		if(forme == CreatureShape.SQUARE) {
			paintSquare(g2);
		} else if(forme == CreatureShape.CIRCLE) {
			paintCircle(g2);
		} else {
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
			g2.fillArc(0, 0, size, size, (int) toDegrees(-fieldOfView / 2),
					(int) toDegrees(fieldOfView));
			
			//g2.setColor(Color.BLACK);
		}
	}

	private void paintSquare(Graphics2D g2) {
		g2.setColor(color);
		g2.fillRect((int)position.getX(),(int)position.getY(),size/2, size/2);
	}

	private void paintCircle(Graphics2D g2) {
		g2.setColor(color);
		g2.fillOval((int)position.getX(),(int)position.getY(), size/2, size/2);
	}

	// ----------------------------------------------------------------------------
	// Description
	// ----------------------------------------------------------------------------

	public String toString() {
		Class<?> cl = getClass();

		StringBuilder sb = new StringBuilder();
		sb.append(getFullName(cl));
		sb.append("\n---\n");
		sb.append(mkString(getProperties(cl), "\n"));

		return sb.toString();
	}

	private List<String> getProperties(Class<?> clazz) {
		List<String> properties = new ArrayList<String>();

		Iterable<Field> fields = filter(
				Arrays.asList(clazz.getDeclaredFields()),
				new Predicate<Field>() {
					@Override
					public boolean apply(Field input) {
						return !Modifier.isStatic(input.getModifiers());
					}
				});

		for (Field f : fields) {
			String name = f.getName();
			Object value = null;

			try {
				value = f.get(this);
			} catch (IllegalArgumentException e) {
				value = "unable to get value: " + e;
			} catch (IllegalAccessException e) {
				value = "unable to get value: " + e;
			} finally {
				properties.add(name + ": " + value);
			}
		}

		Class<?> superclass = clazz.getSuperclass();
		if (superclass != null) {
			properties.addAll(getProperties(superclass));
		}

		return properties;
	}

	private String getFullName(Class<?> clazz) {
		String name = clazz.getSimpleName();
		Class<?> superclass = clazz.getSuperclass();
		if (superclass != null) {
			return getFullName(superclass) + " > " + name;
		} else {
			return name;
		}
	}
	
	public String getName() {
		return getClass().getName();
	}
	public Iterable<ICreature> creaturesAround(
			AbstractCreature creature) {
		return filter(environment.getCreatures(), new CreaturesAroundCreature(this));
	}
	
	public double getLostEnergy(){
		Iterable<ICreature> creatures = environment.creaturesNearByAPoint(this.getPosition(), 30);
		int nbCreature = 1 ;
		if(creatures == null){
			return ENERGY_LOST ;
		}
		for (ICreature c : creatures) {
			nbCreature++;
		}
		return (double)(ENERGY_LOST / nbCreature);
		
	}
	public void energySearch(Point2D nearestEnergyPointCoordinates){
		this.setDirection(this.directionFormAPoint(nearestEnergyPointCoordinates, Math.PI));
		double incX = speed * cos(direction);
		double incY = speed * sin(direction);
		move(incX, incY);
	}
	
}
