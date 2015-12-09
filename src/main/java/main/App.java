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
    		br = new BufferedReader(new FileReader("superSimplePrototype.fml"));
    		String ligne;
    		while((ligne = br.readLine()) != null) {
    			fichierALire += ligne;
    		} 
    		br.close();
    	} catch(IOException ioe) {
    		ioe.printStackTrace();
    	}

    	int egalIndex = fichierALire.indexOf("=");
    	String fmName = fichierALire.substring(0,egalIndex).trim();
    	String FM = fichierALire;

    	
    	String configName = "config1";
    	
    	FamiliarInterpreter fi = FamiliarInterpreter.getInstance();
    	
    	try {
			fi.eval(FM);
			FeatureModelVariable fmv = fi.getFMVariable(fmName);
	    	
	    	System.out.println("Instantiated FM : "+fmv.getSyntacticalRepresentation());
	    	
	    	fi.eval(configName+" = configuration "+fmName);
	    	
	    	
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
	        Collection<String> res = fi.getSelectedFeature(configName);
	        for(String st : new String[]{"nEP_5", "nEP_10", "nEP_15"}) {
	        	System.out.println("in for");
	        	if(res.contains(st)) {
	        		Launcher.main(new String[]{st.substring(4)});
	        		break;
	        	}
	        }
	        System.out.println("Grand resultat de Daniel " + res);
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
