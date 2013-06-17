package ru.ifmo.rain.tkachenko.itmotranslator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class WordTranslate {
	private String answer;
	private final static String lang = "en-ru";

	public WordTranslate(String text) {
		answer = null;
		try {
			URL url = new URL(
					"http://translate.yandex.net/api/v1/tr.json/translate?lang="
							+ lang + "&text=" + text);
			URLConnection connection = url.openConnection();
			String str;
			StringBuilder builder = new StringBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			while ((str = reader.readLine()) != null) {
				builder.append(str);
			}
			JSONObject json = new JSONObject(builder.toString());
			JSONArray translateArray = json.getJSONArray("text");
			answer = translateArray.getString(0);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public String getAnswer() {
		return answer;
	}
}
