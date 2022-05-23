package utils;

import org.aeonbits.owner.Config;

@Config.Sources({"classpath:./application.properties"})
public interface AppConfig extends Config {
    @Key("start.url")
    String startUrl();

    @Key("return.url")
    String returnUrl();

    @Key("userName")
    String getUserName();

    @Key("userPassword")
    String getUserPassword();

    @Key("cardNumberWithSMS")
    String getCardNumberWithSMS();

    @Key("cardYearWithSMS")
    String getCardYearWithSMS();

    @Key("cardMonthWithSMS")
    String getCardMonthWithSMS();

    @Key("cardNameWithSMS")
    String getCardNameWithSMS();

    @Key("cardCvcWithSMS")
    String getCardCvcWithSMS();

    @Key("smsCode")
    String getSmsCode();

    @Key("cardNumber")
    String getCardNumber();

    @Key("cardYear")
    String getCardYear();

    @Key("cardMonth")
    String getCardMonth();

    @Key("cardName")
    String getCardName();

    @Key("cardCvc")
    String getCardCvc();

    @Key("testEmail")
    String getTestEmail();

    @Key(value = "webdriver.browser.name")
    String webDriverBrowserName();

    @Key("maxCountOfPayTry")
    int getMaxCountOfPayTry();

    @Key("timeouts.page")
    int pageLoadTimeout();

    @Key("sleepTime")
    int getSleepTime();

    @Key("timeouts.element")
    int elementTimeout();
}