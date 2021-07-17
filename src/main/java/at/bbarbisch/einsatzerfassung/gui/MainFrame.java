package at.bbarbisch.einsatzerfassung.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Path;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import at.bbarbisch.einsatzerfassung.config.Config;
import at.bbarbisch.einsatzerfassung.data.DataException;
import at.bbarbisch.einsatzerfassung.data.DataHandler;

public class MainFrame {
	private Config config;
	private DataHandler dataHandler;
	private JFrame mainFrame;
	private JComboBox<String> typeSelection;
	private JTextField dateTextField;
	private JTextField nameTextField;
	
	public MainFrame(DataHandler dataHandler, Config config) {
		this.dataHandler = dataHandler;
		this.config = config;
		
		createGui();
	}
	
	private void createGui() {
		// general initialization
		mainFrame = new JFrame();
		mainFrame.getContentPane().setLayout(new FlowLayout(1, 0, 0));
		mainFrame.setDefaultCloseOperation(2);
		mainFrame.setTitle("Einsatzerfassung");
		mainFrame.setName("einsatzFrame");
		mainFrame.getAccessibleContext().setAccessibleName("Einsatzdokumentation");
		
		// create settings panel
		var settingsPanel = new JPanel();
		settingsPanel.setLayout(new GridBagLayout());
		
		var labelCreateNew = new JLabel();
		labelCreateNew.setText("Neuer Einsatz anlegen");
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.anchor = 17;
		gridBagConstraints.insets = new Insets(5, 10, 5, 10);
		settingsPanel.add(labelCreateNew, gridBagConstraints);
		
		typeSelection = new JComboBox<String>();
		for(var type : config.getTypes()) {
			typeSelection.addItem(type);
		}
		typeSelection.setMaximumSize(new Dimension(500, 19));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.anchor = 17;
		gridBagConstraints.insets = new Insets(5, 10, 5, 10);
		settingsPanel.add(typeSelection, gridBagConstraints);
		
		dateTextField = new JTextField();
		var dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		dateTextField.setText(dateTimeFormatter.format(ZonedDateTime.now()));
		dateTextField.setMaximumSize(new Dimension(300, 20));
		dateTextField.setMinimumSize(new Dimension(150, 20));
		dateTextField.setPreferredSize(new Dimension(150, 20));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = 17;
		gridBagConstraints.insets = new Insets(5, 10, 5, 10);
		settingsPanel.add(dateTextField, gridBagConstraints);
		
		nameTextField = new JTextField();
		nameTextField.setMinimumSize(new Dimension(150, 20));
		nameTextField.setPreferredSize(new Dimension(150, 20));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = 17;
		gridBagConstraints.insets = new Insets(5, 10, 5, 10);
		settingsPanel.add(nameTextField, gridBagConstraints);
		
		var okButton = new JButton();
		okButton.setText("Ok");
		okButton.setFocusCycleRoot(true);
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if(checkData()) {
					try {
						var type = (String)typeSelection.getSelectedItem();
						var dateParts = dateTextField.getText().split("\\.");
						var date = dateParts[2] + dateParts[1] + dateParts[0];
						var description = nameTextField.getText();
						var path = dataHandler.createDataStructure(type, date, description);
						openDirectory(path);
						close();
					} catch (DataException e) {
						showErrorDialog(e.getMessage());
					}
				}
			}
		});
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = 13;
		gridBagConstraints.insets = new Insets(5, 10, 5, 10);
		settingsPanel.add(okButton, gridBagConstraints);
		
		var cancelButton = new JButton();
		cancelButton.setText("Abbrechen");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				close();
			}
		});
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = 17;
		gridBagConstraints.insets = new Insets(5, 10, 5, 10);
		settingsPanel.add(cancelButton, gridBagConstraints);
		
		var dateLabel = new JLabel();
		dateLabel.setText("Datum");
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = 17;
		gridBagConstraints.insets = new Insets(5, 10, 5, 10);
		settingsPanel.add(dateLabel, gridBagConstraints);
		
		var nameLabel = new JLabel();
		nameLabel.setText("Einsatzname");
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = 17;
		gridBagConstraints.insets = new Insets(5, 10, 5, 10);
		settingsPanel.add(nameLabel, gridBagConstraints);
		
		// create logo panel
		var logoPanel = new JPanel();
		logoPanel.setBackground(new Color(0, 93, 46));
		logoPanel.setAlignmentX(0.0F);
		logoPanel.setAlignmentY(0.0F);
		var logoLabel = new JLabel();
		logoLabel.setIcon(new ImageIcon(getClass().getResource("/brdLogo.gif")));
		logoPanel.add(logoLabel);
		
		// wire everything together
		var scrollPanel = new JScrollPane();
		scrollPanel.setViewportView(settingsPanel);
		logoPanel.add(scrollPanel);
		mainFrame.getContentPane().add(logoPanel);
		mainFrame.pack();
	}
	
	public void showErrorDialog(String message) {
		showConfirmDialog("Fehler", message);
	}
	
	public void showConfirmDialog(String title, String message) {
		JOptionPane.showConfirmDialog(mainFrame, message, title, -1, 0);
	}
	
	public void run() {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		double width = toolkit.getScreenSize().getWidth();
		double height = toolkit.getScreenSize().getHeight();
		mainFrame.setLocation((int) (width / 2.0D) - mainFrame.getWidth() / 2,
				(int) (height / 2.0D) - mainFrame.getHeight() / 2 - 100);
		mainFrame.setVisible(true);
	}

	public void close() {
		mainFrame.setVisible(false);
		mainFrame.dispose();
	}

	private boolean checkData() {
		var dateString = this.dateTextField.getText();
		if(dateString.length() < 1) {
			showErrorDialog("Bitte gib ein Datum ein!");
			return false;
		}
		if(this.nameTextField.getText().length() < 1) {
			showErrorDialog("Bitte gib einen Namen ein!");
			return false;
		}
		if(!dateString.matches("\\d{2}\\.\\d{2}\\.\\d{4}")) {
			showErrorDialog("Bitte gib einen gültiges Datum ein (Format dd.mm.yyyy)!");
			return false;
		}
		return true;
	}

	private void openDirectory(Path path) {
		try {
			var cmd = String.format("explorer \"%s\"", path.toString());
			Runtime.getRuntime().exec(cmd);
		} catch (Exception e) {
			// do nothing in this case as the command above is windows specific
		}
	}
}
