import engine.MP3Gui;


public aspect Mute {
	pointcut mute(): execution(void engine.MP3Gui.mute());
	after():mute(){
		MP3Gui.buttonPanel2.add(MP3Gui.muteButton);
	}
	
}