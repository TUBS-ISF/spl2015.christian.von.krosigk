import engine.MP3Gui;


public aspect VolumeControl {
	pointcut vCtrl(): execution(void engine.MP3Gui.volumeControl());
	after():vCtrl(){
		MP3Gui.mainPanel3.add(MP3Gui.volBar);
	}
}