package fr.dwarf.jcrypt;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Interface de service de crypto.
 *
 * @author flecorre
 */
public interface ByteCipherService
{

    /**
     * Méthode d'encryption avec clé.
     *
     * @param key       clé.
     * @param clearFile octets à encrypter.
     * @return octets cryptés.
     * @throws NoSuchAlgorithmException  exception.
     * @throws NoSuchPaddingException    exception.
     * @throws InvalidKeyException       exception.
     * @throws IllegalBlockSizeException exception.
     * @throws BadPaddingException       exception.
     */
    byte[] cipher(String key, byte[] clearFile) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException;

    /**
     * Méthode de decryption avec clé.
     *
     * @param key          clé
     * @param scrumbleFile
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    byte[] decipher(String key, byte[] scrumbleFile) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException;

}
