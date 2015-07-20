import engine.MP3Gui;


public aspect ShowCover {
	pointcut sC(): execution(void engine.MP3Gui.showCover());
	after():sC(){
		MP3Gui.mainPanel.add(MP3Gui.coverLabel);
	}
	
}