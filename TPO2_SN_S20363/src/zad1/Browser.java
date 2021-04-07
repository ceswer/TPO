package zad1;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class Browser extends Region {
    private final WebView browser = new WebView();
    private final WebEngine webEngine = browser.getEngine();

    public Browser() {
        webEngine.load("https://en.wikipedia.org/wiki/Main_Page");
        getChildren().add(browser);
    }

    public void loadPage(String city) {
        webEngine.load("https://en.wikipedia.org/wiki/"+city);
    }

    @Override
    protected void layoutChildren() {
        double width = getWidth(), height = getHeight();
        layoutInArea(browser,0,0,width,height,0, HPos.CENTER, VPos.CENTER);
    }
}
