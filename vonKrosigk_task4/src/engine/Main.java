package engine;

import plugins.*;

public class Main {

	public static void main(String[] args) {
		
		MP3Gui myGui = new MP3Gui();
		
		String pluginPackage = "plugins";
		//*
		//simple player
		String[] plugins = {
				//"ShowCover",
				//"ProgressBar",
				//"ShowTime",
				//"Mute",
				//"VolumeControl",
				//"ShowCurrentSong"
			};
			
		/*
		// player with volume control
		String[] plugins = {
				//"ShowCover",
				//"ProgressBar",
				//"ShowTime",
				"Mute",
				"VolumeControl",
				//"ShowCurrentSong"
			};
		/*
		// volume control and progressbar
		String[] plugins = {
				//"ShowCover",
				"ProgressBar",
				//"ShowTime",
				"Mute",
				"VolumeControl",
				//"ShowCurrentSong"
			};
		/*	
		// all but cover
		String[] plugins = {
				//"ShowCover",
				"ProgressBar",
				"ShowTime",
				"Mute",
				"VolumeControl",
				"ShowCurrentSong"
			};
		/*	
		// only song infos and time
		String[] plugins = {
				//"ShowCover",
				"ProgressBar",
				"ShowTime",
				//"Mute",
				//"VolumeControl",
				"ShowCurrentSong"
			};
		//*/
		
		
		if ((plugins != null) && (plugins.length > 0)) {
			for (int i = 0; i < plugins.length; i++) { 
				String pluginName = pluginPackage + "." + plugins[i];
				try {
					Class<?> pluginClass = Class.forName(pluginName);
					myGui.addPlugin((Plugin) pluginClass.newInstance());
				} catch (Exception e) {
					System.out.println("Cannot load plugin " + pluginName + ", reason: " + e);
				}
			}
		}
		
		myGui.execute();
		myGui.setVisible();
		
		return;
		
	}

}
