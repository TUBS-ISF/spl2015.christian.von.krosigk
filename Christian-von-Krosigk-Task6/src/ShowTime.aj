import engine.MP3Gui;


public aspect ShowTime {
	pointcut sT(MP3Gui mg): execution(void engine.MP3Gui.showCurrentSong(MP3Gui)) && args(mg);
	after(MP3Gui mg):sT(mg){
		MP3Gui.changeTime(mg);
	}
	
	pointcut sTR(MP3Gui mg): execution(void engine.MP3Gui.showCurrentSong(MP3Gui)) && args(mg);
	after(MP3Gui mg):sTR(mg){
		MP3Gui.changeTimeRun(mg);
	}
}