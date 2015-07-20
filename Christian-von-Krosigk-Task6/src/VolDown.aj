import engine.*;


public aspect VolDown {
	pointcut vDown(): execution(void engine.MP3Gui.volDown());
	after():vDown(){
		MP3Gui.buttonPanel2.add(MP3Gui.volDownButton);
	}
}