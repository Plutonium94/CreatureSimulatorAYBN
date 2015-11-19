package simulator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.*;
import java.awt.*;
import java.awt.geom.Point2D;
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
	
	private static final int ep1_x = 30;
	private static final int ep1_y = 40;
	
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
		
		ep1 = new EnergyPoint(environment,new Point2D.Double(ep1_x, ep1_y));
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
	 * Tests if a creature is really in the vicinity of an EnergyPoint
	 */
	public void testIsInVicity() {
		sc.setPosition(new Point2D.Double(ep1_x, ep1_y - EnergyPoint.size/2 - 1));
		assertFalse(ep1.isInVicinity(sc));
		sc.setPosition(new Point2D.Double(ep1_x, ep1_y - EnergyPoint.size/2));
		assertTrue(ep1.isInVicinity(sc));
		sc.setPosition(new Point2D.Double(ep1_x - EnergyPoint.size/2 - 1, ep1_y));
		assertFalse(ep1.isInVicinity(sc));
		sc.setPosition(new Point2D.Double(ep1_x - EnergyPoint.size/2, ep1_y));
		assertTrue(ep1.isInVicinity(sc));	
	}
	
	@Test
	public void energizeTest() {
		double reducedEnergy = 35;
		sc.setEnergy(reducedEnergy);
		ep1.energize(sc);
		assertEquals( ep1.getEnergy() + reducedEnergy, sc.getEnergy(), 0.01 );
		reducedEnergy = AbstractCreature.MAX_ENERGY - ep1.getEnergy() - 1;
		sc.setEnergy(reducedEnergy);
		ep1.energize(sc);
		assertNotEquals(AbstractCreature.MAX_ENERGY, sc.getEnergy(), 0.01);
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
	
	

}
