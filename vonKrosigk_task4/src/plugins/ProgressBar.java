package plugins;

public class ProgressBar extends Plugin implements CoverInterface {

	public String getPluginName() {
		
		return "ProgressBar";
	}
	
	public String getPathDefaultPciture() {
		return null;
	}
	
	public int getWidth() {
		return 20;
	}
	
	public int getHeight() {
		return 100;
	}

}
