package org.kilar.hybridIS.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JRadioButton;

import org.kilar.hybridIS.abstractions.ModuleType;
import org.kilar.hybridIS.general.Util;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class NewModuleDialog extends JDialog {

	private static final long serialVersionUID = 6741265910842852620L;
	private final JPanel contentPanel = new JPanel();
	private JTextField fieldName;
	private boolean isValidName = false;
	private JRadioButton radioProduction;
	private JRadioButton radioNeural;
	private JLabel labelValidation;
	private JButton okButton;
	private JButton cancelButton;
	private String moduleType = ModuleType.Production;

	public boolean isValidName() {
		return isValidName;
	}

	public String getModuleName() {
		if(isValidName){
			return fieldName.getText();
		} else {
			throw new RuntimeException();
		}
	}
	
	public String getModuleType() {
		if(isValidName){
			return moduleType;
		} else {
			throw new RuntimeException();
		}
	}
	
	private boolean validateName(){
		boolean isValid = Util.isFileNameValid(fieldName.getText());
		if(isValid){
			labelValidation.setForeground(Color.BLUE);
			labelValidation.setText(fieldName.getText());
		} else {
			labelValidation.setForeground(Color.RED);
			labelValidation.setText("Некорректное имя модуля");
		}
		okButton.setEnabled(isValid);
		return isValid;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			NewModuleDialog dialog = new NewModuleDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initialize(){
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		SpringLayout sl_contentPanel = new SpringLayout();
		contentPanel.setLayout(sl_contentPanel);
		
		JLabel label = new JLabel("Название модуля");
		sl_contentPanel.putConstraint(SpringLayout.NORTH, label, 10, SpringLayout.NORTH, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.WEST, label, 10, SpringLayout.WEST, contentPanel);
		contentPanel.add(label);
		
		fieldName = new JTextField();
		
		fieldName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				//TODO
				validateName();
			}
		});
		sl_contentPanel.putConstraint(SpringLayout.NORTH, fieldName, 15, SpringLayout.SOUTH, label);
		sl_contentPanel.putConstraint(SpringLayout.WEST, fieldName, 10, SpringLayout.WEST, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.EAST, fieldName, 0, SpringLayout.EAST, contentPanel);
		contentPanel.add(fieldName);
		fieldName.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Тип модуля");
		sl_contentPanel.putConstraint(SpringLayout.NORTH, lblNewLabel, 19, SpringLayout.SOUTH, fieldName);
		sl_contentPanel.putConstraint(SpringLayout.WEST, lblNewLabel, 0, SpringLayout.WEST, label);
		contentPanel.add(lblNewLabel);
		
		radioProduction = new JRadioButton("Продукционный");
		radioProduction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moduleType = ModuleType.Production;
				radioProduction.setSelected(true);
				radioNeural.setSelected(false);
			}
		});
		radioProduction.setSelected(true);
		sl_contentPanel.putConstraint(SpringLayout.NORTH, radioProduction, 17, SpringLayout.SOUTH, lblNewLabel);
		sl_contentPanel.putConstraint(SpringLayout.WEST, radioProduction, 0, SpringLayout.WEST, label);
		contentPanel.add(radioProduction);
		
		radioNeural = new JRadioButton("Нейросеть");
		radioNeural.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moduleType = ModuleType.Neural;
				radioProduction.setSelected(false);
				radioNeural.setSelected(true);
			}
		});
		sl_contentPanel.putConstraint(SpringLayout.NORTH, radioNeural, 6, SpringLayout.SOUTH, radioProduction);
		sl_contentPanel.putConstraint(SpringLayout.WEST, radioNeural, 0, SpringLayout.WEST, label);
		contentPanel.add(radioNeural);
		
		labelValidation = new JLabel("");
		sl_contentPanel.putConstraint(SpringLayout.WEST, labelValidation, 0, SpringLayout.WEST, label);
		sl_contentPanel.putConstraint(SpringLayout.SOUTH, labelValidation, -10, SpringLayout.SOUTH, contentPanel);
		contentPanel.add(labelValidation);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("OK");
				okButton.setEnabled(false);
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						//TODO OK
						if(validateName() == true){
							isValidName = true;
							dispose();
						}
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						//TODO CANCEL
						isValidName = false;
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	
	public NewModuleDialog(JFrame parent){
		super(parent, "Создание нового модуля", true);
		initialize();
	}
	
	/**
	 * Create the dialog.
	 */
	public NewModuleDialog() {
		initialize();
	}
}
