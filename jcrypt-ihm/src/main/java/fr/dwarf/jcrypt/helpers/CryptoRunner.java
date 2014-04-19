package fr.dwarf.jcrypt.helpers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Callable;

import javax.crypto.NoSuchPaddingException;

import org.apache.commons.compress.compressors.CompressorException;
import org.apache.pivot.util.concurrent.TaskExecutionException;

import fr.dwarf.jcrypt.FileCipherService;
import fr.dwarf.jcrypt.impl.FileCipherServiceImpl;
import fr.dwarf.jcrypt.models.MainWindowModel;

/**
 * Classe d'appels multithreadés au service de crypto.
 * 
 * @author flecorre
 * 
 */
public class CryptoRunner implements Callable<Path> {

    /**
     * Service de crypto.
     */
    private FileCipherService service = null;

    private MainWindowModel model = null;

    private Path dataIn;

    private Path dataOut;

    public CryptoRunner(MainWindowModel model, Path dataIn, Path dataOut) {
	super();
	this.service = new FileCipherServiceImpl();

	this.model = model;

	this.dataIn = dataIn;

	this.dataOut = dataOut;
    }

    @Override
    public Path call() throws Exception {

	switch (model.getMode()) {

	case CYPHER:
	    try {

		service.cipher(dataIn, dataOut, model.getKey(), model.getCompress());
		if (model.getCut()) {
		    // si nécessaire, supprimer les originaux.
		    Files.delete(dataIn);
		}

	    } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IOException e) {
		throw new TaskExecutionException(e);
	    }
	    break;
	case DECYPHER:
	    try {
		try {
		    // On considère que le fichier a été compressé.
		    service.decipher(dataIn, dataOut, model.getKey(), Boolean.TRUE);
		} catch (CompressorException ce) {
		    // Si le fichier n'est pas compressé alors exception, on
		    // considère donc qu'il n'a pas été compressé.
		    service.decipher(dataIn, dataOut, model.getKey(), Boolean.FALSE);
		}

	    } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IOException e) {
		throw new TaskExecutionException(e);
	    }
	    break;
	}

	return dataIn;
    }
}
