package eu.the5zig.mod.manager;

public class WeatherData {

	private String city;
	private String country;
	private String condition;
	private int celsius;
	private int fahrenheit;

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public int getCelsius() {
		return celsius;
	}

	public void setCelsius(int celsius) {
		this.celsius = celsius;
	}

	public int getFahrenheit() {
		return fahrenheit;
	}

	public void setFahrenheit(int fahrenheit) {
		this.fahrenheit = fahrenheit;
	}

	@Override
	public String toString() {
		return "WeatherData{" +
				"city='" + city + '\'' +
				", country='" + country + '\'' +
				", condition='" + condition + '\'' +
				", celsius=" + celsius +
				", fahrenheit=" + fahrenheit +
				'}';
	}
}
