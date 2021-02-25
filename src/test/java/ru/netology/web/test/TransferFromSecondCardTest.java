package ru.netology.web.test;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransferFromSecondCardTest {
    DashboardPage dashboardPage;
    DataHelper.Card secondCard = DataHelper.getSecondCard();
    String firstCardNumber = DataHelper.getFirstCard().getNumber();
    String secondCardNumber = DataHelper.getSecondCard().getNumber();

    @BeforeEach
    void setup() {
        open("http://localhost:9999/");
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        dashboardPage = verificationPage.validVerify(verificationCode);
        dashboardPage.returnToInitialCondition();
    }

    @Test
    void shouldTransfer0Rub() {
        val moneyTransferPage = dashboardPage.selectCard(firstCardNumber);
        moneyTransferPage.MoneyTransfer(0, secondCard);
        assertEquals(10000, dashboardPage.getCardBalance(firstCardNumber));
        assertEquals(10000, dashboardPage.getCardBalance(secondCardNumber));
    }

    @Test
    void shouldTransfer1Rub() {
        val moneyTransferPage = dashboardPage.selectCard(firstCardNumber);
        moneyTransferPage.MoneyTransfer(1, secondCard);
        assertEquals(10001, dashboardPage.getCardBalance(firstCardNumber));
        assertEquals(9999, dashboardPage.getCardBalance(secondCardNumber));
    }

    @Test
    void shouldTransfer2Rub() {
        val moneyTransferPage = dashboardPage.selectCard(firstCardNumber);
        moneyTransferPage.MoneyTransfer(2, secondCard);
        assertEquals(10002, dashboardPage.getCardBalance(firstCardNumber));
        assertEquals(9998, dashboardPage.getCardBalance(secondCardNumber));
    }

    @Test
    void shouldTransfer5000Rub() {
        val moneyTransferPage = dashboardPage.selectCard(firstCardNumber);
        moneyTransferPage.MoneyTransfer(5000, secondCard);
        assertEquals(15000, dashboardPage.getCardBalance(firstCardNumber));
        assertEquals(5000, dashboardPage.getCardBalance(secondCardNumber));
    }

    @Test
    void shouldTransfer9998Rub() {
        val moneyTransferPage = dashboardPage.selectCard(firstCardNumber);
        moneyTransferPage.MoneyTransfer(9998, secondCard);
        assertEquals(19998, dashboardPage.getCardBalance(firstCardNumber));
        assertEquals(2, dashboardPage.getCardBalance(secondCardNumber));
    }

    @Test
    void shouldTransfer9999Rub() {
        val moneyTransferPage = dashboardPage.selectCard(firstCardNumber);
        moneyTransferPage.MoneyTransfer(9999, secondCard);
        assertEquals(19999, dashboardPage.getCardBalance(firstCardNumber));
        assertEquals(1, dashboardPage.getCardBalance(secondCardNumber));
    }

    @Test
    void shouldTransfer10000Rub() {
        val moneyTransferPage = dashboardPage.selectCard(firstCardNumber);
        moneyTransferPage.MoneyTransfer(10000, secondCard);
        assertEquals(20000, dashboardPage.getCardBalance(firstCardNumber));
        assertEquals(0, dashboardPage.getCardBalance(secondCardNumber));
    }

    @Test
    void shouldNotTransfer20000Rub() {
        val moneyTransferPage = dashboardPage.selectCard(firstCardNumber);
        moneyTransferPage.shouldGiveErrorWhenAmountExceedsBalance(20000, secondCard);
    }

    @Test
    void shouldNotTransferWhenAmountIsEmpty() {
        val moneyTransferPage = dashboardPage.selectCard(secondCardNumber);
        moneyTransferPage.shouldGiveErrorWhenAmountIsEmpty(secondCard);
    }

    @Test
    void shouldNotTransferWhenFromCardIsEmpty() {
        val moneyTransferPage = dashboardPage.selectCard(secondCardNumber);
        moneyTransferPage.shouldGiveErrorWhenFromCardIsEmpty(4500);
    }
}
