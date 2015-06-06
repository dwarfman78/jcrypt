package fr.dwarf.jcrypt.impl;

import fr.dwarf.jcrypt.FileCipherService;
import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorStreamFactory;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class FileCipherServiceImpl implements FileCipherService
{

    private final static String ALGORITHM = "Blowfish";

    @Override
    public void cipher(Path in, Path out, String key, Boolean compress) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            IOException, CompressorException
    {

        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);

        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

        CipherInputStream cis = new CipherInputStream(Files.newInputStream(in, StandardOpenOption.READ), cipher);

        OutputStream outFinal = Files.newOutputStream(out,
                new StandardOpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING});

        if (compress)
        {
            outFinal = new CompressorStreamFactory().createCompressorOutputStream(CompressorStreamFactory.GZIP, outFinal);
        }

        byte[] data = new byte[1024];
        int read = cis.read(data);
        while (read != -1)
        {
            outFinal.write(data, 0, read);
            read = cis.read(data);

        }
        outFinal.flush();
        outFinal.close();
        cis.close();
    }

    @Override
    public void decipher(Path in, Path out, String key, Boolean compress) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IOException, CompressorException
    {
        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);

        cipher.init(Cipher.DECRYPT_MODE, skeySpec);

        InputStream inputFinal = Files.newInputStream(in, StandardOpenOption.READ);

        if (compress)
        {
            inputFinal = new CompressorStreamFactory().createCompressorInputStream(CompressorStreamFactory.GZIP, inputFinal);

        }

        CipherInputStream cis = new CipherInputStream(inputFinal, cipher);

        OutputStream fos = Files.newOutputStream(out, new StandardOpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING});

        byte[] data = new byte[1024];
        int read = cis.read(data);
        while (read != -1)
        {
            fos.write(data, 0, read);
            read = cis.read(data);

        }
        fos.flush();
        fos.close();
        cis.close();

    }

}
