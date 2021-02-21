package ru.netology.web.page;

import com.codeborne.selenide.SelenideElement;
import lombok.val;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;

public class DashboardPage {
  private SelenideElement heading = $("[data-test-id=dashboard]");
  private SelenideElement secondHeading = $(withText("Ваши карты"));
  private SelenideElement firstCardInfo = $("[data-test-id='92df3f1c-a033-48e6-8390-206f6b1f56c0']");
  private SelenideElement secondCardInfo = $("[data-test-id='0f3f5c2a-249e-4c3d-8287-09f7a039391d']");
  private final String balanceStart = "баланс: ";
  private final String balanceFinish = " р.";
  private SelenideElement firstReplenishButton = $("[data-test-id='92df3f1c-a033-48e6-8390-206f6b1f56c0'] button");
  private SelenideElement secondReplenishButton = $("[data-test-id='0f3f5c2a-249e-4c3d-8287-09f7a039391d'] button");

  public DashboardPage() {
    heading.shouldBe(visible);
    secondHeading.shouldBe(visible);
  }

  public MoneyTransferPage selectCard(String cardNumber) {
    if(cardNumber == DataHelper.getFirstCard().getNumber()) {
      firstReplenishButton.click();
    }
    else {
      secondReplenishButton.click();
    }
    return new MoneyTransferPage();
  }

  private int extractBalance(String text) {
    val start = text.indexOf(balanceStart);
    val finish = text.indexOf(balanceFinish);
    val value = text.substring(start + balanceStart.length(), finish);
    return Integer.parseInt(value);
  }

  public int getCardBalance(String cardNumber) {
    String balance;
    if(cardNumber == DataHelper.getFirstCard().getNumber()) {
     balance = firstCardInfo.getText();
    }
    else {
      balance = secondCardInfo.getText();
    }
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
