package fr.dwarf.jcrypt;

import org.apache.pivot.wtk.DesktopApplicationContext;

import fr.dwarf.jcrypt.controllers.MainWindow;

public class Main {
    public static void main(String[] args) {

	/*
	 * TerraTheme themeLocal = (TerraTheme) Theme.getTheme();
	 * themeLocal.setBaseColor(4, new Color(0, 0, 0));
	 */
	DesktopApplicationContext.main(MainWindow.class, args);
    }
}
