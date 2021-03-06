package net.blay09.mods.chattweaks.chat.emotes.twitch;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.blay09.mods.chattweaks.balyware.CachedAPI;
import net.blay09.mods.chattweaks.chat.emotes.IEmote;
import net.minecraft.util.IntHashMap;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;


public class TwitchEmotesAPI {

	public static final String CLIENT_ID = "10napoemcms7qf9j0dxf28ndl2ltc3";

	public static final int EMOTESET_GLOBAL = 0;
	public static final int EMOTESET_TURBO = 19194;

	private static final IntHashMap<String> emoteSets = new IntHashMap<>();
	private static final Map<String, IEmote<?>> twitchEmotes = new HashMap<>();

	public static void loadEmoteSets() throws Exception {
		JsonObject sets = CachedAPI.loadCachedAPI("https://twitchemotes.com/api_cache/v3/sets.json", "twitch_emotesets_v3.json", null);
		if (sets != null) {
			for (Map.Entry<String, JsonElement> entry : sets.entrySet()) {
				emoteSets.addKey(Integer.parseInt(entry.getKey()), entry.getValue().getAsJsonObject().get("channel_name").getAsString());
			}
		}
	}

	@Nullable
	public static String getChannelForEmoteSet(int emoteSet) {
		return emoteSets.lookup(emoteSet);
	}

	@Nullable
	public static JsonObject loadEmotes(int... emotesets) {
		StringBuilder sb = new StringBuilder();
		for (int emoteset : emotesets) {
			if (sb.length() > 0) {
				sb.append(",");
			}
			sb.append(emoteset);
		}
		String url = "https://api.twitch.tv/kraken/chat/emoticon_images?api_version=5&client_id=" + CLIENT_ID;
		if (emotesets.length > 0) {
			url += "&emotesets=" + sb.toString();
		}
		return CachedAPI.loadCachedAPI(url, "twitch_emotes" + (sb.length() > 0 ? "-" + sb.toString() : "") + ".json", "application/vnd.twitchtv.v5+json");
	}

	public static void registerTwitchEmote(String id, IEmote<?> emote) {
		twitchEmotes.put(id, emote);
	}

	@Nullable
	public static IEmote<?> getEmoteById(String id) {
		return twitchEmotes.get(id);
	}

}
