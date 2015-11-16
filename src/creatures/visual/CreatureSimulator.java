package creatures.visual;

import static commons.Utils.filter;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import simulator.EnergyPoint;
import simulator.Simulator;
import commons.Utils;
import commons.Utils.Predicate;

import creatures.ICreature;
import creatures.IEnvironment;


/**
 * Environment for the creatures together with the visualization facility.
 */
public class CreatureSimulator extends Simulator<ICreature> implements IEnvironment {

	static class CreaturesNearbyPoint implements Predicate<ICreature> {
		private final Point2D point;
		private final double margin;

		public CreaturesNearbyPoint(Point2D point, double margin) {
			this.point = point;
			this.margin = margin;
		}

		@Override
		public boolean apply(ICreature input) {
			return input.distanceFromAPoint(point) <= margin;
		}
	}

	private Dimension size;
	private static final int DEFAULT_NUM_ENERGY_POINTS = 5;
	private int numEnergyPoints = DEFAULT_NUM_ENERGY_POINTS;
	private final ArrayList<EnergyPoint> energyPoints;
	

	public CreatureSimulator(Dimension initialSize) {
		super(new CopyOnWriteArrayList<ICreature>(), 10);
		this.size = initialSize;
		ArrayList<EnergyPoint> res = new ArrayList<>();
		for(int i = 0; i < numEnergyPoints; i++) {
			res.add(new EnergyPoint(this, new Point2D.Double(Utils.getRandom(-size.getWidth()/4 + 25, size.getWidth()/4-25), Utils.getRandom(-size.getHeight()/4 + 25, size.getHeight()/4 - 25))));
		}
		energyPoints= res;
	}
	
	/**
	 * @return a copy of current size
	 */
	public synchronized Dimension getSize() {
		return new Dimension(size);
	}
	
	public synchronized void setSize(Dimension size) {
		this.size = size;
	}
	
	/**
	 * @return a copy of the current creature list.
	 */
	@Override
	public Iterable<ICreature> getCreatures() {
		return new ArrayList<ICreature>(actionables);
	}
	
	@Override
	public Iterable<EnergyPoint> getEnergyPoints() {
		//EnergyPoint centralEnergyPoint = new EnergyPoint(this, new Point2D.Float(0f,0f));
		ArrayList<EnergyPoint> res = new ArrayList<>();
		/*res.add(centralEnergyPoint);
		double[] pointCoordinates = new double[]{-size.getWidth()/8, -size.getHeight()/8, size.getWidth()/8,-size.getHeight()/8, -size.getWidth()/8,size.getHeight()/8, size.getWidth()/8,size.getHeight()/8};  
		for(int i=0; i<pointCoordinates.length-1; i+=2) {
			res.add(new EnergyPoint(this, new Point2D.Double(pointCoordinates[i], pointCoordinates[i+1])));
		}*/
		/*for(int i = 0; i < 5; i++) {
			res.add(new EnergyPoint(this, new Point2D.Double(Utils.getRandom(-size.getWidth()/4, size.getWidth()/4), Utils.getRandom(-size.getHeight()/4, size.getHeight()/4))));
		}*/
		return energyPoints;
	}
	
	
	public int creatureSize() {
		return actionables.size();
	}
	
	public void addCreature(ICreature creature) {
		actionables.add(creature);
	}
	
	public void removeCreature(ICreature creature) {
		actionables.remove(creature);
	}
	
	public Iterable<ICreature> creaturesNearByAPoint(Point2D point,  double radius) {
		return filter(actionables, new CreaturesNearbyPoint(point, radius));
	}

	public void addAllCreatures(Collection<? extends ICreature> creatures) {
		actionables.addAll(creatures);
	}
	
	public void clearCreatures() {
		actionables.clear();
	}

}
