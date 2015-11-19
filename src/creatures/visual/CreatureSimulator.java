package creatures.visual;

import static commons.Utils.filter;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.*;
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
			res.add(new EnergyPoint(this, new Point2D.Double(Utils.getRandom(-size.getWidth()/2 + 25, size.getWidth()/2-25), Utils.getRandom(-size.getHeight()/2 + 25, size.getHeight()/2 - 25))));
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
		/*ArrayList<ICreature> res = new ArrayList<ICreature>();
		res.add(actionables.get(0));
		return res;*/
		return new ArrayList<ICreature>(actionables);
	}
	
	@Override
	/***
	 * Renvoie une liste fixe des points d'energie reparties au hasard
	 */
	public Iterable<EnergyPoint> getEnergyPoints() {
		return energyPoints;
	}
	
	
	public int creatureSize() {
		return actionables.size();
	}
	
	/***
	 * Returns the position of the nearest energy point of the creature given as argument
	 * @param creature
	 * @return 
	 */
	public EnergyPoint getNearestEnergyPoint(ICreature creature) {
		EnergyPoint nearestEnergyPoint = null;
		double minDistance = Double.POSITIVE_INFINITY;
		Point2D creatureCoordinates = creature.getPosition();
		for(EnergyPoint ep :this.getEnergyPoints()) {
			double newDistance = creatureCoordinates.distance(ep.getPosition());
			if(newDistance < minDistance) {
				minDistance = newDistance;
				nearestEnergyPoint = ep;
			}
		}
		return nearestEnergyPoint;
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
