package fr.dwarf.jcrypt.helpers;

import fr.dwarf.jcrypt.models.MainWindowModel;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

public class JCryptFileVisitor extends SimpleFileVisitor<Path>
{

    /**
     * Résultats des threads.
     */
    private List<Future<Path>> retours = new ArrayList<>();

    /**
     * model
     */
    private MainWindowModel model = null;

    /**
     * @param retours
     * @param model
     * @param currentFile
     */
    public JCryptFileVisitor(List<Future<Path>> retours, MainWindowModel model)
    {
        super();
        this.retours = retours;
        this.model = model;
    }

    @Override
    public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException
    {
        if (attrs.isRegularFile() && model != null && retours != null && file != null)
        {

            // Path du nouveau fichier créé.
            Path newFilePath = null;

            // Dossier de sortie est celui du model. (champ input)
            String dossierOut;

            if (model.getOneFolder())
            {
                // en mode "one folder" cad dossier src = dossier
                // destination
                dossierOut = file.getParent().toString();
            } else
            {
                // Sinon dossier sortie c'est le dossier saisi dans le file
                // picker.;
                dossierOut = model.getDossierOut().toString();
            }

            switch (model.getMode())
            {
                case CYPHER:
                    // Ajout de l'extension .jcrypt si cryptage.
                    newFilePath = Paths.get(dossierOut, file.getFileName() + ".jcrypt");
                    break;
                case DECYPHER:
                    // Suppression de l'extension .jcrypt si decryptage.
                    newFilePath = Paths.get(dossierOut, file.getFileName().toString().replace(".jcrypt", ""));
                    break;
            }

            // Instanciation du sous thread.
            CryptoRunner runner = new CryptoRunner(model, file, newFilePath);

            // Soumission du sous thread à l'ordonnanceur.
            retours.add(model.getExecutor().submit(runner));
        }

        return super.visitFile(file, attrs);
    }
}
