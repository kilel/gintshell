package org.kilar.hybridIS.gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;

import java.awt.SystemColor;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
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
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

import org.kilar.hybridIS.abstractions.Module;
import org.kilar.hybridIS.general.Logger;
import org.kilar.hybridIS.general.Project;
import org.kilar.hybridIS.general.ProjectConfig;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import org.kilar.hybridIS.productionIS.*;
import org.eclipse.wb.swing.FocusTraversalOnArray;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import sun.org.mozilla.javascript.internal.json.JsonParser;

import java.awt.Component;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

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
	private JTextArea codeArea;
	private JPanel panel;
	private JScrollPane scrollPane_2;
	private JTextArea text;
	private JLabel labelNumStr;
	
	private JTree tree;
	private DefaultMutableTreeNode root;
	private Project project;
	

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
		initProjects();
	}

	private void updateLog(){
		consoleOutput.setText(Logger.get());
	}
	private void refreshTree(){
		
	}
	/**
	 * Loads projects to the tree, if can
	 */
	private void initProjects(){
		//projects = new ArrayList<>();
		Gson gson = new Gson();
		//[TODO]
		File projConfig;
		Logger.info("Поиск последних проектов...");
		projConfig = new File("gintshell.config");
		try{
			Scanner scanner = new Scanner(projConfig);
			while(scanner.hasNextLine()){
				String path = scanner.nextLine();
				path = path.trim();
				File f = new File(path);
				if(f.exists()){
					openProject(path.trim());
					break;
				}
			}
			scanner.close();
		} catch (FileNotFoundException e){
			try {
				projConfig.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (Exception e ){
			//[TODO]
		}
		
		tree.expandPath(new TreePath(root));
	}
	
	private void openProject(String path){
		project = new Project(path);
		for(Module module : project.getModules()){
			DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(module.getName());
			root.add(newNode);
		}
	}
	
	private void addChildsToNode(DefaultMutableTreeNode node, File file){
		ArrayList<String> directories = new ArrayList<>(), files = new ArrayList<>();
		for(File child : file.listFiles()){
			if(child.isDirectory()){
				directories.add(child.getAbsolutePath());
			} else {
				files.add(child.getAbsolutePath());
			}
		}
		Object[] dirs = directories.toArray(), fi = files.toArray();
		Arrays.sort(dirs);
		Arrays.sort(fi);
		File t;
		for(Object d : dirs){
			t = new File((String) d);
			DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(t.getName());
			node.add(newNode);
			if(t.listFiles().length == 0){
				newNode.add(new DefaultMutableTreeNode());
			}
			addChildsToNode(newNode, t);
		}
		for(Object f : fi){
			t = new File((String) f);
			DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(t.getName());
			node.add(newNode);
		}
	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(SystemColor.window);
		frame.setBackground(Color.WHITE);
		frame.setTitle("Гибридная Информационная Система - gintshell");
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
		root = new DefaultMutableTreeNode("Проекты");
		tree.setModel(new DefaultTreeModel(root	));

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
		menuNewProject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//TODO adding a new project
				NewProjectDialog fc = new NewProjectDialog(frame);
				fc.showDialog();
				
				
				
			}
		});
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
				List<Double> inVal = new ArrayList<>();
				inVal.add(0.5);
				inVal.add(0.3);
				in.add("java");
				in.add("proxy");
				out.add("out1");
				out.add("out2");
				ProdCodeParser analyser = new ProdCodeParser(codeArea.getText(), in, out);
				analyser.isValid();
				updateLog();
				try {
					analyser.calculate(inVal);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					Logger.error(e1.getLocalizedMessage());
				}
				Logger.info(analyser.toString());
				updateLog();
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
