package org.kilar.hybridIS.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.JFormattedTextField;

import org.kilar.hybridIS.general.Logger;
import org.kilar.hybridIS.general.Util;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.IO;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class NewProjectDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField areaPath;
	private JTextField areaName;
	private JLabel labelProjectNameFull;
	private boolean isPathSelected = false;
	private JButton okButton;
	private JButton cancelButton;
	public NewProjectDialog(JFrame parent){
		super(parent, "Открытие проекта", true);
		initialize();
	}
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			NewProjectDialog dialog = new NewProjectDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public boolean isPathSelected(){
		return isPathSelected;
	}
	public int showDialog(){
		try {
			this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			this.setVisible(true);
			
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		return 0;
	}
	
	
	private boolean isValidFullPath(){
		String s = "";
		boolean isValid = true;
		
		File f = new File(areaPath.getText());
		if(!f.exists()){
			s = "Рабочий каталог должен существовать!";
			isValid = false;
		}
		if(!Util.isFileNameValid(areaName.getText())) {
			s = "Некорректное имя проекта!"; 
			isValid = false;
		}
		if(isValid)
			try {
				s = new File(areaPath.getText(), areaName.getText()).getCanonicalPath();
			} catch (IOException e) {
				s = "Ошибка формирования пути";
				isValid = false;
			}
		labelProjectNameFull.setText(s);
		if(isValid)
			labelProjectNameFull.setForeground(Color.BLUE);
		else
			labelProjectNameFull.setForeground(Color.RED);
		return isValid;
	}
	
	private void initialize(){
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				//TODO set areaPath with some value
			}
		});
		setTitle("Создание нового проекта - gintshell");
		setBounds(100, 100, 450, 213);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		SpringLayout sl_contentPanel = new SpringLayout();
		contentPanel.setLayout(sl_contentPanel);
		
		areaPath = new JTextField();
		areaPath.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				okButton.setEnabled(isValidFullPath());
			}
		});
		areaPath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				okButton.setEnabled(isValidFullPath());
			}
		});
		sl_contentPanel.putConstraint(SpringLayout.WEST, areaPath, 10, SpringLayout.WEST, contentPanel);
		contentPanel.add(areaPath);
		areaPath.setColumns(10);
		
		JButton buttonBrowse = new JButton("Обзор...");
		buttonBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//TODO
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				
				if(fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
					File cd = fc.getSelectedFile();
					
					String s;
					try {
						s = cd.getCanonicalPath();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						return;
					}
					areaPath.setText(s);
					okButton.setEnabled(isValidFullPath());
				}
				
			}
		});
		sl_contentPanel.putConstraint(SpringLayout.NORTH, areaPath, 3, SpringLayout.NORTH, buttonBrowse);
		sl_contentPanel.putConstraint(SpringLayout.EAST, areaPath, -17, SpringLayout.WEST, buttonBrowse);
		sl_contentPanel.putConstraint(SpringLayout.NORTH, buttonBrowse, 17, SpringLayout.NORTH, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.EAST, buttonBrowse, -10, SpringLayout.EAST, contentPanel);
		contentPanel.add(buttonBrowse);
		
		JLabel labelCat = new JLabel("Путь до рабочего каталога");
		sl_contentPanel.putConstraint(SpringLayout.NORTH, labelCat, 0, SpringLayout.NORTH, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.WEST, labelCat, 0, SpringLayout.WEST, areaPath);
		contentPanel.add(labelCat);
		
		JLabel labelProjectNameLabel = new JLabel("Название проекта");
		sl_contentPanel.putConstraint(SpringLayout.NORTH, labelProjectNameLabel, 6, SpringLayout.SOUTH, areaPath);
		sl_contentPanel.putConstraint(SpringLayout.WEST, labelProjectNameLabel, 0, SpringLayout.WEST, areaPath);
		contentPanel.add(labelProjectNameLabel);
		
		JLabel labelFinDest = new JLabel("Итоговый полный путь до проекта:");
		sl_contentPanel.putConstraint(SpringLayout.NORTH, labelFinDest, 38, SpringLayout.SOUTH, labelProjectNameLabel);
		sl_contentPanel.putConstraint(SpringLayout.WEST, labelFinDest, 0, SpringLayout.WEST, areaPath);
		contentPanel.add(labelFinDest);
		
		areaName = new JTextField();
		areaName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				 okButton.setEnabled(isValidFullPath());
			}
		});
		sl_contentPanel.putConstraint(SpringLayout.NORTH, areaName, 6, SpringLayout.SOUTH, labelProjectNameLabel);
		sl_contentPanel.putConstraint(SpringLayout.WEST, areaName, 10, SpringLayout.WEST, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.EAST, areaName, 0, SpringLayout.EAST, areaPath);
		contentPanel.add(areaName);
		areaName.setColumns(10);
		
		labelProjectNameFull = new JLabel("Путь не может быть пустым");
		labelProjectNameFull.setFont(new Font("Dialog", Font.ITALIC, 12));
		labelProjectNameFull.setForeground(Color.RED);
		sl_contentPanel.putConstraint(SpringLayout.NORTH, labelProjectNameFull, 6, SpringLayout.SOUTH, labelFinDest);
		sl_contentPanel.putConstraint(SpringLayout.WEST, labelProjectNameFull, 0, SpringLayout.WEST, areaPath);
		contentPanel.add(labelProjectNameFull);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(isValidFullPath()){
							isPathSelected = true;
							return;
						} 
						
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	
	/**
	 * Create the dialog.
	 */
	public NewProjectDialog() {
		initialize();
	}
}