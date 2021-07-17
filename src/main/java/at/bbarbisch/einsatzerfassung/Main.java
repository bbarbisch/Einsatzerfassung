package at.bbarbisch.einsatzerfassung;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import at.bbarbisch.einsatzerfassung.config.Config;
import at.bbarbisch.einsatzerfassung.config.ConfigException;
import at.bbarbisch.einsatzerfassung.config.ConfigParser;
import at.bbarbisch.einsatzerfassung.data.DataHandler;
import at.bbarbisch.einsatzerfassung.gui.MainFrame;

public class Main 
{
    public static void main( String[] args ) {
    	// load configuration
    	var configPath = Paths.get("config.xml");
    	if(!Files.exists(configPath)) {
    		firstRun(configPath);
    		return;
    	}
    	
    	// parse configuration
    	var configParser = new ConfigParser();
    	Config config;
    	try {
			config = configParser.parse(configPath);
		} catch (ConfigException e) {
			var mainFrame = new MainFrame(null, new Config());
			mainFrame.showErrorDialog("Fehler beim Einlesen der Konfiguration:\n" + e.getMessage());
			return;
		}
    	
    	// run
		var dataHandler = new DataHandler(config);
		var mainFrame = new MainFrame(dataHandler, config);
		mainFrame.run();
    }
    
    private static void firstRun(Path configPath) {
    	// create config file from template
		var configTemplate = Main.class.getResourceAsStream("/configuration.xml.template");
		try {
			Files.copy(configTemplate, configPath);
		} catch (IOException e) {
			System.out.println("Could not create the config.xml file: " + e.getMessage());
		}
		
		// write out ico file
		var logo = Main.class.getResourceAsStream("/brdLogo.ico");
		try {
			Files.copy(logo, Paths.get("brdLogo.ico"));
		} catch (IOException e) {
			System.out.println("Could not create the brdLogo.ico file: " + e.getMessage());
		}
		
		// show message
		var mainFrame = new MainFrame(null, new Config());
		mainFrame.showConfirmDialog("Info", String.format("Keine Konfiguration vorhanden.\nBitte die erstellte Konfiguration unter '%s' korrekt ausfüllen!", configPath.toAbsolutePath().toString()));
    }
}
