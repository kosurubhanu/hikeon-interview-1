package com.flipcart.search.carina.demo.gui.pages;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ProductPage extends AbstractPage {

    @FindBy(xpath = "//h1/span")
    public ExtendedWebElement productTitle;

    @FindBy(xpath = "//*[text()='Special price']/../following-sibling::div[1]/div/div/div[1]")
    public ExtendedWebElement productPrice;

    @FindBy(xpath = "(//span[contains(@id,'productRating')]/div)[1]")
    public ExtendedWebElement productRating;

    @FindBy(xpath = "//span[contains(@id,'productRating')]/following-sibling::span[1]/span/span[3]")
    public ExtendedWebElement productReviews;

    public ProductPage(WebDriver driver) {
        super(driver);
        setUiLoadedMarker(productTitle);
    }



}
