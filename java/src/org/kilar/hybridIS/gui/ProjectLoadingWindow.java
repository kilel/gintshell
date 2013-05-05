package org.kilar.hybridIS.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.SpringLayout;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;

public class ProjectLoadingWindow extends JDialog {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			ProjectLoadingWindow dialog = new ProjectLoadingWindow();
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ProjectLoadingWindow() {
		setBackground(Color.RED);
		getContentPane().setBackground(Color.RED);
		getContentPane().setEnabled(false);
		setResizable(false);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("Загрузка проекта. . .");
		setBounds(100, 100, 377, 10);
		getContentPane().setLayout(new BorderLayout());
	}
}
