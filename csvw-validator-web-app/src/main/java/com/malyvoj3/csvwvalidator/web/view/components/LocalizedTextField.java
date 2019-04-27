package com.malyvoj3.csvwvalidator.web.view.components;

import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;

public class LocalizedTextField extends TextField implements LocaleChangeObserver {

    private String propertyName;

    public LocalizedTextField(String propertyName) {
        this.propertyName = propertyName;
    }

    public LocalizedTextField(String label, String propertyName) {
        super(label);
        this.propertyName = propertyName;
    }

    @Override
    public void localeChange(LocaleChangeEvent localeChangeEvent) {
        setLabel(getTranslation(propertyName));
    }

}
