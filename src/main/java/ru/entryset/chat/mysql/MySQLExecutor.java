package ru.entryset.chat.mysql;

import ru.entryset.chat.EntryChat;
import ru.entryset.chat.message.Convector;
import ru.entryset.core.EntryCore;

public class MySQLExecutor {

    public static void createTableProducts() {
        EntryCore.getInstance().getDatabase().execute(EntryChat.getInstance().getResource("deploy.sql"));
    }

    public static void check() {

        EntryCore.getInstance().getDatabase().select("SELECT * FROM `entrychat`", rs -> {
            while (rs.next()) {
                String hash = rs.getString(2);
                EntryChat.getInstance().time = Convector.deserializeMap(hash);
            }
        });
    }

    public static void update(){
        EntryCore.getInstance().getDatabase().select("SELECT * FROM `entrychat`", rs -> {
            int x = 0;
            while (rs.next()) {
                x++;
            }
            if(x > 0){
                EntryCore.getInstance().getDatabase().update("UPDATE `entrychat` SET `hash` = ? WHERE `id` = 1", Convector.serializeMap(EntryChat.getInstance().time));
            } else {
                EntryCore.getInstance().getDatabase().update("INSERT INTO `entrychat` (`id`, `hash`) VALUES (?, ?)", 1, Convector.serializeMap(EntryChat.getInstance().time));
            }
        });
    }
}
