import java.io.File; 
import java.io.IOException; 

import javax.sound.sampled.AudioFormat; 
import javax.sound.sampled.AudioInputStream; 
import javax.sound.sampled.AudioSystem; 
import javax.sound.sampled.Clip; 
import javax.sound.sampled.FloatControl; 
import javax.sound.sampled.LineUnavailableException; 
import javax.sound.sampled.UnsupportedAudioFileException; 

import javazoom.jl.decoder.JavaLayerException; 

/**
 * A simple MP3Player, which is able to play an .mp3-file and pause, resume, and stop the playblack.
 */
public  class  PlayEngine {
	
	public final static int STATUS_READY 	= 0;

	
	public final static int STATUS_PLAYING 	= 1;

	
	public final static int STATUS_PAUSED 	= 2;

	
	public final static int STATUS_STOPPED	= 3;

	
	public final static int STATUS_FINISHED = 4;

	

	private int playerStatus = STATUS_READY;

	
	private AudioInputStream audioStream;

	
	private Clip clip;

	
	private FloatControl gainControl;

	 

	private float muteVolume, actualVolume;

	
	private boolean muted, volMuted;

	
	/**
	 * Creates a new MP3Player from given InputStream.
	 * @param filename
	 * @throws IOException 
	 * @throws UnsupportedAudioFileException 
	 * @throws LineUnavailableException
	 */
	public PlayEngine(String filename) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		File file = new File(filename);

		audioStream = AudioSystem.getAudioInputStream(file);
		AudioFormat baseFormat = audioStream.getFormat();
		AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(),
				baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
		AudioInputStream stream = AudioSystem.getAudioInputStream(decodedFormat, audioStream);
		clip = AudioSystem.getClip();
		clip.open(stream);
		gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		actualVolume = gainControl.getValue();
		
		muted = false;
	}

	

	/**
	 * Creates a new MP3Player without an InputStream.
	 * 
	 */
	public PlayEngine() {
		muted = false;
		
	}

	

	/**
	 * Starts playback (resumes if paused).
	 * @throws JavaLayerException when there is a problem decoding the file.
	 */
	public void openFile(String filename) throws UnsupportedAudioFileException, IOException, LineUnavailableException{
		File file = new File(filename);
		if (clip != null) {
			clip.close();
		}
		audioStream = AudioSystem.getAudioInputStream(file);
		AudioFormat baseFormat = audioStream.getFormat();
		AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(),
				baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
		AudioInputStream stream = AudioSystem.getAudioInputStream(decodedFormat, audioStream);
		clip = AudioSystem.getClip();
		clip.open(stream);
		gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		actualVolume = gainControl.getValue();
		muted = false;
		play();
		
	}

	  
	
	/**
	 * Starts playback (resumes if paused).
	 * @throws JavaLayerException when there is a problem decoding the file.
	 */
	public void play() {
		synchronized (clip) {
			if(clip != null && clip.isOpen()) {
				clip.start();
				playerStatus = STATUS_PLAYING;
			}
		}
	}

	

	/**
	 * Pauses playback.
	 */
	public void pause() {
		synchronized (clip) {
			if (playerStatus == STATUS_PLAYING) {
				clip.stop();
				playerStatus = STATUS_PAUSED;
			}
		}
	}

	

	/**
	 * Resumes playback.
	 */
	public void resume() {
		synchronized (clip) {
			if (playerStatus == STATUS_PAUSED) {
				clip.start();
				playerStatus = STATUS_PLAYING;
			}
		}
	}

	

	/**
	 * Stops playback. If not playing, does nothing.
	 */
	public void stop() {
		synchronized (clip) {
			clip.stop();
			clip.setFramePosition(0);
			playerStatus = STATUS_STOPPED;
			
		}
	}

	

	/**
	 * Returns the current player status.
	 * @return the player status
	 */
	public int getPlayerStatus() {
		return this.playerStatus;
	}

	

	/**
	 * Closes the player, regardless of current state.
	 */
	public void close() {
		synchronized (clip) {
			if(clip != null && clip.isOpen()) {
				clip.stop();
				clip.close();
			}
		}
		try {
			audioStream.close();
			audioStream = null;
		} catch (IOException e) {
			// we are terminating, thus ignore exception
		}
	}

	
	public long getFramePosition() {
		return clip.getFramePosition();
	}

	
	public long getTimeInMillisekonds() {
		return clip.getMicrosecondPosition()/1000;
	}

	
	public void skip(int sek) {
		clip.setMicrosecondPosition(clip.getMicrosecondPosition() + sek*1000000);
		
	}

	
	
	public boolean songEnded() {
		if (clip != null) {
			if (clip.getMicrosecondLength() == clip.getMicrosecondPosition()) {
				playerStatus = STATUS_FINISHED;
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}

	
	
	public boolean fileLoaded() {
		
		return clip != null;
	}

	
	
	public void changeVolume(float volDiff)  {
		actualVolume = gainControl.getValue() + volDiff;
		if(muted) return;
		if (volDiff > 0 && actualVolume > gainControl.getMaximum()) {
			
			actualVolume = gainControl.getMaximum();
			
		}
		else if (volDiff < 0 && actualVolume < gainControl.getMinimum()) {
				actualVolume = gainControl.getMinimum();
		}
		gainControl.setValue(actualVolume);
		
	}

	
	
	public void muteVolume()  {
		
		
		if(volMuted) return;
		if(!muted) {
			muteVolume = gainControl.getValue();
			muted = true;
			gainControl.setValue(gainControl.getMinimum());
		}
		else {
			muted = false;
			gainControl.setValue(muteVolume);
		}
	}

	
	
	public float getActualVolume() {
		return actualVolume;
	}

	

	public void setVolume(float vol) {
		actualVolume = vol;
		gainControl.setValue(actualVolume);
		
	}

	
	
	public boolean isMuted() {
		return muted;
	}


}
