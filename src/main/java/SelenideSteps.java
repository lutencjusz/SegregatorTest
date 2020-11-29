import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.Assert;
import org.openqa.selenium.Keys;
import utils.SelenideElementsFactory;

import java.util.Map;
import java.util.stream.IntStream;

import static com.codeborne.selenide.CollectionCondition.*;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class SelenideSteps {

    ElementsCollection langButtonsList = $$("div.doPrawej button.button_w");
    ElementsCollection helpButtonsImgList = $$("#root div div.row div span button img");
    ElementsCollection newChoiceButtonList = $$("div.rbt-menu.dropdown-menu.show a.dropdown-item");
    ElementsCollection candidateButtonsList = $$("button.button_w");
    ElementsCollection radioButtonsNameList = $$("span.MuiTypography-root.MuiFormControlLabel-label.MuiTypography-body1");
    ElementsCollection pszokPointsMediaList = $$("div.media.pszok");

    private SelenideElementsFactory selenideElementsFactory = new SelenideElementsFactory();

    private boolean findClickCategory(ElementsCollection ec, String category) {
        for (int i = 0; i < ec.size(); i++)
            if (ec.get(i).attr("alt").equalsIgnoreCase(category)) {
                ec.get(i).shouldBe(visible).click();
                return true;
            }
        return false;
    }

    private void checkConfirmationAndMessage(boolean isVisible) {
        boolean isFound = false;
        for (SelenideElement se : newChoiceButtonList) {
            if (se.is(text("Nowy wybór:"))) {
                isFound = true;
                if (isVisible) {
                    se.click();
                }
            }
        }
        if (!isFound) {
            newChoiceButtonList.get(0).click();
        }

        if (isVisible) {
            SelenideElement se = selenideElementsFactory.createElement("toastSuccessMessage");
            se.waitUntil(visible, 10000)
                    .shouldHave(text("Nie znalazłem"));
            se.waitUntil(not(visible), 10000);
        } else {
            selenideElementsFactory.createElement("toastSuccessMessage")
                    .shouldNotBe(visible);
        }
    }

    @Before
    public void setUp() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide().screenshots(true).savePageSource(false));
    }


    @Step("Otwieranie strony startowej")
    @Given("go to Home Page")
    public void goToHomePage() {
        Allure.suite("Strona domowa");
        SelenideElement se = selenideElementsFactory.createElement("HomePage");
        try {
            if (se.isDisplayed()) se.click();
        } catch (Exception e) {
            open("https://master.d3f6907mja6s04.amplifyapp.com/");
        }
    }


    @Step("Zmiana języka serwisu")
    @When("^change language (.*)$")
    public void changeLanguageLang(String lang) {
        IntStream.range(0, langButtonsList.size())
                .filter(i -> langButtonsList.get(i).is(text(lang)))
                .forEach(i -> langButtonsList.get(i).shouldBe(visible).click());
    }

    @Step("Jezyk serwisu został zmieniony")
    @Then("^language is changed for (.*) is (.*)$")
    public void isLanguageChangedForLang(String lang, Boolean isVisible) {
        SelenideElement se = selenideElementsFactory.createElement("HomePage");
        if (isVisible) {
            switch (lang) {
                case "en":
                    se.shouldHave(text("Waste classification"));
                    break;
                case "de":
                    se.shouldHave(text("Abfallklassifizierung"));
                    break;
                case "pl":
                    se.shouldHave(text("Klasyfikacja odpadków"));
                    break;
                case "fr":
                    se.shouldHave(text("Classification des déchets"));
                    break;
                case "ch":
                    se.shouldHave(text("废物分类"));
                    break;
            }
        } else {
            switch (lang) {
                case "en":
                    se.shouldNotHave(text("Waste classification"));
                    break;
                case "de":
                    se.shouldNotHave(text("Abfallklassifizierung"));
                    break;
                case "pl":
                    se.shouldNotHave(text("Klasyfikacja odpadków"));
                    break;
                case "fr":
                    se.shouldNotHave(text("Classification des déchets"));
                    break;
                case "ch":
                    se.shouldNotHave(text("废物分类"));
                    break;
            }
        }
    }

    @Step("Przejście do opisu kategorii {0}")
    @When("^go to link and click category (.*)$")
    public void goToLinkAndClickCategory(String category) {
        helpButtonsImgList.shouldHave(sizeGreaterThanOrEqual(8), 10000);
        findClickCategory(helpButtonsImgList, category);
    }

    @Step("Zmiana opisu kategorii {0}")
    @When("^change garbage category description (.*)")
    public void changeGarbageCategoryDescription(String category) {
        helpButtonsImgList.shouldHave(sizeGreaterThanOrEqual(8), 10000);
        findClickCategory(helpButtonsImgList, category);
    }

    @Step("Opis kategorii {0} jest widoczny")
    @Then("^garbage category description (.*) is (.*)$")
    public void isGarbageCategoryDescription(String category, boolean isVisible) {
        SelenideElement se = selenideElementsFactory.createElement("descriptionWaste");
        se.waitUntil(visible, 10000);
        if (isVisible) {
            se.shouldHave(text(category));
        } else {
            se.shouldNotHave(text(category));
        }
    }

    @Step("Strona {0} jest widoczna")
    @Then("^page (.*) visible is (.*)$")
    public void linkedPageVisible(String page, boolean isVisible) {
        SelenideElement se = selenideElementsFactory.createElement(page);
        if (se != null) {
            if (isVisible) {
                se.waitUntil(visible, 10000);
            } else {
                se.waitUntil(not(visible), 10000);
            }
        }

    }

    @Step("Przejście do strony {0}")
    @When("^go to linked page and click (.*)$")
    public void goToLinkAndClick(String page) {
        Allure.suite(page);
        selenideElementsFactory.createElement(page)
                .waitUntil(visible, 10000)
                .click();
    }

    @Step("Szukanie wyrażenia {0}")
    @When("^search (.*)$")
    public void searchInputData(String searchData) {
        SelenideElement se = selenideElementsFactory.createElement("SearchInput");
        se.waitUntil(visible, 10000).sendKeys(Keys.chord(Keys.LEFT_CONTROL, Keys.BACK_SPACE)); //czyści przedpole
        se.waitUntil(visible, 10000).sendKeys(searchData);
        se.pressEnter();
    }

    @Step("Potwierdzenie pojawienia się komunikatu Nowy wybór")
    @Then("confirmation message is appear")
    public void isAppearConfirmationMessage() {
        checkConfirmationAndMessage(true);

    }

    @Step("Szukane wyrażene już istnieje")
    @Then("is search data existing")
    public void isSearchDataExisting() {
        checkConfirmationAndMessage(false);
    }

    @Step("Otworzenie formularza dla kandydata {0}")
    @When("^open form for candidate (.*)$")
    public void openFormForCandidate(String canidate) {
        boolean isFound = false;
        for (SelenideElement se : candidateButtonsList) {
            if (!isFound && canidate.equals(se.getText())) {
                isFound = true;
                se.waitUntil(visible, 10000).click();
            }
        }
    }

    @Step("Czy formularz z kandydatem {0} jest otwarty? {1}")
    @Then("^form opened for candidate (.*) is (.*)$")
    public void isFormOpenedForCandidateUszka(String candidate, boolean isVisible) {
        boolean isFound = false;
        SelenideElement se = selenideElementsFactory.createElement("nameInput");
        for (SelenideElement i : candidateButtonsList) {
            if (!isFound && candidate.equals(i.getText())) {
                isFound = true;
                if (isVisible) {
                    se.shouldHave(attribute("value", candidate));
                } else {
                    se.shouldNotHave(attribute("value", candidate));
                }

            }
        }
    }

    @Step("Wypełnianie formularza dla kandydata kategrią {0}")
    @When("^fill form of the candidate with category (.*)$")
    public void fillFormOfTheCandidateWithCategory(String category) {
        selenideElementsFactory.createElement("nameInput")
                .sendKeys("Test");
        for (SelenideElement i : radioButtonsNameList) {
            if (category.equals(i.getText())) {
                i.waitUntil(visible, 10000).click();
            }
        }
        selenideElementsFactory.createElement("komentarzInput")
                .sendKeys("Lorem ipsum...");
    }
    @Step("Wciśnięto przycisk {0}")
    @And("^(.*) this one candidate")
    public void actionOnThisOneCandidate(String actionButton) {
        selenideElementsFactory.createElement(actionButton).waitUntil(visible, 10000).click();
    }

    @Step("Potwierdzenie dodania kandydata")
    @Then("is candidate added")
    public void isCandidateAdded() {
        SelenideElement se = selenideElementsFactory.createElement("toastSuccessMessage");
        se.waitUntil(visible, 10000)
                .shouldHave(text("Dodano"));
        se.waitUntil(not(visible), 10000);
    }

    @Step("Wypełnienie formularza następującycmi danymi:")
    @When("fill form data:")
    public void fillFormData(DataTable dataTable) {
        Map<String, String> data = dataTable.asMap(String.class, String.class);
        selenideElementsFactory.createElement("nameInput")
                .waitUntil(visible, 10000)
                .sendKeys(data.get("name"));
        for (SelenideElement i : radioButtonsNameList) {
            if (data.get("category").equals(i.getText())) {
                i.waitUntil(visible, 10000).click();
            }
        }
        selenideElementsFactory.createElement("komentarzInput")
                .sendKeys(data.get("description"));
    }
    @Step("Wprowadzenie miasta {0}")
    @When("^city search (.*)$")
    public void citySearch(String citi) {
        selenideElementsFactory.createElement("CitiFinder")
                .waitUntil(visible, 10000)
                .sendKeys(citi);
    }

    @Step("Wyświetlanie punktów tylko dla miasta {0}")
    @Then("only points of city (.*) is visible$")
    public void onlyPointsOfCityIsVisible(String citi) {
        for (SelenideElement se : pszokPointsMediaList) {
            se.shouldHave(text(citi));
        }
    }
}
