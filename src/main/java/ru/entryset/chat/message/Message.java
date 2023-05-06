package ru.entryset.chat.message;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.entryset.api.bukkit.manager.Messager;
import ru.entryset.chat.main.Main;

public class Message {

    private MessageType type;

    private String message;

    private Player sender;

    public Message(Player sender, String input_message){
        if(input_message.startsWith("!")){
            setType(MessageType.GLOBAL);
            setMessage(input_message.substring(1));
        } else {
            setType(MessageType.LOCAL);
            setMessage(input_message);
        }
        setSender(sender);
    }

    public void send() {
        if(getSender().hasPermission("entrychat.color")){
            setMessage(Messager.color(getMessage()));
        }
        if(hasCaps(getMessage())){
            Main.messager.sendMessage(getSender(), Main.config.getMessage("caps"));
            return;
        }
        if(hasWords(getMessage())){
            Main.messager.sendMessage(getSender(), Main.config.getMessage("block_words"));
            return;
        }
        String content = Messager.color(Messager.parseApi(getType().getFormat(), getSender())).replace("<msg>", getMessage());
        Utils.updateLastMessage(getSender().getUniqueId());
        if (getType() == MessageType.GLOBAL) {
            sendGlobal(content);
            return;
        }
        sendLocal(content);
    }

    private void sendGlobal(String content){
        if(Main.config.getBoolean("settings.cross")){
            Utils.msg(content, Main.config.getSettings().getString("context"));
            return;
        }
        for(Player player : Bukkit.getOnlinePlayers()){
            player.sendMessage(content);
        }
    }

    private void sendLocal(String content){
        for(Player player : Bukkit.getOnlinePlayers()){
            if(getSender().getWorld().equals(player.getWorld())){
                if(getSender().getLocation().distance(player.getLocation()) <= Main.config.getSettings().getInt("local_radius")){
                    player.sendMessage(content);
                }
            }
        }
    }

    private boolean hasWords(String message){
        if(getSender().hasPermission(Main.config.getPermission("admin"))){
            return false;
        }
        boolean result = false;
        for(String section : Main.config.getSettings().getStringList("block_words")){
            if(message.toLowerCase().contains(section.toLowerCase())){
                result = true;
                break;
            }
        }
        return result;
    }

    private boolean hasCaps(String message){
        if(getSender().isOp() || getSender().hasPermission(Main.config.getPermission("admin"))){
            return false;
        }
        char[] chars = message.toCharArray();
        if(chars.length <= 5){
            return false;
        }
        int l = 0;
        int a = 0;
        for(char ch : chars){
            if(Character.isUpperCase(ch)){
                a = a + 1;
            }
            if(Character.isLowerCase(ch)){
                l = l + 1;
            }
        }
        if(a == 0){
            return false;
        }

        int size = l + a;
        double percent = (a*100)/size;
        return !(percent <= 40);
    }

    private void setType(MessageType type) {
        this.type = type;
    }

    private void setSender(Player sender) {
        this.sender = sender;
    }

    private void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public MessageType getType() {
        return type;
    }

    public Player getSender() {
        return sender;
    }
}
