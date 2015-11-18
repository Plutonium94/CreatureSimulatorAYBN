package creatures;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.*;

import creatures.visual.CreatureSimulator;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Point2D;

public class AbstractCreatureTest {
	
	CreatureSimulator environment = mock(CreatureSimulator.class);
	final double w = 200;
	final double h = 100;
	
	private static final double EPSILON = 0.01;
	
	AbstractCreature sc = null;

	@Before
	public void setup() {
		when(environment.getSize()).thenReturn(new Dimension((int)w, (int)h));
		sc = new StupidCreature(environment, new Point2D.Double(0, 0), 3, 3, Color.MAGENTA);
	}
	
	@Test
	public void getSetEnergyTest() {
		assertEquals(AbstractCreature.MAX_ENERGY, sc.getEnergy(), EPSILON);
		sc.setEnergy(15);
		assertEquals(15, sc.getEnergy(), EPSILON);
		sc.setEnergy(-1);
		assertEquals(AbstractCreature.MIN_ENERGY, sc.getEnergy(), EPSILON);
		sc.setEnergy(101);
		assertEquals(AbstractCreature.MAX_ENERGY, sc.getEnergy(), EPSILON);
	}

}
