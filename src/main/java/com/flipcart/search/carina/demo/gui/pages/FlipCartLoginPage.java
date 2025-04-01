package com.flipcart.search.carina.demo.gui.pages;

import com.zebrunner.carina.utils.config.Configuration;
import com.zebrunner.carina.webdriver.config.WebDriverConfiguration;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class FlipCartLoginPage extends AbstractPage {

    @FindBy(xpath = "//*[text()='✕']")
    public ExtendedWebElement loginClose;

    @FindBy(xpath = "//input[@name='q']")
    public ExtendedWebElement searchBox;

    @FindBy(xpath = "(//select)[1]")
    public ExtendedWebElement minPriceElement; // min 600 and max 100

    @FindBy(xpath = "(//select)[2]")
    public ExtendedWebElement maxPriceElement;

    @FindBy(xpath = "//div[text()='4★ & above']/preceding-sibling::div[1]")
    public ExtendedWebElement ratingElement; // 4★ & above, boAt

    @FindBy(xpath = "//div[text()='boAt']/preceding-sibling::div[1]")
    public ExtendedWebElement brandElement;

    @FindBy(xpath = "//div[text()='In the Ear']/preceding-sibling::div[1]")
    public ExtendedWebElement headphoneTypeElement;

    public FlipCartLoginPage(WebDriver driver) {
        super(driver);
        setUiLoadedMarker(loginClose);
    }

    @Override
    public void open() {
        super.open();
    }

    public void enterSearchText(String text){
        loginClose.clickIfPresent();
        searchBox.format("q").type(text+ Keys.ENTER);
    }

    public void selectMinPrice(String price){
        this.waitUntil(ExpectedConditions.elementToBeClickable(minPriceElement.getElement()),EXPLICIT_TIMEOUT);
        minPriceElement.select(price);
    }

    public void selectMaxPrice(String price){
        this.waitUntil(ExpectedConditions.elementToBeClickable(maxPriceElement.getElement()),EXPLICIT_TIMEOUT);
        maxPriceElement.select(price);
    }

    public void selectRating(String rating){
        this.waitUntil(ExpectedConditions.elementToBeClickable(ratingElement.getElement()),EXPLICIT_TIMEOUT);
        ratingElement.format(rating).check();
    }

    public void selectBrand(String brand){
        this.waitUntil(ExpectedConditions.visibilityOf(brandElement.getElement()),EXPLICIT_TIMEOUT);
        brandElement.format(brand).clickByJs(5);
    }

    public void selectHeadPhoneType(String headphoneType){
        headphoneTypeElement.format(headphoneType).clickByJs(5);
    }

}
