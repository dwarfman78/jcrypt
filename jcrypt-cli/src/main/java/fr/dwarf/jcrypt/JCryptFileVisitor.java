package fr.dwarf.jcrypt;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.Date;

import javax.crypto.NoSuchPaddingException;

import org.apache.commons.compress.compressors.CompressorException;

import fr.dwarf.jcrypt.impl.FileCipherServiceImpl;

/**
 * Classe de traitement des sous dossier en entrée. (option --input)
 * 
 * @author flecorre
 * 
 */
public class JCryptFileVisitor extends SimpleFileVisitor<Path> {

    /**
     * Clé.
     */
    private String key;
    /**
     * Dossier de sortie.
     */
    private Path output;

    /**
     * Operation.
     */
    private String operation;

    /**
     * CIPHER.
     */
    private static final String CIPHER = "C";

    /**
     * DECIPHER.
     */
    private static final String DECIPHER = "D";

    /**
     * Verbose
     */
    private final Boolean verbose;

    /**
     * Compress
     */
    private final Boolean compress;

    /**
     * Service de crypto.
     */
    private final FileCipherService service = new FileCipherServiceImpl();

    /**
     * ctor d'init.
     * 
     * @param key
     * @param output
     * @param operation
     */
    public JCryptFileVisitor(String key, Path output, String operation, Boolean verbose, Boolean compress) {
	super();
	this.key = key;
	this.output = output;
	this.operation = operation;
	this.verbose = verbose;
	this.compress = compress;
    }

    /**
     * Méthode appelée pour chaque fichier.
     * 
     * @param file
     * @param attrs
     * @return
     * @throws IOException
     */
    @Override
    public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {

	if (operation != null) {

	    switch (operation) {

	    case CIPHER:
		if (attrs.isRegularFile()) {

		    Thread t = new Thread() {
			public void run() {
			    try {
				Long start = null, stop = null;
				if (verbose) {
				    start = (new Date()).getTime();

				    System.out.println(MessageFormat.format("thread : {0}, current file {1}, starts @ {2}", Thread.currentThread()
					    .getId(), file.toString(), start.toString()));
				}
				service.cipher(file, Paths.get(output.toString(), file.getFileName() + ".jcrypt"), key, compress);
				if (verbose) {
				    stop = (new Date()).getTime();
				    System.out.println(MessageFormat.format("thread : {0}, current file {1}, stops @ {2}, lasted : {3}", Thread
					    .currentThread().getId(), file.toString(), stop.toString(), Long.toString(stop - start)));
				}
			    } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IOException | CompressorException e) {
				System.err.println("Error while encrypting" + e.getMessage());
			    }
			}
		    };
		    t.start();

		}

		break;
	    case DECIPHER:
		if (attrs.isRegularFile()) {
		    Thread t = new Thread() {
			public void run() {
			    try {
				Boolean compressorException = Boolean.FALSE;
				if (attrs.isRegularFile() && file.toString().endsWith(".jcrypt")) {
				    Long start = null, stop = null;
				    if (verbose) {
					start = (new Date()).getTime();

					System.out.println(MessageFormat.format("thread : {0}, current file {1}, starts @ {2}", Thread
						.currentThread().getId(), file.toString(), start.toString()));
				    }
				    try {
					service.decipher(file, Paths.get(output.toString(), file.getFileName().toString().replace(".jcrypt", "")),
						key, Boolean.TRUE);
				    } catch (CompressorException e) {
					compressorException = Boolean.TRUE;
				    }
				    
				    if(compressorException){
					service.decipher(file, Paths.get(output.toString(), file.getFileName().toString().replace(".jcrypt", "")),
						key, Boolean.FALSE);
				    }

				    if (verbose) {
					stop = (new Date()).getTime();
					System.out.println(MessageFormat.format("thread : {0}, current file {1}, stops @ {2}, lasted : {3}", Thread
						.currentThread().getId(), file.toString(), stop.toString(), Long.toString(stop - start)));
				    }

				}
			    } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IOException | CompressorException e) {
				System.err.println("Error while decrypting" + e.getMessage());
			    }
			}
		    };
		    t.start();
		}
		break;
	    default:
		System.err.println("Error, unrecognized operation : " + operation);
		break;

	    }
	}

	return super.visitFile(file, attrs);

    }
}
