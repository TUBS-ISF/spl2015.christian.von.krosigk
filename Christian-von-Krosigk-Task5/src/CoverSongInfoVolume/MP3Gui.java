
import java.text.SimpleDateFormat; 
import java.util.HashMap; 
import java.util.List; 
import java.util.ArrayList; 
import java.util.Collections; 
import java.util.Date; 
import java.util.Map; 

import javax.imageio.ImageIO; 
import javax.swing.*; 
import javax.swing.event.ChangeEvent; 
import javax.swing.event.ChangeListener; 
import javax.swing.event.ListSelectionListener; 
import javax.swing.filechooser.FileFilter; 
import javax.swing.filechooser.FileNameExtensionFilter; 
import javax.sound.sampled.FloatControl; 
import javax.sound.sampled.LineUnavailableException; 
import javax.sound.sampled.UnsupportedAudioFileException; 

import javazoom.spi.mpeg.sampled.file.tag.MP3Tag; 

import com.mpatric.mp3agic.*; 

import java.awt.*; 
import java.awt.event.*; 
import java.io.ByteArrayInputStream; 
import java.io.File; 
import java.io.IOException; 


public   class  MP3Gui  extends JFrame  implements ActionListener {
	 //, ListSelectionListener {


	/**
	 * 
	 */
	private static final long serialVersionUID  = 1L;

	
	
	
	//MP3 Player variables
	private PlayEngine guiMP3Player  = null;

	
	private String filename  = System.getProperty("user.home");

	
	private String filepath  = "";

	
	
	// GUI Variabels  
	private static String title  = "";

	

	private JButton playButton,stopButton,pauseButton,openButton,
					volUpButton, volDownButton, muteButton,
					shuffleButton, repeatOneButton, repeatAllButton;

	
	
	private JPanel 	mainPanel, leftPanel, rightPanel, volumePanel,
					controlButtonPanel, volumeButtonPanel, coverPanel, shuffleButtonPanel;

	
	
	private JLabel coverLabel  ;

	
	
	private JList playList  ;

	
	private DefaultListModel<String> playListModel  ;

	
	
	private JProgressBar progBar  ;

	
	
	private JSlider volBar  ;

	
	
	private MenuBar myMenu  ;

	
	private Menu file  ;

	
	private MenuItem openFileMenuItem, openFolderMenuItem;

	
	
	// Open Files
	private JFileChooser fc  = null;

	
	private FileFilter ff  ;

	
	
	// MP3 Tag variables
	private static final String DEFAULT_PICTURE_PATH  = "./pictures/no_cover.jpg";

	 //TODO
	
	private Mp3File mp3tag  = null;

	
	private String mp3Album, mp3artist, mp3title, mp3length;

	
	private int mp3Track  ;

	
	private Timer t1  ;

	
	private String actTime  ;

	
	private SimpleDateFormat sdf  = new SimpleDateFormat("mm:ss");

	
	private ImageIcon coverIcon, default_coverIcon;

	
	
	// Playlist variables
	private Map<String, String> playListMap;

	
	private int playlistIndex  ;

	
	private boolean repeatAll, repeat1;

	
	// GUI-Constructor
	
	public MP3Gui () {
		super(title);
	}

	

	
	public void execute() {
					
		this.setSize(300,600);
		this.setLocation(300,300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.setResizable(true);
		
		
		// MP3Player
		guiMP3Player = new PlayEngine();
		playListMap = new HashMap<String,String>();
		// MP3Tag
		t1 = new Timer(100, this);
		playlistIndex = 0;
		repeatAll = false;
		repeat1 = false;
		
		// Menubar
		myMenu = new MenuBar();
		file = new Menu("File");
		
		openFileMenuItem = new MenuItem("Open File ...");
		openFolderMenuItem = new MenuItem("Open Folder ...");
		
		openFileMenuItem.addActionListener(this);
		openFolderMenuItem.addActionListener(this);
		
		file.add(openFileMenuItem);
		// file.add(openFolderMenuItem);
		
		
		myMenu.add(file);
		
		//Progressbar
		progBar = new JProgressBar();
		
		progBar.setSize(20,	100);
				
		// Volumebar
		volBar = new JSlider(-80, 6);
		
		// PlayList
		playListModel = new DefaultListModel();
		playList = new JList(playListModel);
		
		playList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e ) {
				if ( e.getClickCount() == 2 ) {
					if (guiMP3Player.fileLoaded()) {
						guiMP3Player.stop();
					}
					playlistIndex = playList.getSelectedIndex();
					playFile(playListMap.get(playListModel.get(playlistIndex)));
					t1.start();
	    		}
			}
		});
		playList.addKeyListener(new KeyListener() {
			
			public void keyTyped(KeyEvent e) {
				if (playList.getSelectedIndex() != -1) {
					switch (e.getKeyChar()) {
					case 'd':
						playListModel.remove(playList.getSelectedIndex());
						break;
					default:
						System.out.println("KeyCode: " + e.getKeyCode() + " and KeyChar: " + e.getKeyChar());
						break;
					}
					
						
				}
			}
			
			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyPressed(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		// Panels

		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.LINE_AXIS));
		
		leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));
		
		rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.PAGE_AXIS));
		
		volumePanel = new JPanel();
		volumePanel.setLayout(new BoxLayout(volumePanel, BoxLayout.PAGE_AXIS));
		
		controlButtonPanel = new JPanel();
		controlButtonPanel.setLayout(new BoxLayout(controlButtonPanel, BoxLayout.LINE_AXIS));
		
		volumeButtonPanel = new JPanel();
		volumeButtonPanel.setLayout(new BoxLayout(volumeButtonPanel, BoxLayout.LINE_AXIS));
		
		coverPanel = new JPanel();
		coverPanel.setLayout(new BoxLayout(coverPanel, BoxLayout.LINE_AXIS));
		
		shuffleButtonPanel = new JPanel();
		shuffleButtonPanel.setLayout(new BoxLayout(shuffleButtonPanel, BoxLayout.LINE_AXIS));
		
		// Cover Label
		default_coverIcon = new ImageIcon(DEFAULT_PICTURE_PATH);
		Image img = default_coverIcon.getImage();
		img = img.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
		default_coverIcon = new ImageIcon(img);
		coverLabel = new JLabel(default_coverIcon);
		
		// Buttons
		openButton = new JButton("OPEN FILE");
		playButton = new JButton("PLAY");
		stopButton = new JButton("STOP");
		pauseButton = new JButton("PAUSE");	
		
		volUpButton = new JButton("VolUp");
		volDownButton = new JButton("VolDown");
		muteButton = new JButton("Mute");
		
		
		shuffleButton = new JButton("Shuffle");
		repeatOneButton = new JButton("Repeat One");
		repeatAllButton = new JButton("Repeat All");
		
		// GUI build structure
		// Control Button Panel
		controlButtonPanel.add(stopButton);
		controlButtonPanel.add(playButton);
		controlButtonPanel.add(pauseButton);
		
		// Volume Buttons/Bars
		 
		volumeButtonPanel.add(volDownButton);
		volumeButtonPanel.add(volUpButton);
		
		
		volumePanel.add(volBar);
		leftPanel.add(Box.createVerticalStrut(5));
		volumePanel.add(volumeButtonPanel);
		
		// ShuffleRepeat Buttons
		shuffleButtonPanel.add(shuffleButton);
		shuffleButtonPanel.add(repeatOneButton);
		shuffleButtonPanel.add(repeatAllButton);
		
		// Main Panel
		
		leftPanel.add(progBar);
				
		leftPanel.add(Box.createVerticalStrut(5));
		leftPanel.add(controlButtonPanel);
		leftPanel.add(Box.createVerticalStrut(5));
		
		
		rightPanel.add(new JScrollPane(playList));
		rightPanel.add(Box.createVerticalStrut(5));
		//#ifdef ShuffleRepeat
		rightPanel.add(shuffleButtonPanel);
		rightPanel.add(Box.createVerticalStrut(5));
		//#endif
		
		mainPanel.add(leftPanel);
		mainPanel.add(Box.createHorizontalStrut(5));
		//#ifdef Playlist
		//mainPanel.add(rightPanel);
		//mainPanel.add(Box.createHorizontalStrut(5));
		//#endif
		
		// ButtonListeners
		playButton.addActionListener(this);
		stopButton.addActionListener(this);
		pauseButton.addActionListener(this);
		openButton.addActionListener(this);
		
		volUpButton.addActionListener(this);
		volDownButton.addActionListener(this);
		muteButton.addActionListener(this);
		
		shuffleButton.addActionListener(this);
		repeatOneButton.addActionListener(this);
		repeatAllButton.addActionListener(this);
		
		// Finish GUI
		setMenuBar(myMenu);
		getContentPane().add(mainPanel);
		pack();
		t1.start(); // start Timer
		
	}

	
	
	
	// Button Functions
	public void actionPerformed  (ActionEvent e) {
		if (e.getSource() == playButton){
			if(playListModel.isEmpty()) {
				openButton.doClick();
			}
			else {
				if(guiMP3Player.getPlayerStatus() == guiMP3Player.STATUS_STOPPED) {
					if (guiMP3Player.fileLoaded()) {
						guiMP3Player.play();
					}
				}
			}
			t1.start();
		}
		
		else if (e.getSource() == stopButton) {
			if(guiMP3Player.fileLoaded()){
				guiMP3Player.stop();
			}
			if(t1.isRunning()) t1.stop();
			progBar.setStringPainted(false);
		}
	
		else if (e.getSource() == pauseButton) {
			if (guiMP3Player.getPlayerStatus() == PlayEngine.STATUS_PLAYING){
				guiMP3Player.pause();
			}
			else if (guiMP3Player.getPlayerStatus() == PlayEngine.STATUS_PAUSED){
				guiMP3Player.resume();
			}
	
		}
		else if  (e.getSource() == openFolderMenuItem){
			fc = new JFileChooser();
			fc.setCurrentDirectory(new File(filename));
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					
			int returnVal = fc.showOpenDialog(this);
			
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				filename = fc.getSelectedFile().getParent();
				File[] fileArray = fc.getSelectedFile().listFiles();
				boolean empty = playListModel.isEmpty();
				addToPlaylist(fileArray);
				if (empty) {
					playList.setSelectedIndex(0);
					playFile(playListMap.get(playList.getSelectedValue()));
					playlistIndex = 0;
				}
			}
			
		}
		else if (e.getSource() == openFileMenuItem || e.getSource() == openButton){
			boolean playfile = false;
			fc = new JFileChooser();
			ff = new FileNameExtensionFilter("mp3","mp3");
			
			fc.setCurrentDirectory(new File(filename));
			
			fc.addChoosableFileFilter(ff);
			fc.setFileFilter(ff);
			 
			int returnVal = fc.showOpenDialog(this);
			
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				filename = fc.getSelectedFile().getParent(); //Save Path
				filepath = fc.getSelectedFile().getPath();

				if (playListMap.isEmpty()) {
					playfile = true;
				}
				File[] fA = {fc.getSelectedFile()};
				addToPlaylist(fA);
				
				if (playfile) {
					playFile(filepath);
					playList.setSelectedIndex(0);
					playlistIndex = 0;
				}
			}
		}
		
		else if (e.getSource() == t1) {
			if (guiMP3Player.songEnded()) {
				if(repeat1) {
					guiMP3Player.stop();
					guiMP3Player.play();
				}
				else if (repeatAll) {
					playlistIndex = (playlistIndex + 1)%playListModel.getSize();
					playFile(playListMap.get(playListModel.get(playlistIndex)));
					playList.setSelectedIndex(playlistIndex);
				}
				else {
					playlistIndex++;
					if(playListModel.getSize() <= playlistIndex) {
						stopButton.doClick();
					}
					else {
						playFile(playListMap.get(playListModel.get(playlistIndex)));
						playList.setSelectedIndex(playlistIndex);
					}
					
				}	
			}
			else if (guiMP3Player.getPlayerStatus() == guiMP3Player.STATUS_PLAYING ||
					 guiMP3Player.getPlayerStatus() == guiMP3Player.STATUS_PAUSED) {
			
				long actualTime = guiMP3Player.getTimeInMillisekonds();
				actTime = sdf.format(new Date(actualTime));
						
				progBar.setValue((int)actualTime);
				
				progBar.setString(actTime + " / " + mp3length);
				
			}
			
			else {
				return;
			}
		}
		else if (e.getSource() == volDownButton) {
			volBar.setValue(volBar.getValue() - 2);
			if(guiMP3Player.fileLoaded()){
				guiMP3Player.changeVolume(-2.0f);
			}
		}
		else if (e.getSource() == volUpButton) {
			volBar.setValue(volBar.getValue() + 2);
			if(guiMP3Player.fileLoaded()){
				guiMP3Player.changeVolume(2.0f);
			}
		}
		else if (e.getSource() == muteButton) {
			if(guiMP3Player.fileLoaded()){
				guiMP3Player.muteVolume();
				if (guiMP3Player.isMuted()) {
					muteButton.setText("Unmute");
				}
				else {
					muteButton.setText("Mute");
				}
			}
		}
		else if (e.getSource() == shuffleButton) {
			List<String> keys = new ArrayList<String>(playListMap.keySet());
			Collections.shuffle(keys);
			playListModel.clear();
			for (String o : keys) {
				playListModel.addElement(o);
			}
			playList.setSelectedIndex(0);
		}
		else if (e.getSource() == repeatOneButton) {
			repeat1 = !repeat1;
			if (repeat1) {
				repeatOneButton.setText("Repeat One - An");
			}
			else {
				repeatOneButton.setText("Repeat One");
			}
		}
		else if (e.getSource() == repeatAllButton) {
			repeatAll = !repeatAll;
			if (repeatAll) {
				repeatAllButton.setText("Repeat All - An");
			}
			else {
				repeatAllButton.setText("Repeat All");
			}
		}
		
	}

	
	
	private void addToPlaylist  (File[] fileArray) {
		String 	tmp_mp3Album = "",
				tmp_mp3artist = "",
				tmp_mp3title = "",
				tmp_mp3length = "",
				tmp_mp3Track = "";
		for(int i = 0; i < fileArray.length ; i++) {
			
			if (fileArray[i].isDirectory()) { // Datei ist Ordner
				//
				continue;
			}
			else if (fileArray[i].isFile()) {
				String filePath = fileArray[i].getPath();
				String[] extension = filePath.split("\\.");	
				if(extension.length > 0) {
					if(extension[extension.length - 1].equals("wav")){ // Datei ist MP3-Datei
						
						try {
							
							Mp3File tmp_mp3tag = new Mp3File(filePath);
							
							if (tmp_mp3tag.hasId3v1Tag()) {
								tmp_mp3artist = tmp_mp3tag.getId3v1Tag().getArtist();
								tmp_mp3title = tmp_mp3tag.getId3v1Tag().getTitle();
								tmp_mp3Track = tmp_mp3tag.getId3v1Tag().getTrack();
								tmp_mp3Album = tmp_mp3tag.getId3v1Tag().getAlbum();
								tmp_mp3length = sdf.format(new Date(tmp_mp3tag.getLengthInMilliseconds()));
							}
							else if (tmp_mp3tag.hasId3v2Tag()){
								tmp_mp3artist = tmp_mp3tag.getId3v2Tag().getArtist();
								tmp_mp3title = tmp_mp3tag.getId3v2Tag().getTitle();
								tmp_mp3Track = tmp_mp3tag.getId3v2Tag().getTrack();
								tmp_mp3Album = tmp_mp3tag.getId3v2Tag().getAlbum();
								tmp_mp3length = sdf.format(new Date(tmp_mp3tag.getLengthInMilliseconds()));
								
							}
							else {
								tmp_mp3artist = "unkown";
								tmp_mp3title = "unknown";
								tmp_mp3Track = "0";
								tmp_mp3Album = "unknown";
								tmp_mp3length = sdf.format(new Date(tmp_mp3tag.getLengthInMilliseconds()));
							}
						} catch (IOException ex) {
							ex.printStackTrace();
						} catch (UnsupportedTagException e1) {
							e1.printStackTrace();
						} catch (InvalidDataException e1) {
							e1.printStackTrace();
						}
						String mapKey = tmp_mp3Track + " : " + tmp_mp3Album + " - " + tmp_mp3artist + " - " + tmp_mp3title + " - " + tmp_mp3length;
						playListMap.put(mapKey, filePath);
						playListModel.addElement(mapKey);
						
					}
				}
			}
		}
		
	}

	
	
	private void playFile  (String filePath) {
		try {
			
			mp3tag = new Mp3File(filePath);
			
			if (mp3tag.hasId3v1Tag()) {
				mp3artist = mp3tag.getId3v1Tag().getArtist();
				mp3title = mp3tag.getId3v1Tag().getTitle();
				mp3length = sdf.format(new Date(mp3tag.getLengthInMilliseconds()));
			}
			else if (mp3tag.hasId3v2Tag()){
				mp3artist = mp3tag.getId3v2Tag().getArtist();
				mp3title = mp3tag.getId3v2Tag().getTitle();
				byte[] imageData = mp3tag.getId3v2Tag().getAlbumImage();

	            if (imageData != null) {
	                Image img = ImageIO
	                        .read(new ByteArrayInputStream(imageData));
	                img = img.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
	                coverIcon = new ImageIcon(img);
	                coverLabel.setIcon(coverIcon);
	                
	            }
	            else {
	            	coverLabel.setIcon(default_coverIcon);
	            }
	            
				mp3length = sdf.format(new Date(mp3tag.getLengthInMilliseconds()));
				progBar.setMaximum((int) mp3tag.getLengthInMilliseconds());
			}
			else {
				mp3artist = "unkown";
				mp3title = "unknown";
				mp3length = sdf.format(new Date(mp3tag.getLengthInMilliseconds()));
			}
			
			guiMP3Player.openFile(filePath);
			guiMP3Player.setVolume((float) volBar.getValue());
			
			
			progBar.setStringPainted(true);
			progBar.setString("00:00 / 00:00");
			
			
		} catch (UnsupportedAudioFileException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (LineUnavailableException ex) {
			ex.printStackTrace();
		}
		catch (UnsupportedTagException e1) {
			e1.printStackTrace();
		} catch (InvalidDataException e1) {
			e1.printStackTrace();
		}
		
		
	}

	
	

	public void setVisible  () {
		this.setVisible(true);
	}


}
