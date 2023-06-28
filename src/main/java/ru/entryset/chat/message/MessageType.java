package ru.entryset.chat.message;

import ru.entryset.chat.EntryChat;

public enum MessageType {

    LOCAL(EntryChat.config.getSettings().getString("local")),
    GLOBAL(EntryChat.config.getSettings().getString("global"));

    private final String format;

    MessageType(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }

}
