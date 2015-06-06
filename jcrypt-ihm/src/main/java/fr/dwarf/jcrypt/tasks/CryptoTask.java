package fr.dwarf.jcrypt.tasks;

import fr.dwarf.jcrypt.helpers.JCryptFileVisitor;
import fr.dwarf.jcrypt.models.MainWindowModel;
import org.apache.pivot.util.concurrent.Task;
import org.apache.pivot.util.concurrent.TaskExecutionException;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Task pivot pour lancement asynchrone de la tâche de cryptographie.
 *
 * @author flecorre
 */
public class CryptoTask extends Task<Object>
{

    /**
     * Modèle contenant les informations entre les couches.
     */
    private MainWindowModel model = null;

    /**
     * Ctor init.
     *
     * @param model model.
     */
    public CryptoTask(MainWindowModel model)
    {
        super();
        this.model = model;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.pivot.util.concurrent.Task#execute()
     */
    @Override
    public Object execute() throws TaskExecutionException
    {

        if (model != null)
        {

            // Liste contenant les résultats des threads.
            List<Future<Path>> retours = new ArrayList<>();

            FileVisitor<Path> jcfv = new JCryptFileVisitor(retours, model);

            for (final File f : model.getFileListLocal())
            {

                try
                {
                    Files.walkFileTree(f.toPath(), jcfv);
                } catch (IOException e)
                {
                    throw new TaskExecutionException(e);
                }

            }

            for (Future<Path> retour : retours)
            {
                // Parcours des résulats des threads.
                try
                {
                    // Méthode bloquante tant que le thread n'a pas terminé..
                    retour.get();
                } catch (ExecutionException | InterruptedException e)
                {
                    // En cas de problème dans le thread on remonte une
                    // exception au niveau de la tâche pivot.
                    throw new TaskExecutionException(e);
                }
            }
        }
        return null;
    }
}
