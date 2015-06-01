package engine;

public class Main {

	public static void main(String[] args) {
		// showCover, inTitle, inProgBar, volUp, volDown, muteIt
		boolean[] vars = {false,false,false,false,false,false};
		
		if (args.length > 1) {
			for(int i = 0; i < args.length; i++) {
				if (args[i].equals("showCover")) {
						vars[0] = true;
				}
				else if (args[i].equals("inTitle")) {
						vars[1] = true;
				}
				else if (args[i].equals("inProgBar")) {
						vars[2] = true;
				}
				else if (args[i].equals("volUp")) {
						vars[3] = true;
				}
				else if (args[i].equals("volDown")) {
						vars[4] = true;
				}
				else if (args[i].equals("muteIt")) {
						vars[5] = true;
				}
				
			}
		}
		else {
			vars[0] = true; // showCover
			vars[1] = true; // inTitle
			vars[2] = true; // inProgBar
			vars[3] = true; // volUp
			vars[4] = true; // volDown
			vars[5] = true; // muteIt
			
		}
		
		//boolean[] vars = {true,false,true,false,false,false};
		
		MP3Gui myGui = new MP3Gui();
		
		return;
		
	}

}
