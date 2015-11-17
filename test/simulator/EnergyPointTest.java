package simulator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.*;
import org.mockito.Mockito;
import java.awt.*;
import java.awt.geom.Point2D;

import creatures.visual.CreatureSimulator;

public class EnergyPointTest {
	
	CreatureSimulator environment = mock(CreatureSimulator.class);
	EnergyPoint ep1 = null;
	final double w = 200;
	final double h = 100;
	
	@Before
	public void setup() {
		when(environment.getSize()).thenReturn(new Dimension((int)w,(int)h));
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

}
