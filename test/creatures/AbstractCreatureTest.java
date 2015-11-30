package creatures;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.*;
import org.mockito.Mockito;

import simulator.EnergyPoint;
import creatures.visual.CreatureSimulator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;

import jdk.nashorn.internal.ir.annotations.Ignore;

public class AbstractCreatureTest {
	
	CreatureSimulator environment = mock(CreatureSimulator.class);
	EnergyPoint ep = mock(EnergyPoint.class);
	final double w = 200;
	final double h = 100;
	
	private static final double EPSILON = 0.01;
	ArrayList<ICreature> alIC = new ArrayList<ICreature>();
	AbstractCreature sc = null;
	AbstractCreature smc = null;
	

	@Before
	public void setup() {
		alIC.add(smc);
		//when(environment.creaturesNearByAPoint(sc.getPosition(), 30)).thenReturn(alIC);
		when(environment.getSize()).thenReturn(new Dimension((int)w, (int)h));
		when(ep.getPosition()).thenReturn(new Point2D.Double(1,0));
		sc = new StupidCreature(environment, new Point2D.Double(0, 1), 3, 3, Color.MAGENTA);
		smc = new SmartCreature(environment, new Point2D.Double(0, 0), 3, 3, Color.MAGENTA);
		when(environment.getNearestEnergyPoint(smc)).thenReturn(ep);
		
		
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
	@Test
	public void losingEnergy(){
		double energy = sc.getEnergy();
		sc.act();
		assertEquals(sc.getEnergy(),energy - AbstractCreature.ENERGY_LOST,EPSILON);
	}
	@Test
	public void energyzing(){
		smc.setEnergy(20);
		smc.act();
		Mockito.verify(ep,times(1)).energize(smc);;
	}
	@Ignore
	public void groupeTest(){
		
		double energy = sc.getEnergy();
		sc.act();
		assertEquals(sc.getEnergy(),energy - (AbstractCreature.ENERGY_LOST / 2),EPSILON );
	}

}