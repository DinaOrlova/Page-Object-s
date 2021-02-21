package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;

public class MoneyTransferPage {
    private SelenideElement heading = $(withText("Пополнение карты"));
    private SelenideElement amount = $("[data-test-id=amount] input");
    private SelenideElement fromCard = $("[data-test-id=from] input");
    private SelenideElement transferButton = $("[data-test-id=action-transfer]");

    public MoneyTransferPage() {
        heading.shouldBe(Condition.visible);
    }

    public void deleteAmount() {
        $("[data-test-id=amount] input").sendKeys(Keys.chord(Keys.CONTROL,"A"));
        $("[data-test-id=amount] input").sendKeys(Keys.chord(Keys.DELETE));
    }

    public void deleteFrom() {
        $("[data-test-id=from] input").sendKeys(Keys.chord(Keys.CONTROL,"A"));
        $("[data-test-id=from] input").sendKeys(Keys.chord(Keys.DELETE));
    }

    public DashboardPage MoneyTransfer(int transferAmount, DataHelper.Card card) {
        String str = Integer.toString(transferAmount);
        deleteAmount();
        amount.setValue(str);
        deleteFrom();
        fromCard.setValue(card.getNumber());
        transferButton.click();
        return new DashboardPage();
    }
}
