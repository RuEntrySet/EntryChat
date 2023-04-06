package ru.entryset.chat.message;

import ru.entryset.chat.main.Main;

public enum MessageType {

    LOCAL(Main.config.getSettings().getString("local")),
    GLOBAL(Main.config.getSettings().getString("global"));

    private final String format;

    MessageType(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }

}
