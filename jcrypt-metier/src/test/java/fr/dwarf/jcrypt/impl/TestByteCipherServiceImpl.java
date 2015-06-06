package fr.dwarf.jcrypt.impl;

import junit.framework.Assert;
import org.junit.Test;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * Test de la classe CipherServiceImpl.
 *
 * @author flecorre
 */
public class TestByteCipherServiceImpl
{

    private ByteCipherServiceImpl test = null;

    private static final String key = "KEY";

    @Test
    public void testCipher() throws GeneralSecurityException, IOException
    {
        test = new ByteCipherServiceImpl();

        byte[] in = new byte[]{(byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32,
                (byte) 0x56, (byte) 0x34, (byte) 0xE3, (byte) 0x03};


        byte[] out = test.cipher(key, in);

        Assert.assertNotNull(out);
        Assert.assertFalse(out.equals(in));

        test = new ByteCipherServiceImpl();

        byte[] decrypt = test.decipher(key, out);

        Assert.assertNotNull(decrypt);
        Assert.assertTrue(decrypt.length > 0);

        for (int i = 0; i < decrypt.length; i++)
        {
            Assert.assertEquals(decrypt[i], in[i]);
        }
    }
}
