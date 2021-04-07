/*
 *
 *  @author Sukhetskyi Nazarii S20363
 *
 */

package zad1;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Currency;
import java.util.Locale;

public class Service {
    private final Locale locale;

    public Service(String country) {
        country = country.replaceAll("\\s", "");
        this.locale = getLocaleFromNameOfCountry(country);
    }

    private Locale getLocaleFromNameOfCountry(String country) {
        for (Locale locale : Locale.getAvailableLocales())
            if (country.equals(locale.getDisplayCountry()))
                return locale;
        return null;
    }

    public String getWeather(String city) {
        String weather = null;
        city = city.replaceAll("\\s", "");
        try {
            URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q="+city+","+locale.getCountry()+"&appid=OWN KEY&units=metric");
            URLConnection request = url.openConnection();
            request.connect();

            JsonParser jsonParser = new JsonParser();
            JsonElement jsonElement = jsonParser.parse(new InputStreamReader(request.getInputStream()));
            weather = jsonElement.toString();
           } catch (Exception ignored) {}
        System.out.println(weather);
        return weather;
    }

    public Double getRateFor(String currency) {
        Double rate = null;
        try {
            URL base = new URL("http://data.fixer.io/api/latest?access_key=OWN KEY&format=1");
            URLConnection request = base.openConnection();
            request.connect();

            JsonParser jsonParser = new JsonParser();
            JsonElement jsonElement = jsonParser.parse(new InputStreamReader(request.getInputStream()));
            Double selectedCurrency = jsonElement.getAsJsonObject().get("rates").getAsJsonObject().get(currency).getAsDouble();
            Double countryCurrency = jsonElement.getAsJsonObject().get("rates").getAsJsonObject().get(String.valueOf(Currency.getInstance(locale))).getAsDouble();
            rate = selectedCurrency/countryCurrency;
        } catch (Exception ignored) {}
        System.out.println(rate);
        return rate;
    }

    public Double getNBPRate() {
        Double rate = null;
        try {
            URL base = new URL("http://data.fixer.io/api/latest?access_key=OWN KEY&format=1");
            URLConnection request = base.openConnection();
            request.connect();

            JsonParser jsonParser = new JsonParser();
            JsonElement jsonElement = jsonParser.parse(new InputStreamReader(request.getInputStream()));
            Double PLN = jsonElement.getAsJsonObject().get("rates").getAsJsonObject().get("PLN").getAsDouble();
            Double countryCurrency = jsonElement.getAsJsonObject().get("rates").getAsJsonObject().get(String.valueOf(Currency.getInstance(locale))).getAsDouble();
            rate = PLN/countryCurrency;
        } catch (Exception ignored) {}
        System.out.println(rate);
        return rate;
    }
}
