package gt.app.e2e.pageobj;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public abstract class BasePage<T> {

    public SelenideElement body() {
        return $("body");
    }

    public SelenideElement html() {
        return $("html");
    }

    public void toggleTheme() {
        $("#theme-toggle").click();
    }

    public abstract T open();

}
