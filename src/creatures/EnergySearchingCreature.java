package creatures;

import java.awt.geom.Point2D;

import creatures.visual.CreatureSimulator;

public class EnergySearchingCreature extends AbstractCreature {

	public EnergySearchingCreature(IEnvironment environment, Point2D position) {
		super(environment, position);
	}

	@Override
	public void act() {
		if(this.getEnergy() <= 40) {
			CreatureSimulator environment = (CreatureSimulator)getEnvironment();
			Point2D nearestEnergyPointCoordinates = environment.getNearestEnergyPointCoordinates(this);
			this.setDirection(this.directionFormAPoint(nearestEnergyPointCoordinates, Math.PI));
			this.move(2,2);
		} else {
			this.move(3, 3);
		}
		this.setEnergy(this.getEnergy()-10);
	}

}
