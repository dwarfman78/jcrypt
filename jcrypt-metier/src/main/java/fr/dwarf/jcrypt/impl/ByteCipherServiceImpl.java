package fr.dwarf.jcrypt.impl;

import fr.dwarf.jcrypt.ByteCipherService;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Classe d'implémentation du service de crypto.
 *
 * @author flecorre
 */
public class ByteCipherServiceImpl implements ByteCipherService
{

    private final static String ALGORITHM = "Blowfish";

    @Override
    public byte[] cipher(String key, byte[] clearFile) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException
    {

        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);

        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

        return cipher.doFinal(clearFile);

    }

    @Override
    public byte[] decipher(String key, byte[] scrumbleFile) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException
    {

        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);

        cipher.init(Cipher.DECRYPT_MODE, skeySpec);

        return cipher.doFinal(scrumbleFile);

    }

}
