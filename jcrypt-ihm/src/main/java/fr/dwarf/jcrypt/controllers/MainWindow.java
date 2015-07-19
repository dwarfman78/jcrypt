package fr.dwarf.jcrypt.controllers;

import fr.dwarf.jcrypt.common.JcryptMode;
import fr.dwarf.jcrypt.models.MainWindowModel;
import fr.dwarf.jcrypt.tasks.CryptoTask;
import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.BXMLSerializer;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.io.FileList;
import org.apache.pivot.serialization.SerializationException;
import org.apache.pivot.util.Resources;
import org.apache.pivot.util.concurrent.Task;
import org.apache.pivot.util.concurrent.TaskListener;
import org.apache.pivot.wtk.*;
import org.apache.pivot.wtk.Button;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.Window;

import java.awt.*;
import java.awt.Frame;
import java.awt.event.InputEvent;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Locale;
import java.util.MissingResourceException;

/**
 * Controller de la fenêtre principale de l'application.
 *
 * @author flecorre
 */
public class MainWindow extends Window implements Application, Bindable
{

    /**
     * Instance de la fenêtre.
     */
    private Window window = null;

    /**
     * Champ indiquant le dossier de sortie.
     */
    // @BXML
    // private TextInput outputDirectory;

    /**
     * Largeur de la fenêtre host.
     */
    private final static Integer WINDOW_WIDTH = 400;

    /**
     * Hauteur de la fenêtre host.
     */
    private final static Integer WINDOW_HEIGHT = 400;

    /**
     * Vue principale.
     */
    private final static String MAIN_VIEW = "/views/MainWindow.bxml";

    /**
     * Drop target pour déposer les fichiers.
     */
    @BXML
    private TablePane dropTarget = null;

    /**
     * Bouton cryptage.
     */
    @BXML
    private PushButton buttonCypher = null;

    /**
     * Bouton decryptage.
     */
    @BXML
    private PushButton buttonDecypher = null;

    /**
     * Fenêtre de saisie du mdp.
     */
    @BXML
    private PromptWindow dialog = null;

    /**
     * Fenetre d'infos /aide.
     */
    @BXML
    private HelpWindow help = null;

    /**
     * Fenêtre de saisie du dossier de sortie.
     */
    @BXML
    private PathWindow path = null;

    /**
     * Panel contenant les indicateurs d'activités.
     */
    @BXML
    private BoxPane activitiesPane = null;

    /**
     * Bouton suppression des originaux.
     */
    @BXML
    private PushButton buttonCut = null;

    /**
     * Bouton répertoire destination source identiques.
     */
    @BXML
    private PushButton buttonOneFolder = null;

    /**
     * Bouton d'aide.
     */
    @BXML
    private PushButton buttonHelp = null;

    /**
     * Bouton de compression.
     */
    @BXML
    private PushButton buttonZip = null;

    /**
     * Host window.
     */
    public java.awt.Frame host;

    /**
     * le modèle.
     */
    private MainWindowModel model = new MainWindowModel();

    /**
     * liste fichiers droppés.
     */
    private FileList fileListLocal;

    /**
     * Activités mappée par tache.
     */
    private HashMap<Task<Object>, ActivityIndicator> mapActivities = new HashMap<>();

    @Override
    public void startup(Display display, Map<String, String> properties) throws Exception
    {

        host = (Frame) display.getHostWindow();
        // définition de la taille de la fenêtre hote.
        host.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        // désactivation du bouton de maximisation.
        host.setResizable(false);

        // Parsing vue principale.
        BXMLSerializer bxmlSerializer = new BXMLSerializer();
        //window = (Window) bxmlSerializer.readObject(MainWindow.class, MAIN_VIEW);

        Resources resources = null;

        try
        {
            resources = new Resources("localization/Localization", Locale.getDefault());
        } catch (IOException | SerializationException | MissingResourceException exception)
        {
            resources = new Resources("localization/Localization", Locale.ENGLISH);
        }

        window = (Window) bxmlSerializer.readObject(MainWindow.class.getResource("/views/MainWindow.bxml"), resources);

        // Ouverture de la fenêtre.
        window.open(display);

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.apache.pivot.beans.Bindable#initialize(org.apache.pivot.collections
     * .Map, java.net.URL, org.apache.pivot.util.Resources)
     */
    @Override
    public void initialize(Map<String, Object> namespace, URL location, Resources resources)
    {

        // Initialisation de l'écran.

        path.setModel(model);

        initDropTarget();

        initButtons();

    }

    /**
     * Initialisation listener boutons.
     */
    private void initButtons()
    {

        buttonZip.getButtonPressListeners().add((Button button) -> {
            model.toggleCompress();
            buttonZip.getStyles().put("toolbar", !model.getCompress());
        });

        buttonHelp.getButtonPressListeners().add((Button button) -> help.open(MainWindow.this));

        buttonOneFolder.getButtonPressListeners().add((Button button) -> {
            model.toggleOneFolder();
            buttonOneFolder.getStyles().put("toolbar", !model.getOneFolder());
        });

        // par défaut activé.
        buttonOneFolder.setSelected(model.getOneFolder());

        buttonCut.getButtonPressListeners().add((Button button) -> {
            model.toggleCut();
            buttonCut.getStyles().put("toolbar", !model.getCut());
        });

        buttonCypher.getButtonPressListeners().add((Button button) -> {
            model.setMode(JcryptMode.CYPHER);
            buttonCypher.getStyles().put("toolbar", false);
            buttonDecypher.getStyles().put("toolbar", true);
            buttonCut.setVisible(true);
            buttonZip.setVisible(true);
        });

        buttonDecypher.getButtonPressListeners().add((Button button) -> {
            model.setMode(JcryptMode.DECYPHER);
            buttonDecypher.getStyles().put("toolbar", false);
            buttonCypher.getStyles().put("toolbar", true);
            buttonCut.setVisible(false);
            buttonZip.setVisible(false);
        });
    }

    /**
     * Ajout d'activité
     *
     * @param task tache.
     */
    private void addActivities(CryptoTask task)
    {

        buttonDecypher.setEnabled(false);
        buttonCypher.setEnabled(false);
        buttonCut.setEnabled(false);
        buttonOneFolder.setEnabled(false);
        buttonZip.setEnabled(false);

        ActivityIndicator ai = new ActivityIndicator();
        ai.setActive(true);
        ai.setPreferredHeight(24);
        ai.setPreferredWidth(24);
        // Ajout de l'ai au panel.
        activitiesPane.add(ai);
        // On garde l'instance dans une map pour la retrouver à la fin de la
        // tâche (cf removeActivities).
        mapActivities.put(task, ai);
    }

    /**
     * Suppression d'activité
     *
     * @param task tache terminée.
     */
    private void removeActivities(Task<Object> task)
    {
        activitiesPane.remove(mapActivities.remove(task));

        if (mapActivities.isEmpty())
        {
            buttonDecypher.setEnabled(true);
            buttonCypher.setEnabled(true);
            buttonCut.setEnabled(true);
            buttonOneFolder.setEnabled(true);
            buttonZip.setEnabled(true);
        }
    }

    private void doOperation()
    {

        if (model.getDossierOut() != null || model.getOneFolder())
        {
            // un dossier de sortie doit être sélectionné ou mode
            // src=destination

            if (fileListLocal != null && fileListLocal.getLength() > 0)
            {

                dialog.open(MainWindow.this, (Sheet sheet) -> {            // à la fermeture de la popup on déclenche
                    // le traitement.

                    // on peuple le modèle
                    // avec la clé.
                    model.setKey(((PromptWindow) sheet).getKey().getText());
                    ((PromptWindow) sheet).getKey().setText("");

                    // Avec la liste de fichiers droppés.
                    model.setFileListLocal(fileListLocal);

                    CryptoTask task = new CryptoTask(model);

                    TaskListener<Object> taskListener = new TaskListener<Object>()
                    {
                        @Override
                        public void taskExecuted(Task<Object> task)
                        {
                            // une fois la tâche exécutée on
                            // retire l'indicateur d'activité.
                            removeActivities(task);
                        }

                        @Override
                        public void executeFailed(Task<Object> task)
                        {

                            // En cas de problème on retire
                            // l'indicateur d'activité.
                            removeActivities(task);

                            // Affichage du message d'erreur et
                            // de l'exception.
                            Alert.alert("Erreur durant l'exécution de la tâche : " + task.getFault().toString(), MainWindow.this);

                            // task.getFault().printStackTrace();
                        }
                    };

                    // Ajout de l'indicateur d'activité.
                    addActivities(task);

                    // Exécution de la tâche dans un nouveau
                    // thread.
                    task.execute(new TaskAdapter<>(taskListener));


                });
            }
        } else
        {
            Alert.alert("Sélectionnez un dossier de sortie ou le mode source = destination", MainWindow.this);
        }
    }

    /**
     * Initialisation du listener de drop target.
     */
    private void initDropTarget()
    {

        dropTarget.setDropTarget(new DropTarget()
        {

            @Override
            public DropAction userDropActionChange(Component component, Manifest dragContent, int supportedDropActions, int x, int y,
                                                   DropAction userDropAction)
            {
                return (dragContent.containsFileList() ? DropAction.COPY : null);
            }

            @Override
            public DropAction drop(Component component, final Manifest dragContent, int supportedDropActions, int x, int y, DropAction userDropAction)
            {
                DropAction dropAction = null;

                try
                {
                    fileListLocal = dragContent.getFileList();
                } catch (IOException e1)
                {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

                if (!model.getOneFolder())
                {
                    // si on est pas en mode src=destination il faut
                    // afficher le file picker.
                    path.open(MainWindow.this, (Sheet sheet) -> {
                        String text = path.getOutputDirectory().getText();

                        if (text != null && !text.equals(""))
                        {
                            model.setDossierOut(Paths.get(text));

                            doOperation();

                        }
                    });
                } else
                {

                    doOperation();

                }

                dropAction = DropAction.COPY;

                // MainWindow to front.
                MainWindow.this.getDisplay().getHostWindow().toFront();

                // On simule un clic pour donner le focus.
                Robot robot;
                try
                {
                    robot = new Robot();
                    robot.mousePress(InputEvent.BUTTON1_MASK);
                    robot.mouseRelease(InputEvent.BUTTON1_MASK);
                } catch (AWTException e)
                {
                    Alert.alert("Erreur lors du drop (focus) : " + e.toString(), MainWindow.this);
                }

                return dropAction;
            }

            @Override
            public DropAction dragMove(Component component, Manifest dragContent, int supportedDropActions, int x, int y, DropAction userDropAction)
            {

                return (dragContent.containsFileList() ? DropAction.COPY : null);
            }

            @Override
            public void dragExit(Component component)
            {

            }

            @Override
            public DropAction dragEnter(Component component, Manifest dragContent, int supportedDropActions, DropAction userDropAction)
            {
                DropAction dropAction = null;

                if (dragContent.containsFileList() && DropAction.COPY.isSelected(supportedDropActions))
                {
                    dropAction = DropAction.COPY;
                }

                return dropAction;
            }
        });
    }

    @Override
    public boolean shutdown(boolean optional)
    {
        if (window != null)
        {
            window.close();
        }

        return false;
    }

    @Override
    public void suspend()
    {
    }

    @Override
    public void resume()
    {
    }

}
