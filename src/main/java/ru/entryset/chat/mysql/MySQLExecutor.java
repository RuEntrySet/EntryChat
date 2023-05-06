package ru.entryset.chat.mysql;

import ru.entryset.EntryAPI;
import ru.entryset.chat.main.Main;
import ru.entryset.chat.message.Convector;

public class MySQLExecutor {

    public static void createTableProducts() {
        EntryAPI.base.execute(Main.getInstance().getResource("deploy.sql"));
    }

    public static void check() {

        EntryAPI.base.select("SELECT * FROM `entrychat`", rs -> {
            while (rs.next()) {
                String hash = rs.getString(2);
                Main.getInstance().time = Convector.deserializeMap(hash);
            }
        });
    }

    public static void update(){
        EntryAPI.base.select("SELECT * FROM `entrychat`", rs -> {
            int x = 0;
            while (rs.next()) {
                x++;
            }
            if(x > 0){
                EntryAPI.base.update("UPDATE `entrychat` SET `hash` = ? WHERE `id` = 1", Convector.serializeMap(Main.getInstance().time));
            } else {
                EntryAPI.base.update("INSERT INTO `entrychat` (`id`, `hash`) VALUES (?, ?)", 1, Convector.serializeMap(Main.getInstance().time));
            }
        });
    }

    public static void update2(){
        int x = EntryAPI.base.select("SELECT * FROM `entrychat`", rs -> {
            int z = 0;
            while (rs.next()) {
                z++;
            }
            return z;
        }).join();
        if(x > 0){
            EntryAPI.base.update("UPDATE `entrychat` SET `hash` = ? WHERE `id` = 1", Convector.serializeMap(Main.getInstance().time)).join();
        } else {
            EntryAPI.base.update("INSERT INTO `entrychat` (`id`, `hash`) VALUES (?, ?)", 1, Convector.serializeMap(Main.getInstance().time)).join();
        }
    }
}
