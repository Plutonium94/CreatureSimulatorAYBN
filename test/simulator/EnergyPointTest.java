package simulator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.*;
import org.mockito.Mockito;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.ArrayList;

import creatures.*;
import creatures.visual.CreatureSimulator;

public class EnergyPointTest {
	
	CreatureSimulator environment = mock(CreatureSimulator.class);
	EnergyPoint ep1 = null;
	StupidCreature sc = null;
	ArrayList<EnergyPoint> energyPointList = null;
	double[] initialPositionCoordinates = {0,3, 1,7, 3,10, 6,8 };
	final double w = 200;
	final double h = 100;
	
	@BeforeClass 
	public static void beforeClassSetup() {
		
	}
	
	@Before
	public void setup() {
		when(environment.getSize()).thenReturn(new Dimension((int)w,(int)h));
		
		energyPointList = new ArrayList<EnergyPoint>();
		sc = new StupidCreature(environment,new Point2D.Double(0, 0), Math.PI, 3, Color.GREEN );
		for(int i=0; i < initialPositionCoordinates.length; i+=2) {
			energyPointList.add(new EnergyPoint(environment, new Point2D.Double(initialPositionCoordinates[i], initialPositionCoordinates[i+1])));
		}
		when(environment.getEnergyPoints()).thenReturn(energyPointList);
		
		ep1 = new EnergyPoint(environment,new Point2D.Double(30, 40));
	}

	@Ignore
	public void testEnergyPoint() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetSize() {
		assertEquals(ep1.getSize(), EnergyPoint.size);
	}

	@Test
	/***
	 * Test if energy point position remains constant after multiple simulations
	 */
	public void testGetPosition() {
		Point2D initialPosition = ep1.getPosition();
		for(int i=0; i < 4; i++) {
			environment.simulate();
			assertEquals(initialPosition, ep1.getPosition());
		}
	}
	
	@Test
	public void testGetEnergy() {
		assertEquals( EnergyPoint.DEFAULT_ENERGY, ep1.getEnergy());
		ep1.setEnergy(7);
		assertEquals(7, ep1.getEnergy());
	}
	
	@Ignore
	public void testNearestEnergyPoint() {
		assertEquals(4, energyPointList.size());
		assertNotNull(environment);
		System.out.println(environment);
		System.out.println(environment.getNearestEnergyPointCoordinates(sc));
		assertNotNull(sc);
		EnergyPoint nearestEnergyPoint = null;
		double minDistance = Double.POSITIVE_INFINITY;
		for(EnergyPoint ept : environment.getEnergyPoints()) {
			double dist = sc.getPosition().distance(ept.getPosition());
			if(dist < minDistance) {
				minDistance = dist;
				nearestEnergyPoint = ept;
			}
		}
		System.out.println(nearestEnergyPoint.getPosition());
		assertEquals(4, ((ArrayList<EnergyPoint>)environment.getEnergyPoints()).size());
		assertEquals(energyPointList.get(0).getPosition(), environment.getNearestEnergyPointCoordinates(sc));
	}

}
