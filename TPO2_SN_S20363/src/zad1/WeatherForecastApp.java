package zad1;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.text.DecimalFormat;


public class WeatherForecastApp extends Application {
    private String[] location;
    private String currency;
    private Service service;
    private final JsonParser jsonParser = new JsonParser();

    @Override
    public void start(Stage stage) {
        stage.setTitle("Application");
        stage.setMinWidth(960); stage.setMinHeight(540);

        stage.getIcons().add(new Image("https://i.pinimg.com/originals/77/0b/80/770b805d5c99c7931366c2e84e88f251.png"));

        Browser browser = new Browser();

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(browser);

        BorderPane toolBar = new BorderPane();
        toolBar.setStyle("-fx-background-color: white");

        HBox hBoxLeft = new HBox();
        hBoxLeft.setSpacing(10);
        hBoxLeft.setPadding(new Insets(0,10,0,10));

        HBox hBoxRight = new HBox();
        hBoxRight.setSpacing(10);
        hBoxRight.setPadding(new Insets(0,10,0,10));

        Label labelPLN = new Label("PLN: null");
        labelPLN.setFont(new Font(20));
        labelPLN.setPadding(new Insets(0,5,0,5));

        Label currencyRate = new Label("Currency rate: null");
        currencyRate.setFont(new Font(20));
        currencyRate.setPadding(new Insets(0,5,0,5));

        Label temperature = new Label("\u2103");
        temperature.setFont(new Font(20));
        temperature.setPadding(new Insets(0,5,0,5));

        TextField weatherField = new TextField("Poland, Warsaw");
        weatherField.setPrefWidth(150);
        Label weatherLabel = new Label("Location:");
        weatherLabel.setFont(new Font(20));
        weatherField.setOnAction(event -> {
            location = weatherField.getText().split(",");
            browser.loadPage(location[1]);
            service = new Service(location[0]);
            JsonElement jsonElement = jsonParser.parse(service.getWeather(location[1]));
            String conditions = jsonElement.getAsJsonObject().get("weather").getAsJsonArray().get(0).getAsJsonObject().get("description").getAsString();
            DecimalFormat decimalFormat = new DecimalFormat("###");
            temperature.setText(decimalFormat.format(jsonElement.getAsJsonObject().get("main").getAsJsonObject().get("temp").getAsDouble()) + "\u2103 " + conditions);
            decimalFormat = new DecimalFormat("###.###");
            labelPLN.setText("PLN: " + decimalFormat.format(service.getNBPRate()));
        });

        TextField currencyField = new TextField("USD");
        currencyField.setPrefWidth(50);
        Label currencyLabel = new Label("Currency:");
        currencyLabel.setFont(new Font(20));
        currencyField.setOnAction(event -> {
            DecimalFormat decimalFormat = new DecimalFormat("###.###");
            currency = currencyField.getText();
            currencyRate.setText("Currency rate: " + decimalFormat.format(service.getRateFor(currency)));
        });

        hBoxLeft.getChildren().addAll(weatherLabel,weatherField,currencyLabel,currencyField);
        hBoxRight.getChildren().addAll(labelPLN, currencyRate);

        toolBar.setLeft(hBoxLeft);
        toolBar.setCenter(temperature);
        toolBar.setRight(hBoxRight);

        borderPane.setTop(toolBar);

        Scene scene = new Scene(borderPane,1280,720);
        stage.setScene(scene);
        stage.show();
    }
}
