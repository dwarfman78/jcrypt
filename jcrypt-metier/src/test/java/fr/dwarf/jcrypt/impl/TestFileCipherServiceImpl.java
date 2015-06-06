package fr.dwarf.jcrypt.impl;

import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertTrue;

/**
 * Test de la classe FileCipherServiceImpl.
 *
 * @author flecorre
 */
public class TestFileCipherServiceImpl
{

    /**
     * clé de cryptage.
     */
    private static final String KEY = "KEY";

    private static final String INFILE = "/in.txt";

    private static final String OUTCIPFILE = "/outcip.txt";

    private static final String OUTFILE = "/out.txt";

    private static final String ROOT = "src/test/resources/";

    private static final Path in = Paths.get(ROOT, INFILE);

    private static final Path outcip = Paths.get(ROOT, OUTCIPFILE);

    private static final Path out = Paths.get(ROOT, OUTFILE);

    FileCipherServiceImpl test = new FileCipherServiceImpl();

    @Test
    public void testCipher() throws Exception
    {

        test.cipher(in, outcip, KEY, Boolean.FALSE);

        test.decipher(outcip, out, KEY, Boolean.FALSE);

        assertTrue("The files differ!", FileUtils.contentEquals(in.toFile(), out.toFile()));

        cleanTheMess();

        test.cipher(in, outcip, KEY, Boolean.TRUE);

        test.decipher(outcip, out, KEY, Boolean.TRUE);

        assertTrue("The files differ!", FileUtils.contentEquals(in.toFile(), out.toFile()));

        cleanTheMess();

        test.cipher(in, outcip, KEY, Boolean.FALSE);

        try
        {
            test.decipher(outcip, out, KEY, Boolean.TRUE);
        } catch (CompressorException ce)
        {
            test.decipher(outcip, out, KEY, Boolean.FALSE);
        }

        assertTrue("The files differ!", FileUtils.contentEquals(in.toFile(), out.toFile()));

    }

    @After
    public void cleanTheMess() throws Exception
    {
        Files.delete(outcip);
        Files.delete(out);
    }
}
