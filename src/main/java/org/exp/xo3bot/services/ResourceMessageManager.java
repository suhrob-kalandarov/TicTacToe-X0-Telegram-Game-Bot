package org.exp.xo3bot.services;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.ResourceBundle;

@Service
public class ResourceMessageManager {

    private ResourceBundle bundle;

    // Bundleni o'rnatish va lokalni sozlash
    @PostConstruct
    public void init() {
        this.bundle = ResourceBundle.getBundle("messages", Locale.getDefault());
    }

    public void loadBundle(String baseName, Locale locale) {
        this.bundle = ResourceBundle.getBundle(baseName, locale);
    }

    // Locale o'zgartirish uchun metod
    public void setLocale(Locale locale) {
        this.bundle = ResourceBundle.getBundle("messages", locale);
    }

    // Resurslardan stringni olish
    public String getString(String key) {
        if (bundle == null) {
            throw new IllegalStateException("Resource bundle is not initialized.");
        }
        return bundle.getString(key);
    }
}


/*

package org.exp.xo3bot.services;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Locale;
import java.util.ResourceBundle;

@Service
public class ResourceMessageManager {

    private ResourceBundle bundle;

    // Bundle ni inicializatsiya qilish va default locale (inglizcha) o'rnatish
    @PostConstruct
    public void init() {
        this.bundle = ResourceBundle.getBundle("messages", Locale.ENGLISH);
    }

    // Locale o'zgartirish uchun metod
    public void setLocale(Locale locale) {
        this.bundle = ResourceBundle.getBundle("messages", locale);
    }

    // Resurslardan stringni olish
    public String getString(String key) {
        if (bundle == null) {
            throw new IllegalStateException("Resource bundle is not initialized.");
        }
        return bundle.getString(key);
    }
}


*/
