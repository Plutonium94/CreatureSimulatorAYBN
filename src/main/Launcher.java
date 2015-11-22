package main;

import java.awt.BorderLayout;
import java.util.*;
import java.awt.Dimension;
import java.awt.event.*;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import plug.creatures.CreaturePluginFactory;
import plug.creatures.PluginMenuItemBuilder;
import simulator.*;
import creatures.ICreature;
import creatures.visual.ColorCube;
import creatures.visual.CreatureInspector;
import creatures.visual.CreatureSimulator;
import creatures.visual.CreatureVisualizer;

/**
 * Just a simple test of the simulator.
 * 
 */
@SuppressWarnings("serial")
public class Launcher extends JFrame {

	private int nbVivants = 0;
	private int nbMorts = 0;
	private double energieTotale = 0.0;
	
	private final CreaturePluginFactory factory;
	
	private final CreatureInspector inspector;
	private final CreatureVisualizer visualizer;
	private final CreatureSimulator simulator;
	
	private PluginMenuItemBuilder menuBuilder;
	private JMenuBar mb = new JMenuBar();	
	private Constructor<? extends ICreature> currentConstructor = null;
	private JPanel conteneur = new JPanel();
	
	
	private JLabel label = new JLabel("abc");
	private JLabel label2 = new JLabel("def");
	private JLabel label3 = new JLabel("gef");
	
	
	
	public Launcher() {
		factory = CreaturePluginFactory.getInstance();
		
		conteneur.setLayout(new BoxLayout(conteneur, BoxLayout.Y_AXIS));
		conteneur.add(label);
		conteneur.add(label2);
		conteneur.add(label3);
		add(conteneur, BorderLayout.EAST);
		
		
		
		
		

		setName("Creature Simulator Plugin Version");

		

		//setLayout(new BorderLayout());


		JPanel buttons = new JPanel();
		JButton loader = new JButton("Load plugins");
		loader.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				factory.load();
				buildPluginMenus();
			}
		});
		buttons.add(loader);

		JButton reloader = new JButton("Reload plugins");
		reloader.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				factory.reload();
				buildPluginMenus();
			}
		});
		buttons.add(reloader);

		JButton restart = new JButton("(Re-)start simulation");
		restart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (currentConstructor != null) {
					synchronized(simulator) {
						if (simulator.isRunning()) {
							simulator.stop();
						}
					}
					simulator.clearCreatures();
					Collection<? extends ICreature> creatures = factory.createCreatures(simulator, 10, new ColorCube(50),currentConstructor);
					simulator.addAllCreatures(creatures);
					simulator.start();
				}
			}
		});
		buttons.add(restart);
		
		add(buttons, BorderLayout.SOUTH);
				
		simulator = new CreatureSimulator(new Dimension(640, 480));	
		simulator.setLaucher(this);
		inspector = new CreatureInspector();
		inspector.setFocusableWindowState(false);
		visualizer = new CreatureVisualizer(simulator);
		visualizer.setDebug(false);
		visualizer.setPreferredSize(simulator.getSize());
		
		
		add(visualizer, BorderLayout.CENTER);
		
		
	
	    buildPluginMenus();

	    pack();
	    label.setText("number of creatures alive : " +  simulator.countCreaturesalive());
	    label2.setText(" number of dead creatures  : " + simulator.countCreaturesdead());
	    label3.setText(" energie moyenne: " + simulator.getEnergieMoyenne());
	    pack();
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				exit(evt);
			}
		});
		
		
		
	}
	
	private void exit(WindowEvent evt) {
		System.exit(0);
	}
	
	

	public void buildPluginMenus() {	
		mb.removeAll();
		ActionListener listener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// the name of the plugin is in the ActionCommand
			
				currentConstructor = factory.getConstructorMap().get(((JMenuItem) e.getSource()).getActionCommand());
				label.setText("number of creatures alive : " +  simulator.countCreaturesalive());
			    label2.setText(" number of dead creatures  : " + simulator.countCreaturesdead());
			    label3.setText(" energie moyenne: " + simulator.getEnergieMoyenne());
			}   
		};
		//if(true) throw new RuntimeException("oh yea :::::::: " + factory.getConstructorMap());

		menuBuilder = new PluginMenuItemBuilder(factory.getConstructorMap(),listener);
		menuBuilder.setMenuTitle("Creatures");
		menuBuilder.buildMenu();
		mb.add(menuBuilder.getMenu());
		setJMenuBar(mb);
	}
	
	public void updateStats() {
		this.label.setText("Nb vivants : " + simulator.countCreaturesalive());
		this.label2.setText("Nb morts : "  + simulator.countCreaturesdead());
		label3.setText(" energie moyenne: " + simulator.getEnergieMoyenne());
		
	}
	
	
	public static void main(String args[]) {
	    Logger.getLogger("plug").setLevel(Level.INFO);
		double myMaxSpeed = 5;
		CreaturePluginFactory.init(myMaxSpeed);
		Launcher launcher = new Launcher();
		launcher.setVisible(true);
	}
	
}


