package fr.dwarf.jcrypt;

import java.io.IOException;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Classe de gestion de la ligne de commande.
 * 
 * @author flecorre
 * 
 */
public class CliHandler {

    /**
     * Options dispo dans la cli.
     */
    private Options options = new Options();

    /**
     * Parser de ligne.
     */
    private CommandLineParser parser = new GnuParser();

    /**
     * Input
     */
    private static final String ARG_INPUT = "input";

    /**
     * Output
     */
    private static final String ARG_OUTPUT = "output";

    /**
     * Key
     */
    private static final String ARG_KEY = "key";

    /**
     * Operation
     */
    private static final String ARG_OPERATION = "operation";

    /**
     * Aide.
     */
    private static final String ARG_HELP = "help";

    /**
     * Verbose
     */
    private static final String ARG_VERBOSE = "verbose";

    /**
     * Compress
     */
    private static final String ARG_COMPRESS = "compress";

    /**
     * Initialisation des options.
     */
    public void init() {
	options.addOption(OptionBuilder.withLongOpt(ARG_INPUT).hasArg().withArgName("INPUT DIR OR FILE").create());
	options.addOption(OptionBuilder.withLongOpt(ARG_OUTPUT).hasArg().withArgName("OUTPUT DIR").create());
	options.addOption(OptionBuilder.withLongOpt(ARG_KEY).hasArg().withArgName("KEY").create());
	options.addOption(OptionBuilder.withLongOpt(ARG_OPERATION).hasArg().withArgName("C (cipher) or D (decipher)").create());
	options.addOption(OptionBuilder.withLongOpt(ARG_HELP).withDescription("Display this help").create());
	options.addOption(OptionBuilder.withLongOpt(ARG_VERBOSE).withDescription("Display debug infos to std out").create());
	options.addOption(OptionBuilder.withLongOpt(ARG_COMPRESS).withDescription("Compress output").create());
    }

    /**
     * Exécution du programme en fonction des arguments.
     * 
     * @param args
     *            arguments.
     * @throws IOException
     */
    public void execute(String[] args) throws ParseException, IOException {

	// parse the command line arguments
	CommandLine line = parser.parse(options, args);

	if (line.hasOption(ARG_HELP)) {
	    HelpFormatter formatter = new HelpFormatter();
	    formatter.printHelp("jcrypt-cli", options);
	} else if (line.hasOption(ARG_INPUT) && line.hasOption(ARG_OUTPUT) && line.hasOption(ARG_KEY) && line.hasOption(ARG_OPERATION)) {
	    // Tous les arguments sont présents.

	    FileVisitor<Path> fileVisitor = new JCryptFileVisitor(line.getOptionValues(ARG_KEY)[0], Paths.get(line.getOptionValues(ARG_OUTPUT)[0]),
		    line.getOptionValues(ARG_OPERATION)[0], line.hasOption(ARG_VERBOSE), line.hasOption(ARG_COMPRESS));

	    String[] inputs = line.getOptionValues(ARG_INPUT);

	    for (String input : inputs) {
		Path p = Paths.get(input);
		Files.walkFileTree(p, fileVisitor);
	    }

	} else {
	    throw new ParseException("Missing argument");
	}

    }
}
