package main;

import java.util.*;
import java.util.List;
import java.io.*;

import fr.unice.deptinfo.familiar_interpreter.FMEngineException;
import fr.unice.deptinfo.familiar_interpreter.impl.FamiliarInterpreter;
import fr.unice.polytech.modalis.familiar.interpreter.VariableNotExistingException;
import fr.unice.polytech.modalis.familiar.parser.VariableAmbigousConflictException;
import fr.unice.polytech.modalis.familiar.variable.FeatureModelVariable;

// App.java
public class App 
{
    public static void main( String[] args )
    {
    	System.out.println(System.getProperty("user.dir"));
    	//if(true)return;

    	BufferedReader br = null;
    	String fichierALire = "";
    	try {
    		br = new BufferedReader(new FileReader("basket.fml"));
    		String ligne;
    		while((ligne = br.readLine()) != null) {
    			fichierALire += ligne;
    		} 
    		br.close();
    	} catch(IOException ioe) {
    		ioe.printStackTrace();
    	}

    	int egalIndex = fichierALire.indexOf("=");
    	String fmName = "fmDom", fmTechName = "fmTech";
    	String FM = fichierALire;

    	
    	String configName = "confDom";
    	
    	FamiliarInterpreter fi = FamiliarInterpreter.getInstance();
    	
    	try {
			fi.eval(FM);
			FeatureModelVariable fmv = fi.getFMVariable(fmName);
	    	
	    	fi.eval(configName+" = configuration "+fmName);
	    	System.out.println("Instantiated FM : "+fmv.getSyntacticalRepresentation());
	    	
	    	
	    	
	        Scanner scan = new Scanner(System.in);
	        String s = "";
	        String selectCmd = "select ";
	        
	        do {
	        	System.out.println("Enter the name of features you wish to select, or type exit to exit.");
	        	s = scan.nextLine();
	        	if (!s.equals("exit")) {
		        	fi.eval(selectCmd+s+" in "+configName);
		        	System.out.println("Selected features :"+fi.getSelectedFeature(configName));
		        	System.out.println("Deselected features :"+fi.getDeselectedFeature(configName));
		        	System.out.println("Unselected features :"+fi.getUnselectedFeature(configName));
	        	}
	        } while (!s.equals("exit"));
	        String fmConfigDomName = "fmConfigDom";
	        fi.eval(fmConfigDomName +" = asFM " + configName);
	        
	        String fmSimuName = "fmSimu";
	        fi.eval(fmSimuName + " = aggregate { " + fmConfigDomName +" "+ fmTechName+ " } withMapping regles" );

	        String configSimuName = "configSimu";
	        fi.eval(configSimuName + " = configuration " + fmSimuName);

	        String fmResName = "fmRes";
	        fi.eval(fmResName + " = asFM " + configSimuName);

	        Properties prop = new Properties();
	        Collection<String> res = fi.getSelectedFeature(configSimuName);
	        List<String> launcherMainArgsListe = new ArrayList<String>();
	        for(String st : res) {
	        	if(st.contains("_")) {
	        		String[] splitValeur = st.split("_");
	        		prop.setProperty(splitValeur[0],splitValeur[1]);
	        		launcherMainArgsListe.add(st);
	        	}

	        }

	        System.out.println("Petit resultat de Daniel " + res);
	        System.out.println("prop : " + prop);
	        Launcher.main(launcherMainArgsListe.toArray(new String[launcherMainArgsListe.size()]));
	        
		} catch (FMEngineException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (VariableNotExistingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (VariableAmbigousConflictException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
}
