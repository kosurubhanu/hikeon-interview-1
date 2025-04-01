package com.flipcart.search.carina.demo.cucumber.steps;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipcart.search.carina.demo.gui.pages.FlipCartLoginPage;
import com.flipcart.search.carina.demo.gui.pages.ProductPage;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.CSVWriterBuilder;
import com.opencsv.bean.*;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.zebrunner.carina.cucumber.CucumberRunner;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import java.io.*;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class FlipCartSearchSteps extends CucumberRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    FlipCartLoginPage flipCartLoginPage = null;
    ProductPage productPage = null;
    HeaderColumnNameMappingStrategy<CsvRow> strategy = new HeaderColumnNameMappingStrategy<>();

    private List<ExtendedWebElement> products = null;
    private ArrayList<HashMap<String, String>> productsInfo;

    @Given("user login flipcart app")
    public boolean user_login_flipcart_app() {
        flipCartLoginPage = new FlipCartLoginPage(getDriver());
        flipCartLoginPage.open();
        LOGGER.info("Flipcart app opened");
        return flipCartLoginPage.isPageOpened();
    }
    @When("user closes login popup")
    public void user_closes_login_popup() {
        flipCartLoginPage.loginClose.clickIfPresent(10);
        LOGGER.info("Login popup is closed");
    }
    @When("user search for item {string}")
    public void user_search_for_wireless_headphones(String item) {
        flipCartLoginPage.enterSearchText(item);
        LOGGER.info("Search for item : " + item);
    }
    @When("user selects price range {string} and {string}")
    public void user_selects_price_range(String minPrice, String maxPrice) {
        flipCartLoginPage.selectMinPrice(minPrice);
        flipCartLoginPage.selectMaxPrice(maxPrice);
        LOGGER.info("Select price range : " + minPrice + " " + maxPrice );
    }
    @When("user selects rating {string}")
    public void user_selects_plus_rating(String rating) {
        flipCartLoginPage.selectRating(rating);
        LOGGER.info("Select rating : " + rating);
    }
    @When("user selects headphone type {string}")
    public void user_selects_headphone_type(String headphoneType) {
        flipCartLoginPage.selectHeadPhoneType(headphoneType);
        LOGGER.info("Select headphone type : " + headphoneType);
    }
    @When("user selects brand {string}")
    public void user_selects_brand(String brand) {
        flipCartLoginPage.selectBrand(brand);
        LOGGER.info("Select brand : " + brand);
    }
    @Then("user is displayed with search resluts")
    public void user_is_displayed_with_search_resluts() {
        flipCartLoginPage.waitUntil(ExpectedConditions.numberOfElementsToBe(By.xpath("//a[contains(@title,'boAt')]/ancestor::div[1]"),10),EXPLICIT_TIMEOUT);
        List<ExtendedWebElement> products = flipCartLoginPage.findExtendedWebElements(By.xpath("//a[contains(@title,'boAt')]/ancestor::div[1]"));
        this.products=products;
        LOGGER.info("Search results displayed successfully.");
//        Assert.assertEquals(products.size(),10);
    }

    @When("user collects products info")
    public void user_collects_products_info() {

        HashMap<String, String> product;
        ArrayList<HashMap<String, String>> products = new ArrayList<>();
        try {
            for(ExtendedWebElement productElement : this.products){
                product= new HashMap<>();
                flipCartLoginPage.waitUntil(ExpectedConditions.visibilityOf(productElement.findExtendedWebElement(By.cssSelector("a[title^='boAt']")).getElement()),EXPLICIT_TIMEOUT);
                ExtendedWebElement productLink = productElement.findExtendedWebElement(By.cssSelector("a[title^='boAt']"));
                product.put("name", productLink.getAttribute("title"));
                product.put("price", productLink
                        .findExtendedWebElement(By.xpath("./following-sibling::a[1]/div/div[1]")).getText());
                product.put("rating", productLink
                        .findExtendedWebElement(By.xpath("./following-sibling::div[2]/span/div|./following-sibling::div[1]/span/div")).getText());
                productLink
                        .findExtendedWebElement(By.xpath("./following-sibling::div[2]/span/div|./following-sibling::div[1]/span/div")).hover();
                productLink.hover();
                productLink
                        .findExtendedWebElement(By.xpath("./following-sibling::div[2]/span/div|./following-sibling::div[1]/span/div")).hover();
                String reviewsText = productLink
                        .findExtendedWebElement(By.xpath("./following-sibling::div[2]/span/div|./following-sibling::div[1]/span/div")).findExtendedWebElement(By.xpath("./following-sibling::div[1]//span[contains(text(),'Reviews')]")).getText();
                product.put("reviews",reviewsText.split(" ")[0]);
                products.add(product);
                System.out.println("product = " + product);
            }
            System.out.println("products = " + products);
            this.productsInfo=products;
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }

    }
    @When("user saves products info in csv file")
    public void user_saves_products_info_in_csv_file() throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        csvWriter(this.productsInfo);
        LOGGER.info("Search results captured in products.csv successfully");
    }
    @When("user fetches products info from csv")
    public void user_fetches_products_info_from_csv() throws IOException {

    }
    @Then("user clicks each product and checks product info {string}")
    public void user_clicks_each_product_and_checks_product_info(String product) throws IOException {
        productPage = new ProductPage(getDriver());
        Path outputPath = Path.of("products.csv");

        strategy.setType(CsvRow.class);
        CSVReader reader = new CSVReader(new FileReader("products.csv"));
        reader.skip(1);
        ColumnPositionMappingStrategy<CsvRow> beanStrategy = new ColumnPositionMappingStrategy<CsvRow>();
        beanStrategy.setType(CsvRow.class);
        beanStrategy.setColumnMapping(new String[] {"name","price","rating","reviews"});

        CsvToBean<CsvRow> csvToBean = new CsvToBean<CsvRow>();
        csvToBean.setMappingStrategy(beanStrategy);
        csvToBean.setCsvReader(reader);

        List<CsvRow> csv = csvToBean.parse();
        int index=0;
        List<ExtendedWebElement> searchResultProducts = flipCartLoginPage.findExtendedWebElements(By.xpath("//*[text()='wireless headphones']/ancestor::div[3]/following-sibling::div//img[contains(@alt,'boAt')]"));
        for (CsvRow csvRow : csv) {
                String name = csvRow.getName();
                String price = csvRow.getPrice();
                String rating = csvRow.getRating();
                String reviews = csvRow.getReviews();

                searchResultProducts.get(index++).clickByJs(5);
                String currentWindow= flipCartLoginPage.getDriver().getWindowHandle();
                Set<String> windowHandles = this.getDriver().getWindowHandles();
                WebDriver window = null;
                for(String handle: windowHandles){
                    window = flipCartLoginPage.getDriver().switchTo().window(handle);
                }
                SoftAssert softAssert = new SoftAssert();
                softAssert.assertTrue(productPage.productTitle.getText().contains(name));
                softAssert.assertTrue(productPage.productPrice.getText().contains(price));
                softAssert.assertTrue(productPage.productRating.getText().contains(rating));
                softAssert.assertTrue(productPage.productReviews.getText().contains(reviews));
                softAssert.assertAll();
                LOGGER.info("Verified product : " + name);
                assert window != null;
                window.close();
                flipCartLoginPage.getDriver().switchTo().window(currentWindow);
            }
        LOGGER.info("Products information verified from csv");
    }



    public void csvWriter(List<HashMap<String, String>> listOfMap) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        strategy.setType(CsvRow.class);
        CsvRow[] csvRows=new CsvRow[listOfMap.size()];
        Path outputPath = Path.of("products.csv");

        for(int i=0;i<listOfMap.size();i++){
            HashMap<String, String> row = listOfMap.get(i);
            String name = row.get("name");
            String price = row.get("price");
            String rating = row.get("rating");
            String reviews = row.get("reviews");
            CsvRow csvRow = new CsvRow(name, price, rating, reviews);
            csvRows[i]=csvRow;
        }

        try (var writer = Files.newBufferedWriter(outputPath)) {
            StatefulBeanToCsv<CsvRow> csv = new StatefulBeanToCsvBuilder<CsvRow>(writer)
                    .withMappingStrategy(new AnnotationStrategy<>(CsvRow.class))
                    .build();
            csv.write(Arrays.asList(csvRows));
        }
    }


}
