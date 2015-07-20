package engine;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.mpatric.mp3agic.*;

import java.awt.*;
import java.awt.event.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;


public class MP3Gui extends JFrame implements ActionListener {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//MP3 Player variables
	private engine.MP3Player guiMP3Player = null;
	private String filename = "./music example file"; //System.getProperty("user.home");
	private String filepath = "";
	
	// GUI Variabels
	private static String title = "";
	private int controlGrids,
				volumeGrids;
	
	public static JButton playButton,stopButton,pauseButton,openButton,
					volUpButton, volDownButton, muteButton;
	
	public static JPanel mainPanel, mainPanel2, mainPanel3, buttonPanel, buttonPanel2;
	
	public static JLabel coverLabel;
	
	private JTable playList;
	
	public static JProgressBar progBar, volBar;
	
	private MenuBar myMenu;
	private Menu file;
	private MenuItem openFileMenuItem, openFolderMenuItem;
	
	// Open Files
	private JFileChooser fc = null;
	private FileFilter ff;
	
	// MP3 Tag variables
	private static final String DEFAULT_PICTURE_PATH = "./pictures/no_cover.jpg"; //TODO
	
	private Mp3File mp3tag = null;
	public static String mp3artist, mp3title, mp3length;
	public static long actualTime;
	private Timer t1;
	public static String actTime;
	private SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
	private ImageIcon coverIcon, default_coverIcon;
	
	// GUI-Constructor
	
	public MP3Gui () {
		
		super(title);
				
		setSize(600,800);
		setLocation(300,300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setResizable(true);
		
		
		
		// MP3Player
		guiMP3Player = new engine.MP3Player();
		
		// MP3Tag
		t1 = new Timer(250, this);
		 
		// Menubar
		myMenu = new MenuBar();
		file = new Menu("File");
		
		openFileMenuItem = new MenuItem("Open File ...");
		openFolderMenuItem = new MenuItem("Open Folder ...");
		
		openFileMenuItem.addActionListener(this);
		openFolderMenuItem.addActionListener(this);
		
		file.add(openFileMenuItem);
		// if Playlist active
		//file.add(openFolderMenuItem);
		
		myMenu.add(file);
		
		//Progressbar
		progBar = new JProgressBar();
		volBar = new JProgressBar(-80, 6);
		volBar.setValue(0);
		volBar.setStringPainted(true);
		
		// PlayList
		String[] columnNames = {"Artist - Title", "L�nge"};
		String[][] rowData = {{"",""}};
		playList = new JTable(rowData, columnNames);
		
		// Panels
		volumeGrids= 4;
		controlGrids = 3;
		
				
		mainPanel = new JPanel(new GridLayout(1,volumeGrids));
		mainPanel2 = new JPanel(new GridLayout(controlGrids,1));
		mainPanel3 = new JPanel(new GridLayout(2,1));
		buttonPanel = new JPanel(new GridLayout(1,3));
		buttonPanel2 = new JPanel(new GridLayout(1,3));
		
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
		
		// GUI build structure
		// Button Panel
		buttonPanel.add(stopButton);
		buttonPanel.add(playButton);
		buttonPanel.add(pauseButton);
		
		// Volume Buttons/Bars
		volDown();
		volUp();
		mute();

		volumeControl();
		mainPanel3.add(buttonPanel2);
		
		// Main Panel
		progressBar();
		
		mainPanel2.add(buttonPanel);
		mainPanel2.add(openButton);
		
		mainPanel.add(mainPanel2);
		mainPanel.add(mainPanel3);

		showCover();

		//mainPanel.add(new JScrollPane(playList));
		
		
		// ButtonListeners
		playButton.addActionListener(this);
		stopButton.addActionListener(this);
		pauseButton.addActionListener(this);
		openButton.addActionListener(this);
		
		volUpButton.addActionListener(this);
		volDownButton.addActionListener(this);
		muteButton.addActionListener(this);
		
		// Finish GUI
		setMenuBar(myMenu);
		getContentPane().add(mainPanel);
		pack();
				
	}
	
	
	// Button Functions
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == playButton){
			if(guiMP3Player.fileLoaded()){
				if(guiMP3Player.songEnded()){
					guiMP3Player.stop();
					guiMP3Player.play();
				}
				else {
					guiMP3Player.play()	;
				}
			}
		}
		
		else if (e.getSource() == stopButton) {
			if(guiMP3Player.fileLoaded()){
				guiMP3Player.stop();
			}
		}
	
		else if (e.getSource() == pauseButton) {
			if (guiMP3Player.getPlayerStatus() == MP3Player.STATUS_PLAYING){
				guiMP3Player.pause();
			}
			else if (guiMP3Player.getPlayerStatus() == MP3Player.STATUS_PAUSED){
				guiMP3Player.resume();
			}
	
		}
		else if (e.getSource() == openButton || e.getSource() == openFileMenuItem){
			fc = new JFileChooser();
			ff = new FileNameExtensionFilter("mp3","mp3");
			
			fc.setCurrentDirectory(new File(filename));
			
			fc.addChoosableFileFilter(ff);
			fc.setFileFilter(ff);
			 
			int returnVal = fc.showOpenDialog(this);
			
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				filename = fc.getSelectedFile().getParent(); //Save Path
				filepath = fc.getSelectedFile().getPath();
				if(guiMP3Player.getPlayerStatus() == MP3Player.STATUS_PLAYING) {
					guiMP3Player.stop();
				}
				
				try {
					
					mp3tag = new Mp3File(fc.getSelectedFile().getPath());
					
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
					
					guiMP3Player.openFile(filepath);
					guiMP3Player.setVolume((float) volBar.getValue());

					showCurrentSong(this);
					showTime(this);

					t1.start(); // startet Timer
					
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
		}
		
		else if (e.getSource() == t1) {
			actualTime = guiMP3Player.getTimeInMillisekonds();
			actTime = sdf.format(new Date(actualTime));
			
			showCurrentSong(this);
			showTimeRun(this);
				
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
			}
		}
		
	}
	
	
	public void setVisible() {
		this.setVisible(true);
	}
	
	
	public void showCover() {
	}

	public void showCurrentSong(MP3Gui mg) {
	}
	
	public void progressBar() {
	}
	
	public void showTime(MP3Gui mg) {
	}
	
	public void showTimeRun(MP3Gui mg) {
	}
	
	public void volumeControl() {
	}
	
	public void mute() {
	}
	
	public void volUp() {
	}
	
	public void volDown() {
	}


	public static void changeTitle(MP3Gui mg) {
		mg.setTitle(mp3artist + " - "+ mp3title);
	}


	public static void changeTime(MP3Gui mg) {
		progBar.setStringPainted(true);
		progBar.setString("00:00 / 00:00");	
	}


	public static void changeTimeRun(MP3Gui mg) {
		progBar.setValue((int)actualTime);
		progBar.setString(actTime + " / " + mp3length);
		
	}
	
}