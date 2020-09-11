package com.trustsim;

public class HomeMockView {

    private final TrustSimAbstractTest driver;

    public HomeMockView(TrustSimAbstractTest driver) {
        this.driver = driver;
    }

    public void clickScreenButton(String screenName) {
        driver.clickOn(screenName);
    }

}
