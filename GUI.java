/*
 * Final Year Project :::>>>  OpenSim
 * By :
 * 		Prathibha 
 * 		Vital
 * 		Rohith
 * 
 * 		MVJCE
 */

package org.opensim.GUI;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerDynamicWorkload;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.examples.power.Constants;
import org.cloudbus.cloudsim.power.PowerHost;
import org.cloudbus.cloudsim.power.PowerVm;
import org.opensim.examples.Flavor;
import org.opensim.examples.Image;
import org.opensim.examples.Volume;
import org.opensim.examples.Container;
import org.opensim.sampleStorage.API;
import org.opensim.sampleStorage.OpensimStorageControlNode;
import org.opensim.sampleStorage.OpensimStorageServer;
import org.opensim.Keystone.*;
import datacenter_input.*;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.BorderLayout;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import java.awt.Font;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JPasswordField;
import java.awt.Color;
import java.awt.Desktop;
import javax.swing.UIManager;
import javax.swing.border.MatteBorder;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;


@SuppressWarnings("serial")
public class GUI extends JFrame{
	public static JPanel panel_10 = new JPanel();
	public static JPanel panel_11 = new JPanel();
	public JButton launchButton = new JButton("Launch");
	public static JTabbedPane tabbedPane_2;
	int cloudlet_Id = 0;
	static DefaultComboBoxModel<String> listModelFlavorAccess = new DefaultComboBoxModel<>();
	DefaultComboBoxModel<String> boxModelInstance = new DefaultComboBoxModel<>();
	DefaultTableModel model5;// = new DefaultTableModel();
	public static int instanceNameCount = 0;
	String[] applicationNames = new String[5];
	String sampleApplicationFolder = "/home/rohith/Desktop/workload/new";
	public String userName;
	public String passWord;
	public int isLogged = 0;
	public int auth;
	List<Cloudlet> cloudletList = new ArrayList<Cloudlet>();
	List<PowerHost> hostList = new ArrayList<PowerHost>();
	private List<Vm> vmList = new ArrayList<Vm>();
	public static List<Flavor> flaList = new ArrayList<Flavor>();
	static List<Image> imgList = new ArrayList<Image>();
	public static List<Volume> volList = new ArrayList<Volume>();
	public static Container Swift = new Container();
	int vmid = 0;
	int num_of_host;
	String nflaname = "";
	double mips;
	int pes_no;
	long bw;
	int ram;
	long size;
	
	public long imgSize;
	String imgnm;
	String imgLoc;
	String format;
	String arch;
	int minDisk , minRam;
	
	String volName;
	int volSize = -1;

	//String conName;
	//int conSize = -1;
	
	List<String> vm_name = new ArrayList<String>();

	double instanceMips ;
	int instanceRam , instancePes;
	long instanceBw , instanceSize;
	
	public static JPanel panelConf;
	final JLabel lblUserNameNovaConf = new JLabel();
	final JLabel lblUserNameNova = new JLabel();
	public static JTextArea textArea;
	public static JTextArea textArea_Log;
	private JFrame frmOpensim;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_5;
	private JTextField textField;
	private JPasswordField passwordField;
	private JTabbedPane tabbedPane_1;
	public keyStone keyStone = new keyStone();
	private JTextField textField_4;
	private JPasswordField passwordField_1;
	
	public static BufferedReader reader;
	public static ByteArrayOutputStream baos;
	public static PrintStream ps;
	public static PrintStream old;
	
	public static JTextArea swiftTextArea	= new JTextArea();
	/*
	 * 
	 */
	
	//DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	//Calendar cal = Calendar.getInstance();
	
	// The Map contains the key as the selected row and value as the DataCenter.
	private static Map<Integer, Datacenter> dataCenterMap = new HashMap<>();
	
	public static void main(String[] args) {

		try{
			baos = new ByteArrayOutputStream();
			ps = new PrintStream(baos);
			System.setOut(ps);
			
		}catch (Exception e) {
			e.printStackTrace();
		}

		//Default Flavors
		/*
		 * Flavor size are Ephemeral Storage => the data stored here are lost ones the instance is terimated
		 * Persistent storage =>> Block , Object , file system storage 
		 */
		String[] nms = {"xLarge" , "Large" ,"Medium","Small","Tiny"};
		for(int i = 0 ; i < 5 ; i++ ){
			Flavor f = new Flavor(nms[i],
					Constants.VM_MIPS[i],
					Constants.VM_SIZE,
					Constants.VM_PES[i],
					Constants.VM_RAM[i],
					Constants.VM_BW);
			flaList.add(f);
			
		}
		
		//Default Image
		Image image = new Image("Ubuntu ", "src/org/opensim/ISO/ubuntu-16.04.1-desktop-amd64.iso", "ISO", "x86", 0 , 0 );
		imgList.add(image);
		
		
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
					GUI window = new GUI();
					window.frmOpensim.setTitle("OpenSim");
					window.frmOpensim.setSize(1300 , 600);
					window.frmOpensim.setVisible(true);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Major Error", "Error", JOptionPane.ERROR_MESSAGE);
					System.exit(0);
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings({ "deprecation"})
	private void initialize() {
		frmOpensim = new JFrame();
		frmOpensim.setResizable(false);
		frmOpensim.setTitle("Opensim");
		frmOpensim.setBounds(100, 100, 1000 , 500);
		frmOpensim.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JSplitPane jspMain = new JSplitPane();
		jspMain.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
		tabbedPane.setFont(new Font("Abyssinica SIL", Font.BOLD | Font.ITALIC, 20));
		tabbedPane.setBorder(null);
		jspMain.disable();
		jspMain.setResizeWeight(0.3);
		jspMain.setLeftComponent(tabbedPane);
		frmOpensim.getContentPane().add(jspMain, BorderLayout.CENTER);
		
		//Datacenter Details
		JPanel panel_9 = new JPanel();
		tabbedPane.addTab("Datacenter", null, panel_9, null);
		panel_9.setLayout(null);
		
		JLabel lblDatacenterConfiguration = new JLabel("DataCenter Configuration");
		lblDatacenterConfiguration.setBounds(52, 6, 601, 129);
		lblDatacenterConfiguration.setFont(new Font("Century Schoolbook L", Font.BOLD, 40));
		panel_9.add(lblDatacenterConfiguration);
		
		JTable tableDatacenter = new JTable(){
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int row , int col){
				return false;
			};
		};
		DefaultTableModel modelDatacenter = new DefaultTableModel();
		String DatacenterTableHeader[] = new String[]{"ID" , "Name" , "Location", "No.Rack","No.Of Devices"};
		modelDatacenter.setColumnIdentifiers(DatacenterTableHeader);
		tableDatacenter.setModel(modelDatacenter);
		for(int i = 0 ; i < Datacenter.datacenter_list.size(); i++){
			modelDatacenter.addRow(new Object[]{
					Datacenter.datacenter_list.get(i).getID(),Datacenter.datacenter_list.get(i).getName(),
					Datacenter.datacenter_list.get(i).getLocation(), Datacenter.datacenter_list.get(i).getNoofRack(),
					Datacenter.datacenter_list.get(i).getNoOfDevices()
			});
		}
		JScrollPane scrollPaneDatacenter = new JScrollPane(tableDatacenter);
		scrollPaneDatacenter.setBounds(50, 100, 600, 100);
		panel_9.add(scrollPaneDatacenter);
	
		JButton btnAddDatacenter = new JButton("Add DataCenter");
		btnAddDatacenter.setBounds(75, 500, 145, 40);
		panel_9.add(btnAddDatacenter);

		ActionListenerImpl actionListener = new ActionListenerImpl(this, modelDatacenter, dataCenterMap);
		btnAddDatacenter.addActionListener(actionListener);
		
		ListSelectionListenerImpl listSelectionListener = new ListSelectionListenerImpl(tableDatacenter, dataCenterMap);
		tableDatacenter.getSelectionModel().addListSelectionListener(listSelectionListener);
		JButton btnStartOpensim = new JButton("Start OpenSim");
		btnStartOpensim.setBounds(485, 500, 145, 40);
		panel_9.add(btnStartOpensim);
		
		/*
		 * To display Swift Configuration
		 */
		JScrollPane scrollPane_3 = new JScrollPane();
		scrollPane_3.setBounds(50, 210, 600, 285);
		swiftTextArea.setEditable(false);
		swiftTextArea.setFont(new Font("Serif", Font.BOLD,30));
		swiftTextArea.setTabSize(4);
		swiftTextArea.setText("\n\n\n\tSwift Configuration");
		panel_9.add(scrollPane_3);
		scrollPane_3.setViewportView(swiftTextArea);
		
		btnStartOpensim.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(dataCenterMap.isEmpty())
					JOptionPane.showMessageDialog(null, "No DataCenter Created for Simulation", "Error", JOptionPane.ERROR_MESSAGE);
				else{
					DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
					Calendar cal = Calendar.getInstance();
					textArea.append("\n"+dateFormat.format(cal.getTime())+"\nStarting Opensim..\n");
					for(int i = 0 ; i < 20; i++)
						textArea.append(" - ");
					tabbedPane.setEnabledAt(1, true);
					tabbedPane.setEnabledAt(2, true);
					tabbedPane.setSelectedIndex(1);
				}
			}
		});
		
		//End of Datacenter 
		//Logging Details
		JPanel LogPanel = new JPanel();
		
		jspMain.setRightComponent(LogPanel);
		LogPanel.setLayout(null);
		
		JLabel lblLogs = new JLabel("Logs : ");
		lblLogs.setFont(new Font("FreeSerif", Font.BOLD | Font.ITALIC, 29));
		lblLogs.setBounds(12, 12, 85, 36);
		LogPanel.add(lblLogs);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 60, 350, 500);
		LogPanel.add(scrollPane_1);
		
		textArea = new JTextArea();
		textArea.setFont(new Font("FreeSans", Font.ITALIC, 15));
		textArea.setBackground(UIManager.getColor("Button.background"));
		textArea.setEditable(false);
		//to display in the log from the System.out
		textArea.append(baos.toString());
		scrollPane_1.setViewportView(textArea);
				
		//end of Log Details
		
		//First Tab
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Home", null, panel, null);
		panel.setLayout(null);
		tabbedPane.setEnabledAt(1, false);
		JLabel lblOpenstack = new JLabel("OpenStack");
		lblOpenstack.setBounds(74, 12, 352, 86);
		lblOpenstack.setFont(new Font("Century Schoolbook L", Font.BOLD, 60));
		panel.add(lblOpenstack);
		
		JLabel lblOpensim = new JLabel("OpenSim");
		lblOpensim.setBounds(438, 12, 247, 86);
		lblOpensim.setFont(new Font("URW Chancery L", Font.BOLD, 60));
		panel.add(lblOpensim);
		
		JLabel lblNewLabel = new JLabel();
		ImageIcon ii = new ImageIcon(new ImageIcon("src/org/opensim/GUI/openstackFlowOpensim.jpeg").getImage().getScaledInstance(750, 400, 5));
		lblNewLabel.setIcon(ii);
		lblNewLabel.setBounds(12, 66 , 850, 500);
		panel.add(lblNewLabel);
		
		//End of Home Tab
		
		//Login Tab
		JPanel panel_5 = new JPanel();
		tabbedPane.addTab("Login", null, panel_5, null);
		panel_5.setLayout(null);
		tabbedPane.setEnabledAt(2, false);
		
		JPanel panel_6 = new JPanel();
		panel_6.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		panel_6.setBounds(125, 57, 452, 342);
		panel_5.add(panel_6);
		panel_6.setLayout(null);
		
		JLabel lblOpenstackLogin = new JLabel("OpenStack Login");
		lblOpenstackLogin.setBounds(119, 12, 207, 29);
		lblOpenstackLogin.setFont(new Font("Dialog", Font.BOLD, 20));
		panel_6.add(lblOpenstackLogin);
		
		JLabel lblUserName = new JLabel("User Name : ");
		lblUserName.setFont(new Font("Dialog", Font.BOLD, 20));
		lblUserName.setBounds(64, 70, 168, 29);
		panel_6.add(lblUserName);
		
		JLabel lblPassword = new JLabel("Password : ");
		lblPassword.setFont(new Font("Dialog", Font.BOLD, 20));
		lblPassword.setBounds(64, 125, 156, 29);
		panel_6.add(lblPassword);
		
		textField = new JTextField();
		textField.setBounds(250, 70, 138, 26);
		panel_6.add(textField);
		textField.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(250, 125, 138, 26);
		panel_6.add(passwordField);
		
		JButton btnLogin = new JButton("Log In");
		btnLogin.setFont(new Font("FreeSerif", Font.BOLD | Font.ITALIC, 25));
		btnLogin.setBounds(64, 188, 324, 48);
		panel_6.add(btnLogin);

	
		btnLogin.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				userName = textField.getText();
				passWord = passwordField.getText();
				lblUserNameNovaConf.setText("User Name : "+userName);
				lblUserNameNova.setText("User Name : "+userName);
				
				DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
				Calendar cal = Calendar.getInstance();
				textArea.append("\n"+dateFormat.format(cal.getTime())+"\n");
				
				textArea.append("\nAuthenticating with Keystone..\n");
				
				auth = keyStone.authenticateUser(userName, passWord);
				
				textArea.append("\nKeystone authentication done.\n");
				
				if(textField.getText().isEmpty() || passwordField.getText().isEmpty()){
					textArea.append("\nInvalid authentication\n");
				}else if(auth == 1){
					
					if(isLogged == 1){
						textArea.append("\nLog out First\n");
					}
					else{
						isLogged = 1;
					//Check for the auth of user Login
						textArea.append("Logged in as \n"+ userName+ "\n");
						textField.setText("");
						passwordField.setText("");
						if(userName.equalsIgnoreCase("admin")){
							tabbedPane.setEnabledAt(2, true);
							tabbedPane.setEnabledAt(3, true);
							tabbedPane.setEnabledAt(4, true);
							tabbedPane.setEnabledAt(5, true);
						}
						else{
							tabbedPane.setEnabledAt(2, true);
							tabbedPane.setEnabledAt(3, false);
							tabbedPane.setEnabledAt(4, true);
							tabbedPane.setEnabledAt(5, true);
				
						}
					}
				}
				else{
					textArea.append("\nNo Login Permission\n");
				}
				for(int i = 0 ; i < 20; i++)
					textArea.append(" - ");
			}
		});
		
		
		JButton btnNewButton = new JButton("Log Out");
		btnNewButton.setFont(new Font("FreeSerif", Font.BOLD | Font.ITALIC, 25));
		btnNewButton.setBounds(64, 248, 324, 48);
		panel_6.add(btnNewButton);
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//action to Log out
				if(isLogged == 1){
					DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
					Calendar cal = Calendar.getInstance();
					textArea.append("\n"+dateFormat.format(cal.getTime())+"\n");
					textArea.append("\nSuccessfully Logged out\n");
					for(int i = 0 ; i < 20; i++)
						textArea.append(" - ");
				}
				isLogged = 0;
				tabbedPane.setEnabledAt(3, false);
				tabbedPane.setEnabledAt(4, false);
				tabbedPane.setEnabledAt(5, false);
			}
		});
		
		//End of log tab
		
		//Adding user by Admin
		JPanel panel_8 = new JPanel();
		tabbedPane.addTab("Add User", null, panel_8, null);
		tabbedPane.setEnabledAt(3, false); //false
		panel_8.setLayout(null);
		
		JLabel lblUsername = new JLabel("UserName : ");
		lblUsername.setFont(new Font("Gentium Book Basic", Font.PLAIN, 20));
		lblUsername.setBounds(94, 112, 110, 30);
		panel_8.add(lblUsername);
		
		JLabel lblPassword_1 = new JLabel("Password : ");
		lblPassword_1.setFont(new Font("Gentium Book Basic", Font.PLAIN, 20));
		lblPassword_1.setBounds(94, 154, 110, 29);
		panel_8.add(lblPassword_1);
		
		textField_4 = new JTextField();
		textField_4.setBounds(222, 110, 114, 32);
		panel_8.add(textField_4);
		textField_4.setColumns(10);
		
		passwordField_1 = new JPasswordField();
		passwordField_1.setBounds(222, 152, 114, 31);
		panel_8.add(passwordField_1);
		
		JLabel lblAddNewUser = new JLabel("ADD NEW USER ");
		lblAddNewUser.setFont(new Font("Gentium Book Basic", Font.BOLD, 25));
		lblAddNewUser.setBounds(94, 52, 242, 46);
		panel_8.add(lblAddNewUser);
		
		JButton btnCreateUser = new JButton("Create User");
		btnCreateUser.setFont(new Font("FreeSerif", Font.ITALIC, 25));
		btnCreateUser.setBounds(94, 344, 184, 46);
		panel_8.add(btnCreateUser);
	
		JLabel lblFlavorPermission = new JLabel("Flavor Permission : ");
		lblFlavorPermission.setFont(new Font("Gentium Book Basic", Font.PLAIN, 18));
		lblFlavorPermission.setBounds(94, 218, 149, 15);
		panel_8.add(lblFlavorPermission);
		
		JCheckBox chckbxAllowDenyFlavor = new JCheckBox("Allow / Deny", true);
		chckbxAllowDenyFlavor.setFont(new Font("Gentium Book Basic", Font.PLAIN, 18));
		chckbxAllowDenyFlavor.setBounds(320, 218, 129, 23);
		panel_8.add(chckbxAllowDenyFlavor);
		
		JLabel lblNewLabel_1 = new JLabel("Image Permission : ");
		lblNewLabel_1.setFont(new Font("Gentium Book Basic", Font.PLAIN, 18));
		lblNewLabel_1.setBounds(94, 245, 149, 15);
		panel_8.add(lblNewLabel_1);
		
		JCheckBox chckbxAllowDenyImage = new JCheckBox("Allow / Deny", true);
		chckbxAllowDenyImage.setFont(new Font("Gentium Book Basic", Font.PLAIN, 18));
		chckbxAllowDenyImage.setBounds(320, 245, 129, 23);
		panel_8.add(chckbxAllowDenyImage);
		
		JLabel lblVolumePermission = new JLabel("Volume Permission : ");
		lblVolumePermission.setFont(new Font("Gentium Book Basic", Font.PLAIN, 18));
		lblVolumePermission.setBounds(94, 272, 184, 15);
		panel_8.add(lblVolumePermission);
		
		JLabel lblContainerPermission = new JLabel("Container Permission : ");
		lblContainerPermission.setFont(new Font("Gentium Book Basic", Font.PLAIN, 18));
		lblContainerPermission.setBounds(94, 272, 184, 15);
		panel_8.add(lblContainerPermission);
		
		JCheckBox chckbxAllowDenyVolume = new JCheckBox("Allow / Deny" , true);
		chckbxAllowDenyVolume.setFont(new Font("Gentium Book Basic", Font.PLAIN, 18));
		chckbxAllowDenyVolume.setBounds(320, 272, 129, 23);
		panel_8.add(chckbxAllowDenyVolume);
		
		JLabel lblInstancePermission = new JLabel("Instance Permission : ");
		lblInstancePermission.setFont(new Font("Gentium Book Basic", Font.PLAIN, 18));
		lblInstancePermission.setBounds(94, 299, 166, 15);
		panel_8.add(lblInstancePermission);
		
		JCheckBox chckbxAllowDenyInstance = new JCheckBox("Allow / Deny" , true);
		chckbxAllowDenyInstance.setFont(new Font("Gentium Book Basic", Font.PLAIN, 18));
		chckbxAllowDenyInstance.setBounds(320, 299, 129, 23);
		panel_8.add(chckbxAllowDenyInstance);
		
		btnCreateUser.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				String tempUserName = textField_4.getText();
				String tempPassWord = passwordField_1.getText();
				String authFlavor = "N";
				String authImage = "N";
				String authVolume = "N";
				String authInstance = "N";
				if(chckbxAllowDenyFlavor.isSelected())
					authFlavor = "Y";
				else
					authFlavor = "N";
				if(chckbxAllowDenyImage.isSelected())
					authImage = "Y";
				else
					authImage = "N";
				if(chckbxAllowDenyVolume.isSelected())
					authVolume = "Y";
				else
					authVolume = "N";
				if(chckbxAllowDenyInstance.isSelected())
					authInstance = "Y";
				else
					authInstance = "N";
				int userCreated = keyStone.createUser(tempUserName, tempPassWord, authFlavor, authImage, authVolume, authInstance);
				DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
				Calendar cal = Calendar.getInstance();
				textArea.append("\n"+dateFormat.format(cal.getTime())+"\n");
				if(userCreated == 0)
					textArea.append("\nUserName already exist\n");
				else if (userCreated == -1)
					textArea.append("\nInvalid Input\n");
				else
					textArea.append("\nNew User Added\n");
				for(int i = 0 ; i < 20; i++)
					textArea.append(" - ");
			}
		});
		
		//End of Add User
		
		
		//Configuration Tab
		panelConf = new JPanel();
		tabbedPane.addTab("NovaConf", null, panelConf, null);
		tabbedPane.setEnabledAt(4, false);
		panelConf.setLayout(null);
		tabbedPane_1 = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane_1.setBounds(25, 75, 700, 400);
		tabbedPane_1.setFont(new Font("Century Schoolbook L", Font.ITALIC, 15));
		panelConf.add(tabbedPane_1);
		
		
		lblUserNameNovaConf.setFont(new Font("Khmer OS System", Font.ITALIC, 25));
		lblUserNameNovaConf.setBounds(15, 15, 500, 30);
		panelConf.add(lblUserNameNovaConf);
		
		
		
		
		
				
		//Configuration Flavor Tab
		JPanel panel_3 = new JPanel();
		tabbedPane_1.addTab("Create Flavor", null, panel_3, null);
		panel_3.setLayout(null);
		JLabel lblFlavors = new JLabel("FLAVORS");
		lblFlavors.setFont(new Font("Abyssinica SIL", Font.BOLD | Font.ITALIC, 20));
		lblFlavors.setBounds(206, 12, 131, 22);
		panel_3.add(lblFlavors);
		JTable table1 = new JTable(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row , int col){
				return false;
			};
		};
		DefaultComboBoxModel<String> flavorBoxList = new DefaultComboBoxModel<>();
		DefaultTableModel model1 = new DefaultTableModel();
		String Header[] = new String[]{"Name" ,
				"MIPS","Size","VCPUs","RAM"};
		model1.setColumnIdentifiers(Header);
		table1.setModel(model1);
		for(int i = 0 ; i < flaList.size(); i++){
			model1.addRow(new Object[]{
					flaList.get(i).getName(),flaList.get(i).getMips(),flaList.get(i).getSize(),flaList.get(i).getPes(),
					flaList.get(i).getRam()
			});
			flavorBoxList.addElement(flaList.get(i).getName());
		}
		JScrollPane scrollPane = new JScrollPane(table1);
		scrollPane.setBounds(52, 45, 600, 100);
		panel_3.add(scrollPane);
		//End of Flavor table
		JLabel lblFlavorName = new JLabel("Flavor Name :");
		lblFlavorName.setFont(new Font("FreeSans", Font.ITALIC, 18));
		lblFlavorName.setBounds(52, 167, 157, 21);
		panel_3.add(lblFlavorName);
		textField_1 = new JTextField();
		textField_1.setBounds(301, 157, 114, 25);
		panel_3.add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblMips = new JLabel("MIPS : ");
		lblMips.setFont(new Font("FreeSans", Font.ITALIC, 18));
		lblMips.setBounds(52, 200, 157, 15);
		panel_3.add(lblMips);
		textField_2 = new JTextField("0");
		textField_2.setBounds(301, 190, 114, 25);
		panel_3.add(textField_2);
		
		JLabel lblSize = new JLabel("Size :");
		lblSize.setFont(new Font("FreeSans", Font.ITALIC, 18));
		lblSize.setBounds(52, 229, 157, 15);
		panel_3.add(lblSize);
		textField_3 = new JTextField("0");
		textField_3.setBounds(301, 219, 114, 25);
		panel_3.add(textField_3);
		textField_3.setColumns(10);

		JLabel lblPesno = new JLabel("Pes.No :");
		lblPesno.setFont(new Font("FreeSans", Font.ITALIC, 18));
		lblPesno.setBounds(52, 260, 157, 15);
		panel_3.add(lblPesno);
		//Integer[] pes_no_list  = {0,1,2,3,4,5,6,7,8};
		Integer[] pes_no_list = new Integer[100];
		for(int i=0;i<100;i++)
			pes_no_list[i] = i;
		JComboBox<?> jcb1 = new JComboBox<Object>(pes_no_list);
		jcb1.setBounds(301, 256, 61, 19);
		jcb1.setSelectedIndex(0);
		panel_3.add(jcb1);
		
		JLabel lblRam = new JLabel("RAM  (MB) : ");
		lblRam.setFont(new Font("FreeSans", Font.ITALIC, 18));
		lblRam.setBounds(52, 291, 157, 15);
		panel_3.add(lblRam);
		textField_5 = new JTextField("0");
		textField_5.setBounds(301, 287, 114, 25);
		panel_3.add(textField_5);
		textField_5.setColumns(10);
		try{
		JButton btnCreateButton = new JButton("Create Flavor");
		btnCreateButton.setFont(new Font("FreeSans", Font.ITALIC, 20));
		btnCreateButton.setBounds(52, 321, 177, 33);
		panel_3.add(btnCreateButton);
		
		btnCreateButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try{
				nflaname = textField_1.getText();
				mips = Double.parseDouble(textField_2.getText());
				size = Long.parseLong(textField_3.getText());
				pes_no = (int) jcb1.getSelectedItem();
				ram = Integer.parseInt(textField_5.getText());
				bw = Constants.VM_BW;
				}catch (Exception exception) {
					JOptionPane.showMessageDialog(null, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
				if(nflaname != "" && mips > 0 && size > 0 && pes_no > 0 && ram > 0 && bw > 0){
					DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
					Calendar cal = Calendar.getInstance();
					textArea.append("\n"+dateFormat.format(cal.getTime())+"\n");
					//Auth for Flavor creation from keystone
					textArea.append("\nAuthenticating with Keystone..\n");
					
					int auth = keyStone.authenticateUserForComponent(userName, "FLAVOR");
					if(auth == 1){
					textArea.append("\n" + userName + " has permission to create Flavor\n");
					textArea.append("New Flavor created\nAdded to Flavor Db\n");
					Flavor f = new Flavor(nflaname, mips, size, pes_no, ram, bw);
					flaList.add(f);	
				model1.addRow(new Object[]{
						nflaname,mips,size,pes_no,ram
				});
				flavorBoxList.addElement(nflaname);
				textField_2.setText("0");
				jcb1.setSelectedIndex(0);
				}
				else{
					JOptionPane.showMessageDialog(null, "Flavor creation denied by Keystone", "Error", JOptionPane.ERROR_MESSAGE);
					textField_2.setText("0");
					jcb1.setSelectedIndex(0);
				}
					for(int i = 0 ; i < 20; i++)
						textArea.append(" - ");
			}else{
				JOptionPane.showMessageDialog(null, "Invalid Input", "Error", JOptionPane.ERROR_MESSAGE);
			}
			}
		});
		}catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}

		//End of Flavor Tab
		
		//Configuration Image Tab
		JPanel panel_2 = new JPanel();
		tabbedPane_1.addTab("Create Image", null, panel_2, null);
		panel_2.setLayout(null);
		JLabel lblImage = new JLabel("IMAGES ");
		lblImage.setFont(new Font("Abyssinica SIL", Font.BOLD | Font.ITALIC, 20));
		lblImage.setBounds(206, 12, 114, 22);
		panel_2.add(lblImage);
		JTable table2 = new JTable(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int col){
				return false;
			};
		};
		DefaultTableModel model2 = new DefaultTableModel();
		DefaultComboBoxModel<String> boxModelImage = new DefaultComboBoxModel<>();
		String Header2[] = new String[]{"Name" ,
				"Image Location","Format","Architecture","MinDisk","MinRAM"};
		model2.setColumnIdentifiers(Header2);
		table2.setModel(model2);
		for(int i = 0 ; i < imgList.size(); i++){
			model2.addRow(new Object[]{
					imgList.get(i).getName() , imgList.get(i).getImgLoc() , imgList.get(i).getFormat() , imgList.get(i).getArch() ,
					imgList.get(i).getMinDisk() , imgList.get(i).getMinRam()
			});
			boxModelImage.addElement(imgList.get(i).getName());
		}
		JScrollPane scrollPane2 = new JScrollPane(table2);
		scrollPane2.setBounds(52, 45, 600, 100);
		panel_2.add(scrollPane2);
		
		JLabel lblImageName = new JLabel("Image Name :");
		lblImageName.setFont(new Font("FreeSans", Font.ITALIC, 18));
		lblImageName.setBounds(52, 165, 150, 19);
		panel_2.add(lblImageName);
		JTextField textField_7 = new JTextField();
		textField_7.setBounds(300, 164, 114, 22);
		panel_2.add(textField_7);
		textField_7.setColumns(10);
		
		JLabel lblimglocation = new JLabel("Image Location :");
		lblimglocation.setFont(new Font("FreeSans", Font.ITALIC, 18));
		lblimglocation.setBounds(52, 198, 150, 19);
		panel_2.add(lblimglocation);
		
		JButton imgLocButton = new JButton("Browse");
		imgLocButton.setBounds(300 , 198 , 114 , 21);
		panel_2.add(imgLocButton);
		imgLocButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter(
						"AKI","AMI","ARI","ISO","QCOW2","RAW","VDI","VHD","VHDX","VMDK" ,
						"AKI","AMI","ARI","ISO","QCOW2","RAW","VDI","VHD","VHDX","VMDK");
				chooser.setFileFilter(extensionFilter);
				chooser.setMultiSelectionEnabled(false);
				int option = chooser.showOpenDialog(GUI.this);
				if(option == JFileChooser.APPROVE_OPTION){
					File fileLoc = chooser.getSelectedFile();
					imgLoc = fileLoc.getAbsolutePath();
				}
				
			}
		});
		
		JLabel lblFormat = new JLabel("Format :");
		lblFormat.setFont(new Font("FreeSans", Font.ITALIC, 18));
		lblFormat.setBounds(52, 227, 70, 19);
		panel_2.add(lblFormat);
		String[] format_list = {"AKI","AMI","ARI","ISO","QCOW2","RAW","VDI","VHD","VHDX","VMDK"};
		JComboBox<?> jcb_Format = new JComboBox<Object>(format_list);
		jcb_Format.setSelectedIndex(0);
		jcb_Format.setBounds(300,226,114,22);
		panel_2.add(jcb_Format);
	
		JLabel lblarch = new JLabel("Architecture :");
		lblarch.setFont(new Font("FreeSans", Font.ITALIC, 18));
		lblarch.setBounds(52, 261, 150, 20);
		panel_2.add(lblarch);
		String[] arch_list = {"x86_64","arm","ppc64"};
		JComboBox<?> jcb_arch = new JComboBox<Object>(arch_list);
		jcb_arch.setSelectedIndex(0);
		jcb_arch.setBounds(300, 260, 114, 24);
		panel_2.add(jcb_arch);

		JLabel lblminDisk = new JLabel("Min_Disk : ");
		lblminDisk.setFont(new Font("FreeSans", Font.ITALIC, 18));
		lblminDisk.setBounds(52, 293, 150, 17);
		panel_2.add(lblminDisk);
		JTextField textField_11 = new JTextField("0");
		textField_11.setBounds(300, 292, 114, 21);
		panel_2.add(textField_11);
		textField_11.setColumns(10);
		
		JLabel lblminRam = new JLabel("Min_RAM : ");
		lblminRam.setFont(new Font("FreeSans", Font.ITALIC, 18));
		lblminRam.setBounds(52, 321, 107, 19);
		panel_2.add(lblminRam);
		JTextField textField_12 = new JTextField("0");
		textField_12.setBounds(300, 321, 114, 23);
		panel_2.add(textField_12);
		textField_12.setColumns(10);
		try{
		JButton btnCreateImage = new JButton("Create Image");
		btnCreateImage.setFont(new Font("FreeSans", Font.ITALIC, 18));
		btnCreateImage.setBounds(463, 207, 157, 60);
		panel_2.add(btnCreateImage);
		
		btnCreateImage.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try{
				imgnm = textField_7.getText();
				format = (String) jcb_Format.getSelectedItem();
				arch = (String) jcb_arch.getSelectedItem();
				minDisk = Integer.parseInt(textField_11.getText());
				minRam = Integer.parseInt(textField_12.getText());
				}catch (Exception exception) {
					JOptionPane.showMessageDialog(null, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
				try{
					if(imgnm.equals("") || imgLoc.equals("")){
						//Do nothing 
						JOptionPane.showMessageDialog(null, "Invalid input", "Error", JOptionPane.ERROR_MESSAGE);
					}
					else{
						DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
						Calendar cal = Calendar.getInstance();
						textArea.append("\n"+dateFormat.format(cal.getTime())+"\n");
						//Auth for image creation
						textArea.append("\nAuthenticating with Keystone..\n");
						int auth = keyStone.authenticateUserForComponent(userName, "IMAGE");
						if(auth == 1){
							textArea.append("\n" + userName + " has permission to create Image\n");
							textArea.append("New Image created\nAdded to Glance Db\n");
							Image img = new Image(imgnm, imgLoc, format, arch, minDisk, minRam);
							imgList.add(img);
							model2.addRow(new Object[]{
									imgnm,imgLoc,format,arch,minDisk,minRam
							});
							boxModelImage.addElement(imgnm);
							textField_7.setText("");
							}
						else{
							JOptionPane.showMessageDialog(null, "Image creation denied by Keystone", "Error", JOptionPane.ERROR_MESSAGE);
							textField_12.setText("0");
							textField_7.setText("");
						}
						for(int i = 0 ; i < 20; i++)
							textArea.append(" - ");
					}
				}catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "Invalid input", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		}catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
		
	
		//End of Image Tab
		
		//Configuration Volume Tab
		
		JPanel panel_1 = new JPanel();
		tabbedPane_1.addTab("Create Volume", null, panel_1, null);
		panel_1.setLayout(null);
		JLabel lblVolume = new JLabel("VOLUMES ");
		lblVolume.setFont(new Font("Abyssinica SIL", Font.BOLD | Font.ITALIC, 20));
		lblVolume.setBounds(206, 12, 140, 22);
		panel_1.add(lblVolume);
		JTable table3 = new JTable(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int col){
				return false;
			};
		};
		DefaultTableModel model3 = new DefaultTableModel();
		DefaultComboBoxModel<String> boxModelVolume = new DefaultComboBoxModel<>();
		String Header3[] = new String[]{"Volume Name", "Volume ID" ,"Size" , " Type " , "Attached to VmID "};
		model3.setColumnIdentifiers(Header3);
		table3.setModel(model3);
		for(int i = 0 ; i < volList.size(); i++){
			model3.addRow(new Object[]{ volList.get(i).getName() ,
				 volList.get(i).getID() , volList.get(i).getSize() , volList.get(i).getType(),  volList.get(i).getVMID()
			});
			
			boxModelVolume.addElement(volList.get(i).getName());
			
		}
		JScrollPane scrollPane3 = new JScrollPane(table3);
		scrollPane3.setBounds(52, 45, 600, 100);
		panel_1.add(scrollPane3);
		
		JLabel lblVolumeName = new JLabel("Volume Name :");
		lblVolumeName.setFont(new Font("FreeSans", Font.ITALIC, 18));
		lblVolumeName.setBounds(52, 167, 150, 21);
		panel_1.add(lblVolumeName);
		JTextField textField_13 = new JTextField();
		textField_13.setBounds(299, 162, 114, 26);
		panel_1.add(textField_13);
		textField_13.setColumns(10);
		
		JLabel lblVolSize = new JLabel("Volume Size(MB) :");
		lblVolSize.setFont(new Font("FreeSans", Font.ITALIC, 18));
		lblVolSize.setBounds(52, 200, 170, 21);
		panel_1.add(lblVolSize);
		JTextField textField_14 = new JTextField();
		textField_14.setBounds(299, 195, 114, 26);
		textField_14.setText("-1");
		panel_1.add(textField_14);
		textField_14.setColumns(10);
		
		JButton btnCreateVolume = new JButton("Create Volume ");
		btnCreateVolume.setFont(new Font("FreeSans", Font.ITALIC, 18));
		btnCreateVolume.setBounds(52, 279, 247, 31);
		panel_1.add(btnCreateVolume);
		
		String[] volume_Type = {"RAW","LVM","iSCSR","ScaleIO","nfs"};
		JLabel lblVolumeType = new JLabel("Volume Type : ");
		lblVolumeType.setFont(new Font("FreeSans", Font.ITALIC, 18));
		lblVolumeType.setBounds(52, 233, 150, 22);
		panel_1.add(lblVolumeType);
		
		JComboBox<?> comboBox_3 = new JComboBox<Object>(volume_Type);
		comboBox_3.setSelectedIndex(0);
		comboBox_3.setBounds(299, 224, 114, 31);
		panel_1.add(comboBox_3);
		
		
		btnCreateVolume.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent ae) {
			try{
				volName = textField_13.getText();
				volSize = Integer.parseInt(textField_14.getText());	
			}catch (Exception e) {
				JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
			if(volSize > 0){
				DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
				Calendar cal = Calendar.getInstance();
				textArea.append("\n"+dateFormat.format(cal.getTime())+"\n");
				//Auth for Volume
				textArea.append("\nAuthenticating with Keystone..\n");
				int auth = keyStone.authenticateUserForComponent(userName, "VOLUME");
				if(auth == 1){
				textArea.append("\n" + userName + " has permission to create Cinder Volume \n");
				try{
					//Randomly select the Server ( PVR )
					int cr_no = API.create_volume(comboBox_3.getSelectedItem().toString(), volSize);
					Volume v = new Volume(cr_no, volName, volSize, comboBox_3.getSelectedItem().toString(), -1);
					volList.add(v);
					model3.addRow(new Object[] { volName , cr_no , volSize , comboBox_3.getSelectedItem().toString() , volList.get(cr_no).getVMID() });
					boxModelVolume.addElement(volName);
					volSize = -1;
					textArea.append("New Cinder Volume created\nVolume not attached to any Instance\n");
				}catch (Exception e) {
					JOptionPane.showMessageDialog(null, "A Volume of Size "+volSize+" cannot be created..", "Error", JOptionPane.ERROR_MESSAGE);
				}
				textField_13.setText("");
				textField_14.setText("-1");
				} else {
					JOptionPane.showMessageDialog(null, "Volume creation denied by Keystone", "Error", JOptionPane.ERROR_MESSAGE);
					textField_14.setText("-1");
				}
				for(int i = 0 ; i < 20; i++)
					textArea.append(" - ");
			}else{
				JOptionPane.showMessageDialog(null, "Invalid Input", "Error", JOptionPane.ERROR_MESSAGE);

			}
			}
		});
		
		//End of Volume Tab
		
		//Configuration Swift Tab
		
				//Swift.fillObjList();
				JPanel panelSwift = new JPanel();
				tabbedPane_1.addTab("Create Object", null, panelSwift, null);
				panelSwift.setLayout(null);
				JLabel lblContainer = new JLabel("Object ");
				lblContainer.setFont(new Font("Abyssinica SIL", Font.BOLD | Font.ITALIC, 20));
				lblContainer.setBounds(206, 12, 140, 22);
				panelSwift.add(lblContainer);
				JTable tableS = new JTable(){
					private static final long serialVersionUID = 1L;

					public boolean isCellEditable(int row, int col){
						return false;
					};
				};
				
				
				
				JScrollPane scrollPaneS = new JScrollPane(tableS);
				scrollPaneS.setBounds(52, 45, 600, 100);
				panelSwift.add(scrollPaneS);
				
				JLabel lblContainerName = new JLabel("Container Name:");
				lblContainerName.setFont(new Font("FreeSans", Font.ITALIC, 18));
				lblContainerName.setBounds(52, 170, 140, 31);
				panelSwift.add(lblContainerName);
				
				JTextField textFieldS = new JTextField();
				textFieldS.setBounds(210, 170, 140, 31);
				panelSwift.add(textFieldS);
				textFieldS.setColumns(10);
				
				JButton btnCreateContainer = new JButton("Create");
				btnCreateContainer.setFont(new Font("FreeSans", Font.ITALIC, 18));
				btnCreateContainer.setBounds(540, 170, 100, 31);
				panelSwift.add(btnCreateContainer);
				
				JButton btnDeleteContainer = new JButton("Delete");
				btnDeleteContainer.setFont(new Font("FreeSans", Font.ITALIC, 18));
				btnDeleteContainer.setBounds(540, 230, 100, 31);
				panelSwift.add(btnDeleteContainer);

				
				JLabel lblContainerType = new JLabel("Containers: ");
				lblContainerType.setFont(new Font("FreeSans", Font.ITALIC, 18));
				lblContainerType.setBounds(52, 230, 150, 31);
				panelSwift.add(lblContainerType);
				
				String[] container_Type = Swift.getAccount();
				JComboBox<Object> comboBoxS = new JComboBox<Object>(container_Type);
				//comboBoxS.setEditable(true);
				comboBoxS.setSelectedIndex(0);
				comboBoxS.setBounds(210, 230, 114, 31);
				panelSwift.add(comboBoxS);
				
				DefaultTableModel modelS = new DefaultTableModel();
				DefaultComboBoxModel<String> boxModelContainer = new DefaultComboBoxModel<>();
				String HeaderS[] = new String[]{"Object Name", "Size" ,"Content Type"};
				modelS.setColumnIdentifiers(HeaderS);
				tableS.setModel(modelS);
				
				List<String[]> objList = Swift.getContainer(container_Type[0]);
				for(String[] obj: objList){
					modelS.addRow(new Object[]{obj[0], obj[1], obj[2]});
				}
				
				JButton btnAddFile = new JButton("Add File ");
				btnAddFile.setFont(new Font("FreeSans", Font.ITALIC, 18));
				btnAddFile.setBounds(52, 280, 110, 31);
				panelSwift.add(btnAddFile);
				
				JTextField textFieldS2 = new JTextField();
				textFieldS2.setBounds(210, 280, 140, 31);
				panelSwift.add(textFieldS2);
				textFieldS2.setColumns(10);
				
				JButton btnUploadFile = new JButton("Upload");
				btnUploadFile.setFont(new Font("FreeSans", Font.ITALIC, 18));
				btnUploadFile.setBounds(52, 330, 100, 31);
				panelSwift.add(btnUploadFile);
				
				JLabel lblObjectName = new JLabel("Object Name:");
				lblObjectName.setFont(new Font("FreeSans", Font.ITALIC, 18));
				lblObjectName.setBounds(370, 280, 120, 31);
				panelSwift.add(lblObjectName);
				
				JTextField textFieldS3 = new JTextField();
				textFieldS3.setBounds(500, 280, 140, 31);
				panelSwift.add(textFieldS3);
				textFieldS3.setColumns(10);
				
				JButton btnShowObject = new JButton("Inspect");
				btnShowObject.setFont(new Font("FreeSans", Font.ITALIC, 18));
				btnShowObject.setBounds(370, 330, 100, 31);
				panelSwift.add(btnShowObject);
				
				JButton btnRemoveObject = new JButton("Remove");
				btnRemoveObject.setFont(new Font("FreeSans", Font.ITALIC, 18));
				btnRemoveObject.setBounds(540, 330, 100, 31);
				panelSwift.add(btnRemoveObject);
				
				btnCreateContainer.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent ae) {
					try{
						String conName = textFieldS.getText();
						if(Swift.createContainer(conName)==1) {
							comboBoxS.addItem(conName);
						}
						else{
							JOptionPane.showMessageDialog(null, "Container creation not performed", "Error", JOptionPane.ERROR_MESSAGE);
						}
						textFieldS.setText("");
						
					}catch (Exception e) {
						JOptionPane.showMessageDialog(null, "E 1", "Error", JOptionPane.ERROR_MESSAGE);
					}
					}
				});
				
				comboBoxS.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent ae) {
					try{
						modelS.setRowCount(0);
						String conName = String.valueOf(comboBoxS.getSelectedItem());
						List<String[]> newObjList = Swift.getContainer(conName);
						for(String[] obj: newObjList){
							modelS.addRow(new Object[]{obj[0], obj[1], obj[2]});
						}
						
					}catch (Exception e) {
						JOptionPane.showMessageDialog(null, "E 2", "Error", JOptionPane.ERROR_MESSAGE);
					}
					}
				});
				
				btnDeleteContainer.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent ae) {
					try{
						String conName = String.valueOf(comboBoxS.getSelectedItem());
						if(Swift.deleteContainer(conName)==1) {
							comboBoxS.removeItem(comboBoxS.getSelectedItem());
							modelS.setRowCount(0);
							conName = String.valueOf(comboBoxS.getSelectedItem());
							List<String[]> newObjList = Swift.getContainer(conName);
							for(String[] obj: newObjList){
								modelS.addRow(new Object[]{obj[0], obj[1], obj[2]});
							}
						}
						else{
							JOptionPane.showMessageDialog(null, "Container deletion not performed", "Error", JOptionPane.ERROR_MESSAGE);
						}
						
					}catch (Exception e) {
						JOptionPane.showMessageDialog(null, "E 3", "Error", JOptionPane.ERROR_MESSAGE);
					}
					}
				});
				
				
				
				btnAddFile.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						try{	
						JFileChooser chooser = new JFileChooser("/home/kaveri/Desktop");
						chooser.setMultiSelectionEnabled(false);
						int option = chooser.showOpenDialog(GUI.this);
						if(option == JFileChooser.APPROVE_OPTION){
							File fileLoc = chooser.getSelectedFile();
							imgLoc = fileLoc.getAbsolutePath();
							textFieldS2.setText(imgLoc.toString());
							
						}
						}
						catch (Exception e) {
							JOptionPane.showMessageDialog(null, "E 4", "Error", JOptionPane.ERROR_MESSAGE);
						}
						
					}
				});
				
				btnUploadFile.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent ae) {
					try{
						String conName = String.valueOf(comboBoxS.getSelectedItem());
						String filePath = textFieldS2.getText();
						if(Swift.createObject(conName, filePath)==1) {
							modelS.setRowCount(0);
							List<String[]> newObjList = Swift.getContainer(conName);
							for(String[] obj: newObjList){
								modelS.addRow(new Object[]{obj[0], obj[1], obj[2]});
							}
							textFieldS2.setText("");
						}
					}catch (Exception e) {
						JOptionPane.showMessageDialog(null, "E 5"+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					}
					}
				});
				
				btnShowObject.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent ae) {
					try{
						String conName = String.valueOf(comboBoxS.getSelectedItem());
						String objName = textFieldS3.getText();
						JTextArea msg = new JTextArea(Swift.getObject(conName, objName));
						msg.setLineWrap(true);
						msg.setWrapStyleWord(true);
						msg.setFont(new Font("FreeSans", Font.ITALIC, 20));
						msg.setSize(800, 600);
						JScrollPane scrollPane = new JScrollPane(msg);
						JOptionPane.showMessageDialog(null, scrollPane, textFieldS3.getText(), JOptionPane.PLAIN_MESSAGE);
					}catch (Exception e) {
						JOptionPane.showMessageDialog(null, "E 6", "Error", JOptionPane.ERROR_MESSAGE);
					}
					}
				});
				
				btnRemoveObject.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent ae) {
					try{
						String conName = String.valueOf(comboBoxS.getSelectedItem());
						String objName = textFieldS3.getText();
						if(Swift.deleteObject(conName, objName)==1) {
							modelS.setRowCount(0);
							List<String[]> newObjList = Swift.getContainer(conName);
							for(String[] obj: newObjList){
								modelS.addRow(new Object[]{obj[0], obj[1], obj[2]});
							}
							textFieldS3.setText("");
						}
					}catch (Exception e) {
						JOptionPane.showMessageDialog(null, "E 7", "Error", JOptionPane.ERROR_MESSAGE);
					}
					}
				});
				
				
				//End of Swift Tab

		
		
		//Nova 
		JPanel panelNova = new JPanel();
		tabbedPane.addTab("Nova", null, panelNova, null);
		/*
		 * after keystone enable the tabbed panes
		 */
		tabbedPane.setEnabledAt(5, false);
		panelNova.setLayout(null);
		
		tabbedPane_2 = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane_2.setFont(new Font("Century Schoolbook L", Font.ITALIC, 15));
		tabbedPane_2.setBounds(25, 75, 700, 450);
		panelNova.add(tabbedPane_2);
	
		lblUserNameNova.setFont(new Font("Khmer OS System", Font.ITALIC, 25));
		lblUserNameNova.setBounds(15, 15, 500, 30);
		panelNova.add(lblUserNameNova);
		
		
		//Nova Create Instance tab
		JPanel panel_4 = new JPanel();
		tabbedPane_2.addTab("Create Instance ", null, panel_4, null);
		panel_4.setLayout(null);
		JLabel lblInstance = new JLabel("INSTANCES ");
		lblInstance.setFont(new Font("Abyssinica SIL", Font.BOLD | Font.ITALIC, 20));
		lblInstance.setBounds(206, 12, 159, 22);
		panel_4.add(lblInstance);
		JTable table4 = new JTable(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int col){
				return false;
			};
		};
		DefaultTableModel model4 = new DefaultTableModel();
		String Header4[] = new String[]{"ID","Name " ,"RAM " ,"Floating IP " ,"VCPUs " ,"Flavor Size"};
		model4.setColumnIdentifiers(Header4);
		table4.setModel(model4);
		for(int i = 0 ; i < vmList.size(); i++){
			model4.addRow(new Object[]{
					vmList.get(i).getId(),
					vm_name.get(i), vmList.get(i).getRam() , vmList.get(i).getFloatingIP() , 
					vmList.get(i).getNumberOfPes() , vmList.get(i).getSize()
			});
		}
		JScrollPane scrollPane4 = new JScrollPane(table4);
		scrollPane4.setBounds(52, 45, 600, 100);
		panel_4.add(scrollPane4);
		
		JLabel lblInstanceName = new JLabel("Instance Name :");
		lblInstanceName.setFont(new Font("FreeSans", Font.ITALIC, 18));
		lblInstanceName.setBounds(52, 167, 159, 21);
		panel_4.add(lblInstanceName);
		JTextField textField_15 = new JTextField();
		textField_15.setBounds(303, 166, 129, 22);
		panel_4.add(textField_15);
		textField_15.setColumns(10);
		
		JLabel lblFlavor = new JLabel("Flavor :");
		lblFlavor.setFont(new Font("FreeSans", Font.ITALIC, 18));
		lblFlavor.setBounds(52, 200, 150, 23);
		panel_4.add(lblFlavor);
		
		JComboBox<String> comboBox = new JComboBox<String>(flavorBoxList);
		comboBox.setBounds(303, 199, 129, 24);
		panel_4.add(comboBox);
		
		JLabel lblInstanceImage = new JLabel("Image :");
		lblInstanceImage.setFont(new Font("FreeSans", Font.ITALIC, 18));
		lblInstanceImage.setBounds(52, 229, 70, 26);
		panel_4.add(lblInstanceImage);
		
		JComboBox<String> comboBox_1 = new JComboBox<String>(boxModelImage);
		comboBox_1.setBounds(303, 231, 129, 24);
		panel_4.add(comboBox_1);
		
		JLabel lblInstanceVolume = new JLabel("Volume :");
		lblInstanceVolume.setFont(new Font("FreeSans", Font.ITALIC, 18));
		lblInstanceVolume.setBounds(52, 265, 103, 26);
		panel_4.add(lblInstanceVolume);
		
		JComboBox<String> comboBox_2 = new JComboBox<String>(boxModelVolume);
		comboBox_2.setBounds(303, 267, 129, 24);
		panel_4.add(comboBox_2);

		JComboBox<String> comboBox_S = new JComboBox<String>(boxModelContainer);
		comboBox_S.setBounds(303, 267, 129, 24);
		panel_4.add(comboBox_S);

		JButton btnCreateInstance = new JButton("Create Instance ");
		btnCreateInstance.setFont(new Font("FreeSans", Font.ITALIC, 18));
		btnCreateInstance.setBounds(52, 317, 242, 35);
		panel_4.add(btnCreateInstance);
		
		btnCreateInstance.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try{
					DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
					Calendar cal = Calendar.getInstance();
					textArea.append("\n"+dateFormat.format(cal.getTime())+"\n");
					textArea.append("\nAuthenticating with Keystone..\n");
					
					int auth = keyStone.authenticateUserForComponent(userName, "INSTANCE");
					if(auth == 1){
					textArea.append("\n" + userName + " has permission to create Instance\n");
					String instanceFlavorName = (String) comboBox.getSelectedItem();
					for(int i = 0 ; i < flaList.size() ; i++){
						if(instanceFlavorName == flaList.get(i).getName()){
							instanceMips = flaList.get(i).getMips();
							instancePes = flaList.get(i).getPes();
							instanceRam = (int) flaList.get(i).getRam();
							instanceBw = flaList.get(i).getBw();
							instanceSize = flaList.get(i).getSize();
						}
					}
					
					//image size
					try{
						int index = comboBox_1.getSelectedIndex();
						String imgName = imgList.get(index).getImgLoc();
						File tempImg = new File(imgName);
						double imgSize = (double) (tempImg.length()/(1024*1024));
						if((instanceSize - imgSize) < 0 ){
							JOptionPane.showMessageDialog(null, "Flavor Size Error", "Error", JOptionPane.ERROR_MESSAGE);
							textField_15.setText("");
						}
					}catch (Exception e) {
						JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					}
					if(textField_15.getText().length() > 0){
					Vm instance = new PowerVm(vmid,2,
							instanceMips, instancePes , instanceRam , instanceBw , instanceSize ,
							1 , "Xen", new CloudletSchedulerDynamicWorkload(instanceMips, instancePes),
							Constants.SCHEDULING_INTERVAL);
					//Vm Class -> set and get IP address
					instance.setFloatingIP();
					instance.setOS(comboBox_1.getSelectedItem().toString());
					
					
					textArea.append("\nnova-Api interacts with novaDb\n");					
					int instanceVolumeIndex = comboBox_2.getSelectedIndex();
					if(volList.get(instanceVolumeIndex).getVMID() == -1 ){
					textArea.append("\nnovaschedular finds appropriate Host via filerting and weighing\n");
					volList.get(instanceVolumeIndex).setVMID(vmid);
					API.attachVolume(instanceVolumeIndex, vmid);
					model3.setValueAt(vmid, instanceVolumeIndex, 4);
					modelS.setValueAt(vmid, instanceVolumeIndex, 4);
					textArea.append("New Instance created\n");
					textArea.append("\n"+comboBox_1.getSelectedItem().toString()+",base image stored to local Disk (Flavor Size)\n"); 
					
					textArea.append("\nCinder Volume attached is" +instanceVolumeIndex+"\n");

					
					
					model4.addRow(new Object[] {
							vmid,textField_15.getText() , instanceRam , instance.getFloatingIP() , instancePes , instanceSize
					});
					model5.addRow(new Object[] {
							vmid,textField_15.getText() , instanceRam , instancePes , instance.getFloatingIP()
					});
					
					
					boxModelInstance.addElement(Integer.toString(vmid));
					API.displayDataCenterDetails();
					textArea.append(baos.toString());
					vmid++;
					vmList.add(instance);
					
					}
					else{
						JOptionPane.showMessageDialog(null, "Volume already used by other instance..", "Error", JOptionPane.ERROR_MESSAGE);

					}
					textField_15.setText("");
					}else{
						JOptionPane.showMessageDialog(null, "Invalid Input", "Error", JOptionPane.ERROR_MESSAGE);

					}
					}else{
						JOptionPane.showMessageDialog(null, "Instance creation denied by Keystone", "Error", JOptionPane.ERROR_MESSAGE);
						textField_15.setText("");
					}
				}catch (Exception e) {
					JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		//End of create instance tab
		
		//Launch all created instances 
		/*JButton btnLaunchInstances = new JButton("Launch Instances");
		btnLaunchInstances.setFont(new Font("FreeSans", Font.ITALIC, 18));
		btnLaunchInstances.setBounds(320, 317, 242, 35);
		panel_4.add(btnLaunchInstances);
		
		btnLaunchInstances.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try{
					if(vmList.size() > 0){
						textArea_Log.setText("");
						tabbedPane_2.setEnabledAt(1, true);
						tabbedPane_2.setSelectedIndex(1);
						new launchInstance(vmList, volList);
						
					}else{
						JOptionPane.showMessageDialog(null, "No Instances created..", "Error", JOptionPane.ERROR_MESSAGE);
					}
				
				}catch (Exception exception) {
					JOptionPane.showMessageDialog(null, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		*/
		
		//Nova Launch instance tab
		
		JPanel panel_7 = new JPanel();
		tabbedPane_2.addTab(" Launch Instance ", null, panel_7, null);
		panel_7.setLayout(null);
		JLabel lblInstanceLaunch = new JLabel("INSTANCES ");
		lblInstanceLaunch.setFont(new Font("Abyssinica SIL", Font.BOLD | Font.ITALIC, 20));
		lblInstanceLaunch.setBounds(206, 12, 159, 22);
		panel_7.add(lblInstanceLaunch);
		JTable table5 = new JTable(){
			
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int col){
				return true;
			};
		};
		model5 = new DefaultTableModel();
		String Header5[] = new String[]{"ID","Name " ,"RAM " ,"VCPUs", "Floating IP "};
		model5.setColumnIdentifiers(Header5);
		table5.setModel(model5);
		for(int i = 0 ; i < vmList.size(); i++){
			model5.addRow(new Object[]{
					vmList.get(i).getId(),
					vm_name.get(i), vmList.get(i).getRam() , vmList.get(i).getNumberOfPes(),
					vmList.get(i).getFloatingIP()
			});
		}
		
		JScrollPane scrollPane5 = new JScrollPane(table5);
		scrollPane5.setBounds(52, 45, 600, 100);
		panel_7.add(scrollPane5);
		
		JLabel lblInstanceID = new JLabel("Instance ID :");
		lblInstanceID.setFont(new Font("FreeSans", Font.ITALIC, 18));
		lblInstanceID.setBounds(52, 167, 127, 25);
		panel_7.add(lblInstanceID);
		JComboBox<String> comboBox2 = new JComboBox<String>(boxModelInstance);
		comboBox2.setBounds(185, 165, 51, 27);
		panel_7.add(comboBox2);

		JButton btnSimulate = new JButton(" Open Terminal ");
		btnSimulate.setFont(new Font("FreeSans", Font.ITALIC, 18));
		btnSimulate.setBounds(265, 157, 212, 36);
		panel_7.add(btnSimulate);
		
		btnSimulate.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
					try{
						textArea_Log.setText("");
						tabbedPane_2.setEnabledAt(2, true);
						tabbedPane_2.setSelectedIndex(2);
						String instanceID = comboBox2.getSelectedItem().toString();
						String osName = vmList.get(Integer.parseInt(instanceID)).getOS();
						DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
						GUI.textArea_Log.append(dateFormat.format(Calendar.getInstance().getTime())+" : Started Login Service..\n");
						GUI.textArea_Log.append(dateFormat.format(Calendar.getInstance().getTime())+" : Started System Logging Service..\n");
						GUI.textArea_Log.append("\n" +osName + "\n");
						GUI.textArea_Log.append(vmList.get(Integer.parseInt(instanceID)).getFloatingIP() + " : Logging in as " + userName + "\n");
						//pass the datacenter map to terminal
						new Terminal(Integer.parseInt(instanceID),vmList.get(Integer.parseInt(instanceID)).getFloatingIP(),osName,userName,
								vmList,volList);
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, "No Instance To launch..", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
		});
		
		JButton btnNewButton_1 = new JButton(" Run Simulation ");
		btnNewButton_1.setFont(new Font("FreeSans", Font.ITALIC, 18));
		btnNewButton_1.setBounds(52, 225, 196, 36);
		panel_7.add(btnNewButton_1);
		
		btnNewButton_1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try{
					if(vmList.size() > 0){
						textArea_Log.setText("");
						tabbedPane_2.setSelectedIndex(2);
						new launchInstance(vmList, volList);
						
					}else{
						JOptionPane.showMessageDialog(null, "No Instances created..", "Error", JOptionPane.ERROR_MESSAGE);
					}
				
				}catch (Exception exception) {
					JOptionPane.showMessageDialog(null, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		
		
		tabbedPane_2.addTab(" Instance Console Log ", null, panel_11, null);
		//tabbedPane_2.setEnabledAt(1, false);
		panel_11.setLayout(null);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(6, 6, 688, 354);
		panel_11.add(scrollPane_2);
		
		textArea_Log = new JTextArea();
		textArea_Log.setBackground(Color.BLACK);
		textArea_Log.setForeground(Color.WHITE);
		textArea_Log.setEditable(false);
		textArea_Log.setFont(new Font("FreeSans", Font.ITALIC, 15));
		scrollPane_2.setViewportView(textArea_Log);
		
		JButton button = new JButton(" Log File ");
		button.setFont(new Font("FreeSans", Font.ITALIC , 20));
		button.setBounds(80 , 375 , 250 , 40);
		panel_11.add(button);
		
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try{
					File readFile = new File("src/org/opensim/simulationOutput/Logs.txt");
					Desktop.getDesktop().open(readFile);
					
				}catch (Exception e) {
					JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
				
			}
		});
	
		JButton graphButton = new JButton(" Utilization Graph ");
		graphButton.setFont(new Font("FreeSans", Font.ITALIC , 20));
		graphButton.setBounds(370 , 375 , 250 , 40);
		panel_11.add(graphButton);
		
		graphButton.addActionListener(new ActionListener() {
			@SuppressWarnings("static-access")
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try{
					List<OpensimStorageServer> serverList = OpensimStorageControlNode.getServerList();
					int count = 0;
					for(int i = 0 ; i < serverList.size() ; i++){
						OpensimStorageServer server = serverList.get(i);
						if(server.isUsed())
							count++;
					}
					
					sample plot = new sample("Utilization/Energy Graph", "Utilization/Energy Graph", count);
					plot.main("Utilization/Energy Graph", "Utilization/Energy Graph", count);
					/*
					plot p = new plot("Utilization Graph", "Utilization Graph", count);
					p.main(count);*/
				}catch (Exception e) {
					JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		//end of launch
	}
}
		
		
		
		
		
		