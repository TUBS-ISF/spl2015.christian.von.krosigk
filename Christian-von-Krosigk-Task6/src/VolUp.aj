import engine.*;

public aspect VolUp {
	pointcut vUp(): execution(void engine.MP3Gui.volUp());
	after():vUp(){
		MP3Gui.buttonPanel2.add(MP3Gui.volUpButton);
	}

}