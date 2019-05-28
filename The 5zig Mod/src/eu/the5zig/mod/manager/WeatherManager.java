package eu.the5zig.mod.manager;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.listener.Listener;
import org.apache.commons.io.IOUtils;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class WeatherManager extends Listener {

	private static final String URL_PATH = "https://weather.yahoo.com";

	private boolean previousWeatherEnabled;
	private long lastChecked;
	private WeatherData weatherData;

	public WeatherManager() {
	}

	@Override
	public void onTick() {
		if (previousWeatherEnabled != The5zigMod.getConfig().getBool("renderWeather")) {
			previousWeatherEnabled = !previousWeatherEnabled;
			if (previousWeatherEnabled) {
				lastChecked = 0;
			}
		}
		if (System.currentTimeMillis() - lastChecked > 0 && previousWeatherEnabled) {
			checkWeather();
		}
	}

	private void checkWeather() {
		lastChecked = System.currentTimeMillis() + 1000 * 60 * 30;
		new Thread("Weather-Data") {
			@Override
			public void run() {
				getWeather();
			}
		}.start();
	}

	private void getWeather() {
		The5zigMod.logger.info("Checking for weather...");
		URL url;
		HttpsURLConnection connection = null;
		InputStream inputStream = null;
		BufferedReader reader = null;
		String line;
		try {
			url = new URL(URL_PATH);
			connection = (HttpsURLConnection) url.openConnection();
			int responseCode = connection.getResponseCode();
			if (responseCode != 200) {
				throw new IOException("Illegal response code received.");
			}
			inputStream = connection.getInputStream();
			reader = new BufferedReader(new InputStreamReader(inputStream));

			weatherData = new WeatherData();
			while ((line = reader.readLine()) != null) {
				if (line.contains("<span class=\"name\">")) {
					weatherData.setCity(line.split("<span class=\"name\">|</span>")[1]);
				} else if (line.contains("<div class=\"region\">")) {
					weatherData.setCountry(line.split("<div class=\"region\">|</div>")[1]);
				} else if (line.contains("<div class=\"cond")) {
					weatherData.setCondition(line.split("\">|</div>")[1]);
				} else if (line.contains("<span class=\"f\"><span class=\"num\">")) {
					weatherData.setFahrenheit(Integer.parseInt(line.split("<span class=\"num\">|</span>")[1]));
				} else if (line.contains("<span class=\"c\"><span class=\"num\">")) {
					weatherData.setCelsius(Integer.parseInt(line.split("<span class=\"num\">|</span>")[1]));
				}
			}
			reader.close();
			inputStream.close();
			The5zigMod.logger.info("Got new weather data!");
		} catch (Exception e) {
			The5zigMod.logger.error("Could not fetch weather!", e);
			lastChecked = System.currentTimeMillis() + 1000 * 60;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
			IOUtils.closeQuietly(reader);
			IOUtils.closeQuietly(inputStream);
		}
	}

	public WeatherData getWeatherData() {
		return weatherData;
	}
}
