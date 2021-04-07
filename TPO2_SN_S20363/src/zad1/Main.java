/**
 *
 *  @author Sukhetskyi Nazarii S20363
 *
 */

package zad1;

import javafx.application.Application;

public class Main {
  public static void main(String[] args) {
    Service s = new Service("Poland");
    String weatherJson = s.getWeather("Warsaw");
    Double rate1 = s.getRateFor("USD");
    Double rate2 = s.getNBPRate();
    // ...
    // część uruchamiająca GUI
    Application.launch(WeatherForecastApp.class);
  }
}
