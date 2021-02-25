package ru.netology.web.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.val;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
  private SelenideElement heading = $("[data-test-id=dashboard]");
  private SelenideElement secondHeading = $(withText("Ваши карты"));
  private ElementsCollection cards = $$(".list__item");
  private final String balanceStart = "баланс: ";
  private final String balanceFinish = " р.";

  public DashboardPage() {
    heading.shouldBe(visible);
    secondHeading.shouldBe(visible);
  }

  public MoneyTransferPage selectCard(String cardNumber) {
    cards.find(text(cardNumber.substring(16, 19))).$("button").click();
    return new MoneyTransferPage();
  }

  private int extractBalance(String text) {
    val start = text.indexOf(balanceStart);
    val finish = text.indexOf(balanceFinish);
    val value = text.substring(start + balanceStart.length(), finish);
    return Integer.parseInt(value);
  }

  public int getCardBalance(String cardNumber) {
    String balance = cards.find(text(cardNumber.substring(16, 19))).getText();
    return extractBalance(balance);
  }

  public void returnToInitialCondition() {
    int firstCardBalance = getCardBalance(DataHelper.getFirstCard().getNumber());
    int secondCardBalance = getCardBalance(DataHelper.getSecondCard().getNumber());
    int difference;

    if(firstCardBalance == secondCardBalance) {
      return;
    }
    if(firstCardBalance > secondCardBalance) {
      difference = (firstCardBalance - secondCardBalance) / 2;
      val moneyTransferPage = selectCard(DataHelper.getSecondCard().getNumber());
      moneyTransferPage.MoneyTransfer(difference, DataHelper.getFirstCard());
      return;
    }
    if(firstCardBalance < secondCardBalance) {
      difference = (secondCardBalance - firstCardBalance) / 2;
      val moneyTransferPage = selectCard(DataHelper.getFirstCard().getNumber());
      moneyTransferPage.MoneyTransfer(difference, DataHelper.getSecondCard());
    }
  }
}
