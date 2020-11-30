package main;

import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.StringReader;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.crypto.KeySelector.Purpose;

public class World extends JFrame implements ActionListener, Observer {

	final Repository repository;
	final JFrame frame;
	final JFileChooser fc;
	final JMenuItem loadRoster;
	final JMenuItem addAttendance;
	final JMenuItem save;
	final JMenuItem dataPlot;
	final JTextArea log;
	final JTable jtable;
	final MyTableModel tableModel;

	public World() {
		fc = new JFileChooser();
		log = new JTextArea();

		repository = new Repository();
		repository.addObserver(this);

		frame = new JFrame("CSE360 Final Project");

		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenu aboutMenu = new JMenu("About");

		loadRoster = new JMenuItem("Load a Roster");
		addAttendance = new JMenuItem("Add Attendance");
		save = new JMenuItem("Save");
		dataPlot = new JMenuItem("Plot Data");

		loadRoster.addActionListener(this);
		addAttendance.addActionListener(this);
		save.addActionListener(this);
		dataPlot.addActionListener(this);

		fileMenu.add(loadRoster);
		fileMenu.add(addAttendance);
		fileMenu.add(save);
		fileMenu.add(dataPlot);

		JTextArea aboutInfo = new JTextArea("Amar" + "\n\nDanielle Dupont" + "\n\nIsaiah Graham", 35, 37);
		aboutInfo.setEditable(false);
		aboutMenu.add(aboutInfo);

		menuBar.add(fileMenu);
		menuBar.add(aboutMenu);

		frame.setJMenuBar(menuBar);

		tableModel = new MyTableModel();
		jtable = new JTable(tableModel);
		jtable.setBounds(30, 40, 200, 300);
		JScrollPane sp = new JScrollPane(jtable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		jtable.setFillsViewportHeight(true);
		frame.add(sp);
		frame.setSize(300, 400);
		frame.setVisible(true);

		frame.setSize(500, 650);
		frame.setVisible(true);
	}

	public void populateTable(String[][] tableInfo, String[] headers) {

//		jtable = new JTable(tableInfo, headers);
		tableModel.updateTable(tableInfo, headers);

	}

	public static void main(String[] args) {
		World world = new World();
	}

	@SuppressWarnings({ "deprecation" })
	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			if (e.getSource() == loadRoster) {
				String csv = ""
						+ "123,Amar,Yadav,SER,Graduate,ayadav42\n"
						+ "345,Danielle,Dupont,CSE,Undergrad,dmdupont52\n"
						+ "123,Amar,Yadav,SER,Graduate,ayadav42\n"
						+ "456,Taylor,Northcott,CE,Undergraduate,tnorthcott45\n"
						+ "567,Abi,Mcgee,bs, Undergraduate,amcgee21\n"
						+ "678,Abby,Steinman,BSE, Undergraduate,asteinman17";
				repository.addStudentsFromRoster(new StringReader(csv));

			} else if (e.getSource() == addAttendance) {
				String csv2 = "ayadav42,20\ndmdupont52,60\nayadav42,50\ntnorthcott45,25\namcgee21,3\nasteinman17,25";
				repository.addAttendance(new StringReader(csv2), "Nov 20");
				String csv3 = "ayadav42,40\ndmdupont52,80\ntnorthcott45,0";
				repository.addAttendance(new StringReader(csv3), "Nov 21");

				//add somethings
			} else if (e.getSource() == save) {
				repository.saveDataToFile();
			} else if (e.getSource() == dataPlot) {
				System.out.println("repository.numOfNewStudents: " + repository.numOfNewStudents);
				SwingUtilities.invokeLater(() -> {
					System.out.println("repository.numOfNewStudents2: " + repository.numOfNewStudents);
					PlotData example = new PlotData("Scatter Chart Example", repository);
					example.setSize(800, 400);
					example.setLocationRelativeTo(null);
					example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
					example.setVisible(true);
				});
			}

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

	}

	@Override
	public void update(Observable o, Object arg) {
		String[][] latestTableInfo = repository.getTableData();
		String[] headers = new String[repository.csvHeaders.size()];
		for (int index = 0; index < headers.length; index++) {
			headers[index] = repository.csvHeaders.get(index);
		}
		populateTable(latestTableInfo, headers);
	}

}
