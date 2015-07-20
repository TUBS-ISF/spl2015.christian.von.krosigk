import engine.MP3Gui;


public aspect ProgressBar {
	pointcut pB(): execution(void engine.MP3Gui.progressBar());
	after():pB(){
		MP3Gui.mainPanel2.add(MP3Gui.progBar);
	}
}