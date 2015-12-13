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
import creatures.*;
import creatures.visual.*;

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

	/* Initialized by fml via main args */
	private Integer nombreDePointsDEnergie = null;
	private IColorStrategy creatureColor = null;
	private Integer creatureNumber = null;
	private CreatureShape creatureShape = CreatureShape.DEFAULT;
	private String creatureType = null;
	
	private PluginMenuItemBuilder menuBuilder;
	private JMenuBar mb = new JMenuBar();	
	private Constructor<? extends ICreature> currentConstructor = null;
	private JPanel conteneur = new JPanel();
	
	
	private JLabel label = new JLabel("abc");
	private JLabel label2 = new JLabel("def");
	private JLabel label3 = new JLabel("gef");
	
	
	
	public Launcher(Map<String,Object> map) {

		if(map != null) {
			this.nombreDePointsDEnergie = (Integer)map.get("nombreDePointsDEnergie");
			this.creatureNumber = (Integer)map.get("creatureNumber");
			String cc = (String)map.get("creatureColor");
			this.creatureColor = (cc == null || cc.equals("hasard"))? new ColorCube(50): new SingleColorStrategy(cc);
			String cs = ((String)map.get("creatureShape")).toUpperCase();
			this.creatureShape = (cs == null)? CreatureShape.DEFAULT: (CreatureShape.valueOf(cs) == null)? CreatureShape.DEFAULT: CreatureShape.valueOf(cs);
			this.creatureType = (String)map.get("creatureType");
			
			
		}

		factory = CreaturePluginFactory.getInstance();
		if(this.creatureType != null) {
			currentConstructor = factory.getConstructorMap().get("creatures." + creatureType);
		}

		
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
					Collection<? extends ICreature> creatures = factory.createCreatures(simulator, (creatureNumber==null)? 10: creatureNumber.intValue(), creatureShape, creatureColor,currentConstructor);
					simulator.addAllCreatures(creatures);
					simulator.start();
				}
			}
		});
		buttons.add(restart);
		
		add(buttons, BorderLayout.SOUTH);
				
		simulator = (nombreDePointsDEnergie == null)?( new CreatureSimulator(new Dimension(640, 480))) : (new CreatureSimulator(new Dimension(640, 480), nombreDePointsDEnergie.intValue() ));	
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
	    label3.setText(" energie totale: " + simulator.getEnergieTotale());
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
			    label3.setText(" energie totale: " + simulator.getEnergieTotale());
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
		this.label3.setText(" energie totale: " + simulator.getEnergieTotale());
		
	}
	
	
	public static void main(String args[]) {
	    Logger.getLogger("plug").setLevel(Level.INFO);
		double myMaxSpeed = 5;
		CreaturePluginFactory.init(myMaxSpeed);

		Launcher launcher = null;
		Map<String,Object> map = new TreeMap<String,Object>();
		for(String arg : args) {
			if(arg.startsWith("ep_")) {
				map.put("nombreDePointsDEnergie",Integer.parseInt(arg.split("_")[1]));
			}
			if(arg.startsWith("cc_")) {
				System.out.println(Arrays.toString(arg.split("_")));
				map.put("creatureColor",arg.split("_")[1]);
			}
			if(arg.startsWith("cn_")) {
				map.put("creatureNumber", Integer.parseInt(arg.split("_")[1]));
			}
			if(arg.startsWith("cs_")) {
				map.put("creatureShape", arg.split("_")[1]);
			}
			if(arg.startsWith("ct_")) {
				map.put("creatureType", arg.split("_")[1]);
			}
			
			

		}
		launcher = new Launcher(map);
		launcher.setVisible(true);
	}


	
}


