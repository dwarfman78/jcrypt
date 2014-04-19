package fr.dwarf.jcrypt;

import java.io.IOException;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;

import org.apache.commons.compress.compressors.CompressorException;

/**
 * Interface de crypto au niveau fichier.
 * 
 * @author flecorre
 * 
 */
public interface FileCipherService {

    /**
     * Service d'encryption d'un fichier.
     * 
     * @param in
     *            entrée.
     * @param out
     *            sortie.
     * @param key
     *            clé.
     * @param compress
     *            compression gzip.
     * @throws NoSuchAlgorithmException
     *             exception.
     * @throws NoSuchPaddingException
     *             exception.
     * @throws InvalidKeyException
     *             exception.
     * @throws IOException
     *             exception.
     */
    void cipher(Path in, Path out, String key, Boolean compress) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
	    IOException, CompressorException;

    /**
     * Service de decryption d'un fichier.
     * 
     * @param in
     *            fichier en entrée (à décrypter)
     * @param out
     *            fichier en sortie.
     * @param key
     *            clé.
     * @param compress
     *            décompression gzip.
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IOException
     */
    void decipher(Path in, Path out, String key, Boolean compress) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
	    IOException, CompressorException;

}
