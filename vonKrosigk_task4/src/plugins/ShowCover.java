package plugins;

public class ShowCover extends Plugin implements CoverInterface {

	public String getPathDefaultPciture() {
		return "./pictures/no_cover.jpg";
	}
	
	public int getWidth() {
		return 150;
		
	}
	
	public int getHeight() {
		return 150;
		
	}

	public String getPluginName() {
		
		return "ShowCover";
	}
	
}
