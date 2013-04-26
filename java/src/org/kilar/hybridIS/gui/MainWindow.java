package org.kilar.hybridIS.gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;

import java.awt.SystemColor;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
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
import javax.swing.tree.TreePath;

import org.kilar.hybridIS.abstractions.IntegratorConfig;
import org.kilar.hybridIS.abstractions.Module;
import org.kilar.hybridIS.abstractions.ModuleType;
import org.kilar.hybridIS.abstractions.ProductionIS;
import org.kilar.hybridIS.general.Logger;
import org.kilar.hybridIS.general.Project;
import org.kilar.hybridIS.general.ProjectConfig;
import org.kilar.hybridIS.general.Util;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import org.kilar.hybridIS.productionIS.*;
import org.eclipse.wb.swing.FocusTraversalOnArray;

import java.awt.Component;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javax.swing.JLabel;
import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;
import javax.swing.SwingConstants;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeSelectionEvent;

public class MainWindow {

	private JFrame frame;
	private JTextArea consoleLog;
	private JTextArea consoleOut;
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
	
	private File opennedFile = null;
	private String tempSavedText = "";
	private TreePath currPath = null;
	

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
	
	private void calculate(){
		List<List<Double> > result = project.calculate();
		String s = "";
		for(List<Double> out : result){
			for(Double value : out){
				s += value + "\t"; 
			}
			s += "\n";
		}
		consoleOut.setText(s);
		Util.saveToFile(new File(project.getPath(), "project.outputData"), s);
	}
	
	@SuppressWarnings("unused")
	private void refreshIntegrator(){
		//TODO
	}
	
	@SuppressWarnings("unused")
	private void refreshModule(Module module){
		//TODO
	}
	
	private void save(){
		if(tempSavedText.equals(codeArea.getText())){
			return;
		}
		Logger.info("Сохраняю файл " + opennedFile.getName());
		Util.saveToFile(opennedFile, codeArea.getText());
		tempSavedText = codeArea.getText();
		
		//refresh project
		Logger.info("Перезагружаю проект после изменения соержимого");
		openProject(project.getPath());
		
		/*
		//TODO future realises: refresh module or code of module or integrator....
		if(opennedFile.getName().equals(project.getIntegratorName())){
			refreshIntegrator();
		}
		Module m = project.getModule(opennedFile.getName()) ; 
		if( m != null){
			refreshModule(m);
		}
		//TODO refresh code
		
		//TODO refresh config
		*/
		
		updateLog();
	}
	
	private boolean openFile(File newFile){
		if(opennedFile != null){
			if(opennedFile.getName().equals(newFile.getName()))
				return false;
			if(!tempSavedText.equals(codeArea.getText())){
				int answer = JOptionPane.showConfirmDialog(
						null, "Сохранить изменения в файле?", 
						"Запрос сохранения", JOptionPane.YES_NO_CANCEL_OPTION);
				if(answer == JOptionPane.OK_OPTION){
					save();
				} else if(answer == JOptionPane.CANCEL_OPTION){
					tree.setSelectionPath(currPath);
					return false;
				}
			}	
		}
		String s = Util.getFileTextFull(newFile);
		codeArea.setText(s);
		tempSavedText = s;
		opennedFile = newFile;
		updateLog();
		return true;
	}
	
	@SuppressWarnings("unused")
	private void saveProject(){
		Logger.info("Сохраняю проект");
		String root = project.getPath();
		File config = new File(root, "config");
		try {
			//save config
			Util.saveObjectToFile(project.getConfig(), config);
			//save integrator
			Util.saveObjectToFile(project.getIntegrator().getConfig(), new File(root, project.getIntegratorName()));
			//save modules
			for(Module m : project.getModules()){
				Util.saveObjectToFile(m.getConfig(), new File(root, m.getName()));
				if(m.getType().equals(ModuleType.Production)){
					Util.saveToFile(new File(root, ((ModuleConfigProduction)m.getConfig()).getCode()), ((ProductionIS)m).getCode());
				}
			}
		} catch (IOException e) {
			Logger.error("Ошибка сохранения проекта");
			return;
		}
		//TODO test
		
	}
	
	/**
	 * @param path
	 * @return true if project created successfully
	 */
	private boolean createProject(String path){
		File root = new File(path);
		Logger.info("Пытаюсь создать проект по пути " + path);
		root.mkdirs();
		root.mkdir();
		if(!root.exists()){
			Logger.error("Корневой каталог проекта не существует");
			return false;
		}
		if(!root.isDirectory()){
			Logger.error("По указанному пути находится не каталог, а файл");
			return false;
		}
		
		String integrName = "integrator";
		IntegratorConfig iconfig = new IntegratorConfig();
		iconfig.setName(integrName);
		iconfig.setType("basic");
		File integrator = new File(root.getPath(), integrName);
		
		File dataResource = new File(root.getPath(),"dataResource");
		String toDataResource = "0 0";
		
		File prConfig = new File(root.getPath(), "config");
		ProjectConfig config = new ProjectConfig();
		config.setName(root.getName());
		config.setIntegrator(integrName);
		config.setDataResource(dataResource.getName());
		config.setInputLength(0);
		config.setOutputLength(0);
		config.setInNames(new String[0]);
		config.setOutNames(new String[0]);
		config.setModules(new String[0]);
		
		//save integrator via Gson
		try {
			Util.saveObjectToFile(iconfig, integrator);
		} catch (IOException e1) {
			Logger.error("Не удалось создать файл интегратора");
			e1.printStackTrace();
			return false;
		}
		//save dataresource 
		try {
			dataResource.createNewFile();
			Util.saveToFile(dataResource, toDataResource);
		} catch (IOException e) {
			Logger.error("Не удалось создать файл со стартовыми данными");
			e.printStackTrace();
			return false;
		}
		
		//save project via Gson
		try {
			Util.saveObjectToFile(config, prConfig);
		} catch (IOException e) {
			Logger.error("Не удалось создать файл конфига для проекта");
			e.printStackTrace();
			return false;
		}
		Logger.info("Проект успешно создан");
		updateLog();
		return true;
	}
	
	private void updateLog(){
		consoleLog.setText(Logger.get());
		Util.saveToFile(new File(project.getPath(), "project.logger"), consoleLog.getText());
	}
	private void refreshTree(){
		root = new DefaultMutableTreeNode(project.getName());
		tree.setModel(new DefaultTreeModel(root));
		for(Module module : project.getModules()){
			addToRoot(module.getName());
			if(module.getType().equalsIgnoreCase(ModuleType.Production)){
				addToRoot(((ModuleConfigProduction)module.getConfig()).getCode());
			}
		}
		addToRoot(project.getIntegratorName());
		addToRoot("config");
		tree.expandPath(new TreePath(root));
		updateLog();
	}
	
	/**
	 * Loads last project to the tree, if can
	 */
	private void initProjects(){
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
					scanner.close();
					return;
				}
			}
			scanner.close();
		} catch (FileNotFoundException e){
			try {
				projConfig.createNewFile();
			} catch (IOException e1) {
				Logger.error("Ошибка создания конфигурационного файла");
			}
		} catch (Exception e ){
			e.printStackTrace();
		}
		Logger.info("Не найденs проекты");
		updateLog();
	}
	
	private void openProject(String path){
		Logger.info("Открываю проект " + path);
		try {
			project = new Project(path);
		} catch (Exception e) {
			Logger.error("Ошибка открытия проекта");
			updateLog();
			return;
		}
		Logger.info("Проект открыт");
		updateLog();
		refreshTree();
	}
	
	private void addToRoot(Object o){
		root.add(new DefaultMutableTreeNode(o));
	}
	
	@SuppressWarnings("unused")
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
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				Logger.info("Выбран файл " + e.getPath().getLastPathComponent());
				if(openFile(new File(project.getPath(), e.getPath().getLastPathComponent().toString())))
					currPath = e.getPath();
				updateLog();
			}
		});
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
				NewProjectDialog fc = new NewProjectDialog(frame);
				fc.showDialog();
				if(fc.isPathSelected()){
					String p = fc.getFullPath();
					Logger.info("Сигнал создания проекта" + p);
					if(createProject(p)){						
						openProject(p);
					}
				}
				updateLog();
								
			}
		});
		menuNewProject.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		menuNew.add(menuNewProject);

		JMenuItem menuNewModule = new JMenuItem("Модуль");
		menuNewModule.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//TODO create dialog with radiobutton "production V neural" and name
			}
		});
		menuNewModule.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				InputEvent.CTRL_MASK));
		menuNew.add(menuNewModule);

		JMenuItem menuOpen = new JMenuItem("Открыть...");
		menuOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(new File(project.getPath()));
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int result = chooser.showDialog(null, "open");
				if(result == JFileChooser.APPROVE_OPTION) {
					openProject(chooser.getSelectedFile().getPath()); 
				}
				updateLog();
			}
		});
		menuOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
				InputEvent.CTRL_MASK));
		menu.add(menuOpen);

		JMenuItem menuItem = new JMenuItem("Сохранить...");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				InputEvent.CTRL_MASK));
		menu.add(menuItem);
		
		JMenu menu_1 = new JMenu("Запуск");
		
		menuBar.add(menu_1);
		
		menuItem_2 = new JMenuItem("Запустить");
		menuItem_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Logger.info("Запускаю модули на исполнение");
				calculate();
				updateLog();
			}
		});
		menuItem_2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));
		menu_1.add(menuItem_2);
		menu_1.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{menuItem_2}));
		
		labelNumStr = new JLabel("         Строка: ");
		labelNumStr.setHorizontalAlignment(SwingConstants.LEFT);
		labelNumStr.setFont(new Font("DejaVu Sans Mono", Font.PLAIN, 12));
		menuBar.add(labelNumStr);

		consoleLog = createTextAreaTab(tabbedPanebottom, "Лог");
		consoleLog.setEditable(false);
		consoleOut = createTextAreaTab(tabbedPanebottom, "Результат расчётов");
		consoleOut.setEditable(false);
		frame.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{codeArea, scrollPane_1, panel_1, frame.getContentPane(), splitPane, scrollPane, tree, panelBottom, tabbedPanebottom, panel, scrollPane_2, text, menuBar, menu, menuNew, splitPane_1, menuNewProject, menuNewModule, menuOpen, menuItem, menu_1, menuItem_2}));

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
