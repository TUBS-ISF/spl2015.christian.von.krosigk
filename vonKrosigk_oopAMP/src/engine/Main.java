package engine;
import engine.FeatureOptions;

public class Main {

	public static void main(String[] args) {
		// showCover, inTitle, inProgBar, volUp, volDown, muteIt
		boolean[] vars = {false,false,false,false,false,false};
		
		if (args.length > 1) {
			for(int i = 0; i < args.length; i++) {
				switch (args[i]) {
					case "showCover":
						vars[0] = true;
						break;
					case "inTitle":
						vars[1] = true;
						break;
					case "inProgBar":
						vars[2] = true;
						break;
					case "volUp":
						vars[3] = true;
						break;
					case "volDown":
						vars[4] = true;
						break;
					case "muteIt":
						vars[5] = true;
						break;
					default: //
						
					break;
				}
				
			}
		}
		else {
			vars[0] = false; // showCover
			vars[1] = true; // inTitle
			vars[2] = true; // inProgBar
			vars[3] = false; // volUp
			vars[4] = false; // volDown
			vars[5] = true; // muteIt
			
		}
		
		//boolean[] vars = {true,false,true,false,false,false};
		
		// showCover, inTitle, inProgBar, volUp, volDown, muteIt
		boolean[] config_1 = {true,false,true,false,false,false}; // no volume control
		boolean[] config_2 = {false,true,true,true,true,true}; // no cover
		boolean[] config_3 = {true,true,true,true,true,true}; // all features
		
		vars = config_3;
		FeatureOptions features = new FeatureOptions(vars);
		@SuppressWarnings("unused")
		MP3Gui myGui = new MP3Gui(features);
		
		return;
		
	}

}
