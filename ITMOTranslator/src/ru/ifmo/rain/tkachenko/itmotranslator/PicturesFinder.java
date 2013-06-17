package ru.ifmo.rain.tkachenko.itmotranslator;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class PicturesFinder {
	private Bitmap[] res;
	private final static String key = "AIzaSyBVi9IDX3p-gyCq0Sci75D60PahbO5nD5I";
	private final static String engine = "008630062675229555318:l859fi4df70";

	public PicturesFinder(String query) {
		try {
			res = new Bitmap[10];
			int countSuccesed = 0;
			int startIndex = 1;
			do {
				URL url = new URL(
						"https://www.googleapis.com/customsearch/v1?key="
								+ key
								+ "&cx="
								+ engine
								+ "&q="
								+ query
								+ "&searchType=image&alt=json&imgSize=medium&start="
								+ Integer.toString(startIndex) + "&num=10");
				URLConnection connection = url.openConnection();
				String line;
				StringBuilder builder = new StringBuilder();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(connection.getInputStream()));
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
				JSONObject json = new JSONObject(builder.toString());
				JSONArray resultArray = json.getJSONArray("items");
				for (int i = 0; i < resultArray.length()
						&& countSuccesed < res.length; ++i) {
					Thread.yield();
					res[countSuccesed] = BitmapFactory.decodeStream(new URL(
							resultArray.getJSONObject(i).getString("link"))
							.openStream());
					if (res[countSuccesed] != null) {
						countSuccesed++;
					}
				}
				startIndex += 10;
			} while (countSuccesed < res.length);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public Bitmap[] get() {
		return res;
	}
}
