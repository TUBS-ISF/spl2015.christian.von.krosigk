package plugins;

public class VolumeControl extends Plugin implements VolumeInterface {

	public String getPluginName() {
		return "VolumeControl";
	}
	
	public int getStartValue() {
		return -37;
	}

	public int getSliderMin() {
		// TODO Auto-generated method stub
		return -80;
	}

	public int getSliderMax() {
		return 6;
	}
}
