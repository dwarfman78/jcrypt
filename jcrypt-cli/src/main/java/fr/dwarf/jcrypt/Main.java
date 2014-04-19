package fr.dwarf.jcrypt;

import java.io.IOException;

import org.apache.commons.cli.ParseException;

public class Main {
    public static void main(String[] args) {

	CliHandler handler = new CliHandler();

	handler.init();

	try {
	    handler.execute(args);
	} catch (ParseException | IOException e) {
	    System.err.println("Parsing failed.  Reason: " + e.getMessage() + " " + e.toString());

	    // e.printStackTrace();
	}

    }
}
