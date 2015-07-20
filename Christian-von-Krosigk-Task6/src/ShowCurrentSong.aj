import engine.MP3Gui;


public aspect ShowCurrentSong {
	pointcut sCS(MP3Gui mg): execution(void engine.MP3Gui.showCurrentSong(MP3Gui)) && args(mg);
	after(MP3Gui mg):sCS(mg){
		MP3Gui.changeTitle(mg);
	}
	
}