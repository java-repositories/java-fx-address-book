package org.example.utils;

import lombok.Getter;
import org.example.objects.Lang;

import java.util.Locale;

public class LocaleManager {

    public static final Locale RU_LOCALE = new Locale("ru");
    public static final Locale EN_LOCALE = new Locale("en");

    @Getter
    private static Lang currentLang;

    public static void setCurrentLang(Lang currentLang) {
        LocaleManager.currentLang = currentLang;
    }
}
