package ru.entryset.chat.mysql;

import ru.entryset.chat.main.Main;
import ru.entryset.chat.message.Convector;

public class MySQLExecutor {

    public static void createTableProducts() {
        Main.base.execute(Main.getInstance().getResource("deploy.sql"));
    }

    public static void check() {

        Main.base.select("SELECT * FROM `entrychat`", rs -> {
            while (rs.next()) {
                String hash = rs.getString(2);
                Main.getInstance().time = Convector.deserializeMap(hash);
            }
        });
    }

    public static void update(){
        Main.base.select("SELECT * FROM `entrychat`", rs -> {
            int x = 0;
            while (rs.next()) {
                x++;
            }
            if(x > 0){
                Main.base.update("UPDATE `entrychat` SET `hash` = ? WHERE `id` = 1", Convector.serializeMap(Main.getInstance().time));
            } else {
                Main.base.update("INSERT INTO `entrychat` (`id`, `hash`) VALUES (?, ?)", 1, Convector.serializeMap(Main.getInstance().time));
            }
        });
    }

    public static void update2(){
        int x = Main.base.select("SELECT * FROM `entrychat`", rs -> {
            int z = 0;
            while (rs.next()) {
                z++;
            }
            return z;
        }).join();
        String serialize = Convector.serializeMap(Main.getInstance().time);
        if(x > 0){
            Main.base.update("UPDATE `entrychat` SET `hash` = ? WHERE `id` = 1", Convector.serializeMap(Main.getInstance().time)).join();
        } else {
            Main.base.update("INSERT INTO `entrychat` (`id`, `hash`) VALUES (?, ?)", 1, Convector.serializeMap(Main.getInstance().time)).join();
        }
    }
}
