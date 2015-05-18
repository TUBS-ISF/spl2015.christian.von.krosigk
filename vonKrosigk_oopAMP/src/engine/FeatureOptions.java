package engine;


public class FeatureOptions {

	private boolean showCover;
	private boolean inTitle;
	private boolean inProgBar;
	private boolean volUp;
	private boolean volDown;
	private boolean muteIt;
	
	private int length;
	

	public FeatureOptions(boolean[] vars) {
		length = vars.length;
		if (length != 6) {
			showCover 	= false;
			inTitle		= false;
			inProgBar 	= false;
			volUp 		= false;
			volDown		= false;
			muteIt		= false;
			throw new RuntimeException("all features off, wrong input array");
		}
		
		showCover 	= vars[0];
		inTitle		= vars[1];
		inProgBar 	= vars[2];
		volUp 		= vars[3];
		volDown		= vars[4];
		muteIt		= vars[5];
		if (!inTitle && !inProgBar) {
			throw new RuntimeException("Choose one ID3 feature.");
		}
	}


	public boolean isShowCover() {
		return showCover;
	}


	public void setShowCover(boolean showCover) {
		this.showCover = showCover;
	}


	public boolean isInTitle() {
		return inTitle;
	}


	public void setInTitle(boolean inTitle) {
		this.inTitle = inTitle;
	}


	public boolean isInProgBar() {
		return inProgBar;
	}


	public void setInProgBar(boolean inProgBar) {
		this.inProgBar = inProgBar;
	}


	public boolean isVolUp() {
		return volUp;
	}


	public void setVolUp(boolean volUp) {
		this.volUp = volUp;
	}


	public boolean isVolDown() {
		return volDown;
	}


	public void setVolDown(boolean volDown) {
		this.volDown = volDown;
	}


	public boolean isMuteIt() {
		return muteIt;
	}


	public void setMuteIt(boolean muteIt) {
		this.muteIt = muteIt;
	}
	
	public int length() {
		return length;
	}
	
	
}
