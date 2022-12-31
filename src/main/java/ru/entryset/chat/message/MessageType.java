package ru.entryset.chat.message;

import ru.entryset.api.tools.Messager;
import ru.entryset.chat.main.Main;

public enum MessageType {

    LOCAL(Messager.color(Main.config.getSettings("local"))),
    GLOBAL(Messager.color(Main.config.getSettings("global")));

    private final String format;

    MessageType(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }

}
