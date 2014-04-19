package fr.dwarf.jcrypt.controllers;

import java.net.URL;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Sheet;
import org.apache.pivot.wtk.TextInput;

public class PromptWindow extends Sheet implements Bindable {

    @BXML
    TextInput key;

    /**
     * @return the key
     */
    public TextInput getKey() {
	return key;
    }

    /**
     * @param key
     *            the key to set
     */
    public void setKey(TextInput key) {
	this.key = key;
    }

    @Override
    public void initialize(Map<String, Object> arg0, URL arg1, Resources arg2) {

    }

}
