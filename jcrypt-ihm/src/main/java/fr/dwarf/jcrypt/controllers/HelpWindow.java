package fr.dwarf.jcrypt.controllers;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Button;
import org.apache.pivot.wtk.PushButton;
import org.apache.pivot.wtk.Sheet;

import java.net.URL;

/**
 * Fenêtre contenant l'aide / infos.
 *
 * @author flecorre
 */
public class HelpWindow extends Sheet implements Bindable
{

    @BXML
    private PushButton close;

    @Override
    public void initialize(Map<String, Object> arg0, URL arg1, Resources arg2)
    {

        close.getButtonPressListeners().add((Button button) -> {
            HelpWindow.this.close();
        });

    }
}
