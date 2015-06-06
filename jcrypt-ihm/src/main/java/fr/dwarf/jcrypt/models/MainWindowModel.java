package fr.dwarf.jcrypt.models;

import fr.dwarf.jcrypt.common.JcryptMode;
import org.apache.pivot.io.FileList;

import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Classe contenant les informations manipulées par le controlleur MainWindow.
 *
 * @author flecorre
 */
public class MainWindowModel
{

    /**
     * Pool de thread.
     */
    private ExecutorService executor = Executors.newFixedThreadPool(10);

    /**
     * Mode, crypte ou decrypte.
     */
    private JcryptMode mode = JcryptMode.CYPHER;

    /**
     * Fichiers droppés.
     */
    private FileList fileListLocal = null;

    /**
     * Dossier de sortie.
     */
    private Path dossierOut;

    /**
     * Clé de cryptage.
     */
    private String key = null;

    /**
     * Couper les originaux.
     */
    private Boolean cut = Boolean.FALSE;

    /**
     * Booléen destination = src.
     */
    private Boolean oneFolder = Boolean.TRUE;

    /**
     * Compression gzip.
     */
    private Boolean compress = Boolean.FALSE;

    public void toggleCompress()
    {
        this.compress = !this.compress;
    }

    /**
     * Toggle le booléen cut.
     */
    public void toggleCut()
    {
        cut = !cut;
    }

    /**
     * Toggle le booléen "source = destination"
     */
    public void toggleOneFolder()
    {
        oneFolder = !oneFolder;
    }

    /**
     * @return the key
     */
    public String getKey()
    {
        return key;
    }

    /**
     * @param key the key to set
     */
    public void setKey(String key)
    {
        this.key = key;
    }

    /**
     * @return the executor
     */
    public ExecutorService getExecutor()
    {
        return executor;
    }

    /**
     * @param executor the executor to set
     */
    public void setExecutor(ExecutorService executor)
    {
        this.executor = executor;
    }

    /**
     * @return the mode
     */
    public JcryptMode getMode()
    {
        return mode;
    }

    /**
     * @param mode the mode to set
     */
    public void setMode(JcryptMode mode)
    {
        this.mode = mode;
    }

    /**
     * @return the fileListLocal
     */
    public FileList getFileListLocal()
    {
        return fileListLocal;
    }

    /**
     * @param fileListLocal the fileListLocal to set
     */
    public void setFileListLocal(FileList fileListLocal)
    {
        this.fileListLocal = fileListLocal;
    }

    /**
     * @return the dossierOut
     */
    public Path getDossierOut()
    {
        return dossierOut;
    }

    /**
     * @param dossierOut the dossierOut to set
     */
    public void setDossierOut(Path dossierOut)
    {
        this.dossierOut = dossierOut;
    }

    /**
     * @return the cut
     */
    public Boolean getCut()
    {
        return cut;
    }

    /**
     * @return the oneFolder
     */
    public Boolean getOneFolder()
    {
        return oneFolder;
    }

    /**
     * @param cut the cut to set
     */
    public void setCut(Boolean cut)
    {
        this.cut = cut;
    }

    /**
     * @return the compress
     */
    public Boolean getCompress()
    {
        return compress;
    }

    /**
     * @param compress the compress to set
     */
    public void setCompress(Boolean compress)
    {
        this.compress = compress;
    }

}
