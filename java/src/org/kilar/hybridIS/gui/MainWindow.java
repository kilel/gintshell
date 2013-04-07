package org.kilar.hybridIS.gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;

import java.awt.SystemColor;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.SpringLayout;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.kilar.hybridIS.general.Logger;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import org.kilar.hybridIS.productionIS.*;
import org.eclipse.wb.swing.FocusTraversalOnArray;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;
import javax.swing.SwingConstants;

public class MainWindow {

	private JFrame frame;
	private JTextArea consoleOutput;
	private JTextArea consoleInput;
	private JMenuItem menuItem_2;
	private JMenuBar menuBar;
	private JTree tree;
	private JTextArea codeArea;
	private JPanel panel;
	private JScrollPane scrollPane_2;
	private JTextArea text;
	private JLabel labelNumStr;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(SystemColor.window);
		frame.setBackground(Color.WHITE);
		frame.setTitle("Гибридная Информационная Система (оболочка gintshell)");
		frame.setBounds(100, 100, 740, 497);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		frame.getContentPane().setLayout(springLayout);

		JSplitPane splitPane = new JSplitPane();
		splitPane.setOneTouchExpandable(true);
		splitPane.setContinuousLayout(true);
		springLayout.putConstraint(SpringLayout.NORTH, splitPane, 10,
				SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, splitPane, 10,
				SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, splitPane, -10,
				SpringLayout.SOUTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, splitPane, -10,
				SpringLayout.EAST, frame.getContentPane());
		splitPane.setResizeWeight(0.1);
		frame.getContentPane().add(splitPane);

		JScrollPane scrollPane = new JScrollPane();
		splitPane.setLeftComponent(scrollPane);

		tree = new JTree();
		scrollPane.setViewportView(tree);
		tree.setModel(new DefaultTreeModel(new DefaultMutableTreeNode("Проекты") {
			{
				DefaultMutableTreeNode node_1;
				node_1 = new DefaultMutableTreeNode("temp one");
				node_1.add(new DefaultMutableTreeNode("integrator"));
				node_1.add(new DefaultMutableTreeNode("module 1"));
				node_1.add(new DefaultMutableTreeNode("moudle 2"));
				node_1.add(new DefaultMutableTreeNode("project.properties"));
				add(node_1);
				node_1 = new DefaultMutableTreeNode("temp two");
				node_1.add(new DefaultMutableTreeNode("integrator"));
				node_1.add(new DefaultMutableTreeNode("module 1"));
				node_1.add(new DefaultMutableTreeNode("module 2"));
				node_1.add(new DefaultMutableTreeNode("module 3"));
				node_1.add(new DefaultMutableTreeNode("test data 1"));
				node_1.add(new DefaultMutableTreeNode("project.properties"));
				add(node_1);
			}
		}));

		JSplitPane splitPane_1 = new JSplitPane();
		splitPane_1.setOneTouchExpandable(true);
		splitPane_1.setContinuousLayout(true);
		splitPane_1.setResizeWeight(0.6);
		splitPane_1.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setRightComponent(splitPane_1);

		JPanel panelBottom = new JPanel();
		splitPane_1.setRightComponent(panelBottom);
		SpringLayout sl_panelBottom = new SpringLayout();
		panelBottom.setLayout(sl_panelBottom);

		JTabbedPane tabbedPanebottom = new JTabbedPane(JTabbedPane.TOP);

		sl_panelBottom.putConstraint(SpringLayout.NORTH, tabbedPanebottom, 0,
				SpringLayout.NORTH, panelBottom);
		sl_panelBottom.putConstraint(SpringLayout.WEST, tabbedPanebottom, 0,
				SpringLayout.WEST, panelBottom);
		sl_panelBottom.putConstraint(SpringLayout.SOUTH, tabbedPanebottom, 0,
				SpringLayout.SOUTH, panelBottom);
		sl_panelBottom.putConstraint(SpringLayout.EAST, tabbedPanebottom, 0,
				SpringLayout.EAST, panelBottom);
		panelBottom.add(tabbedPanebottom);

		JPanel panel_1 = new JPanel();
		splitPane_1.setLeftComponent(panel_1);
		SpringLayout sl_panel_1 = new SpringLayout();
		panel_1.setLayout(sl_panel_1);

		JScrollPane scrollPane_1 = new JScrollPane();
		sl_panel_1.putConstraint(SpringLayout.NORTH, scrollPane_1, 0,
				SpringLayout.NORTH, panel_1);
		sl_panel_1.putConstraint(SpringLayout.WEST, scrollPane_1, 0,
				SpringLayout.WEST, panel_1);
		sl_panel_1.putConstraint(SpringLayout.SOUTH, scrollPane_1, 0,
				SpringLayout.SOUTH, panel_1);
		sl_panel_1.putConstraint(SpringLayout.EAST, scrollPane_1, 0,
				SpringLayout.EAST, panel_1);
		panel_1.add(scrollPane_1);

		codeArea = new JTextArea();
		codeArea.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent e) {
				labelNumStr.setText("         Строка: " + ( ProdCodeUtils.getNumStr(codeArea.getText(), codeArea.getCaretPosition())));
				//[TODO]
			}
		});
		codeArea.setDoubleBuffered(true);
		codeArea.setDragEnabled(true);
		codeArea.setFont(new Font("DejaVu Sans Mono", Font.PLAIN, 12));
		codeArea.setTabSize(3);
		codeArea.setWrapStyleWord(true);
		scrollPane_1.setViewportView(codeArea);

		menuBar = new JMenuBar();
		menuBar.setBackground(SystemColor.window);
		menuBar.setToolTipText("");
		frame.setJMenuBar(menuBar);

		JMenu menu = new JMenu("Файл");
		menuBar.add(menu);

		JMenu menuNew = new JMenu("Новый");
		menu.add(menuNew);

		JMenuItem menuNewProject = new JMenuItem("Проект");
		menuNewProject.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		menuNew.add(menuNewProject);

		JMenuItem menuNewModule = new JMenuItem("Модуль");
		menuNewModule.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				InputEvent.CTRL_MASK));
		menuNew.add(menuNewModule);

		JMenuItem menuOpen = new JMenuItem("Открыть...");
		menuOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
				InputEvent.CTRL_MASK));
		menu.add(menuOpen);

		JMenuItem menuItem = new JMenuItem("Сохранить...");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				InputEvent.CTRL_MASK));
		menu.add(menuItem);

		JMenuItem menuItem_1 = new JMenuItem("Сохранить как...");
		menuItem_1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		menu.add(menuItem_1);
		
		JMenu menu_1 = new JMenu("Запуск");
		
		menuBar.add(menu_1);
		
		menuItem_2 = new JMenuItem("Запустить");
		menuItem_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Logger.clear();
				List<String> in = new ArrayList<String>();
				List<String> out = new ArrayList<String>();
				in.add("java");
				in.add("proxy");
				ProdCodeParser parser = new ProdCodeParser(codeArea.getText(), in, out);
				parser.split();
				consoleOutput.setText(Logger.get());
				//[TODO]
			}
		});
		menuItem_2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));
		menu_1.add(menuItem_2);
		menu_1.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{menuItem_2}));
		
		labelNumStr = new JLabel("         Строка: ");
		labelNumStr.setHorizontalAlignment(SwingConstants.LEFT);
		labelNumStr.setFont(new Font("DejaVu Sans Mono", Font.PLAIN, 12));
		menuBar.add(labelNumStr);

		consoleOutput = createTextAreaTab(tabbedPanebottom, "Вывод");
		consoleOutput.setEditable(false);
		consoleInput = createTextAreaTab(tabbedPanebottom, "Консоль");
		frame.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{codeArea, scrollPane_1, panel_1, frame.getContentPane(), splitPane, scrollPane, tree, panelBottom, tabbedPanebottom, panel, scrollPane_2, text, menuBar, menu, menuNew, splitPane_1, menuNewProject, menuNewModule, menuOpen, menuItem, menuItem_1, menu_1, menuItem_2}));

	}

	private JTextArea createTextAreaTab(JTabbedPane tabbedPane, String name) {
		panel = new JPanel();
		scrollPane_2 = new JScrollPane();
		SpringLayout sl_panel = new SpringLayout();

		text = new JTextArea();
		text.setTabSize(3);
		text.setFont(new Font("DejaVu Sans Mono", Font.PLAIN, 12));
		tabbedPane.addTab(name, panel);
		panel.setLayout(sl_panel);

		sl_panel.putConstraint(SpringLayout.NORTH, scrollPane_2, 0,
				SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.WEST, scrollPane_2, 0,
				SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, scrollPane_2, 0,
				SpringLayout.SOUTH, panel);
		sl_panel.putConstraint(SpringLayout.EAST, scrollPane_2, 0,
				SpringLayout.EAST, panel);
		panel.add(scrollPane_2);
		scrollPane_2.setViewportView(text);

		return text;
	}
}
