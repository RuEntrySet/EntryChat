package ru.entryset.chat.message;

import ru.entryset.chat.EntryChat;
import ru.entryset.core.EntryCore;

import java.util.UUID;

public class Utils {

	public static String format(long time) {
		long days = time / 86400;
		long hours = (time % 86400) / 3600;
		long minutes = (time % 3600) / 60;
		long seconds = time % 60;

		StringBuilder sb = new StringBuilder();

		if (days != 0)
			sb.append(EntryChat.config.getSettings().getString("time.days").replace("%size%", "" + days)).append(" ");
		if (hours != 0)
			sb.append(EntryChat.config.getSettings().getString("time.hours").replace("%size%", "" + hours)).append(" ");
		if (minutes != 0)
			sb.append(EntryChat.config.getSettings().getString("time.minute").replace("%size%", "" + minutes)).append(" ");
		if (seconds != 0)
			sb.append(EntryChat.config.getSettings().getString("time.seconds").replace("%size%", "" + seconds)).append(" ");

		String str = sb.toString().trim();

		if (str.isEmpty())
			str = EntryChat.config.getSettings().getString("time.now");
		return str;
	}

	public static void msg(String msg, String context){
		EntryCore.carrotManager.push("EntryChat", "msg=" + msg + "=" + context);
	}

	public static void updateLastMessage(UUID uuid){
		EntryCore.carrotManager.push("EntryChat", "last=" + uuid.toString());
	}

	public static void update(UUID player, Long time){
		EntryCore.carrotManager.push("EntryChat", "update=" + player.toString() + "=" + time.toString());
	}

}
