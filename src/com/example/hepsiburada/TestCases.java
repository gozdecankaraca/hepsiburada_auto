package com.example.hepsiburada;
import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;

public class TestCases {
    private WebDriver driver;
    private static final String URL = "https://www.hepsiburada.com/";

    @Before
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "chromedriver/chromedriver.exe");
        driver = new ChromeDriver();
        driver.get(URL);
        System.out.println("Opened Hepsiburada web page successfully.");
    }

    @Test
    public void userLoginThenAdd2ProductsToCart() throws Exception {

        clickById("myAccount", "Clicked login button on main page successfully.");
        Thread.sleep(1000);
        clickById("login", "Clicked login button on my account successfully.");
        Thread.sleep(1000);

        findByIdSendKeys("txtUserName", "testhepsiburada932@gmail.com", "Type email address.");
        findByIdSendKeys("txtPassword", "testhepsiburada99A", "Type password.");
        clickById("btnLogin", "Clicked login button on login page.");
        System.out.println("Login web page successfully.");

        Thread.sleep(5000);
        WebElement myAccount = driver.findElement(By.xpath("//span[contains(@class,'sf-OldMyAccount-1k66b')]"));
        System.out.println(myAccount.getText());
        Assert.assertEquals("test hepsiburada", myAccount.getText());

        Thread.sleep(3000);
        removeCartItems("Remove cart items.");

        Thread.sleep(1000);
        searchProduct("dolma kalem", "Search product and click search button");
        Thread.sleep(5000);
        openProductFromResultList("Open the product from the result list.");
        Thread.sleep(1000);
        String stockCode = getStockCodeFromTable("//*[@id=\"productTechSpecContainer\"]/table[3]/tbody", "Get stock code from table");
        addToCart2SameProductsFromDifferentSellers("Two products were added to cart successfully.");
        Thread.sleep(1000);
        verifyItemCountIs2InCart("Verify if item count 2.");
        verifyCartProducts(stockCode, "Control the stock code");

    }

    @Test
    public void withoutLoginAdd2ProductsToCart() throws Exception {

        Thread.sleep(3000);
        removeCartItems("Remove cart items.");

        Thread.sleep(1000);
        searchProduct("dolma kalem", "Search product and click search button");
        Thread.sleep(5000);
        openProductFromResultList("Open the product from the result list.");
        Thread.sleep(1000);
        String stockCode = getStockCodeFromTable("//*[@id=\"productTechSpecContainer\"]/table[3]/tbody", "Get stock code from table");
        addToCart2SameProductsFromDifferentSellers("Two products were added to cart successfully.");
        Thread.sleep(1000);
        verifyItemCountIs2InCart("Verify if item count 2.");
        verifyCartProducts(stockCode, "Control the stock code");

    }

    @After
    public void tearDown() {
        driver.quit();
    }

    public void searchProduct(String productName, String message) {
        WebElement searchBox = driver.findElement(By.cssSelector("input[type='text']"));
        searchBox.sendKeys("dolma kalem");
        WebElement searchBoxButton = driver.findElement(By.cssSelector("div[class='SearchBoxOld-buttonContainer']"));
        searchBoxButton.click();
        System.out.println(message);
    }

    public void clickById(String id, String message){
        WebElement webElement = driver.findElement(By.id(id));
        webElement.click();
        System.out.println(message);
    }

    public void clickByClass(String id, String message){
        WebElement webElement = driver.findElement(By.cssSelector(id));
        webElement.click();
        System.out.println(message);
    }



    public void findByIdSendKeys(String id, String text, String message){
        WebElement webElement = driver.findElement(By.id(id));
        webElement.sendKeys(text);
        System.out.println(message);
    }

    public void openProductFromResultList(String message){
        WebElement htmlList = driver.findElement(By.cssSelector("ul[class='product-list results-container do-flex list']"));
        List<WebElement> items = htmlList.findElements(By.cssSelector("div[class='product-image-wrapper']"));
        items.get(0).click();

        System.out.println(message);
    }
    public void addToCart2SameProductsFromDifferentSellers(String message) throws Exception {
        WebElement addToCart = driver.findElement(By.id("addToCart"));
        addToCart.click();
        System.out.println("The first product was added to cart.");
        Thread.sleep(2000);
        clickByClass("div[class='sf-SalesFrontCash-3zIbr hb_sfc_close']","Close sales front cash.");
        Thread.sleep(1000);
        WebElement htmlList = driver.findElement(By.cssSelector("div[class='marketplace-list']"));
        List<WebElement> items = htmlList.findElements(By.cssSelector("div[class='addToCart']"));
        items.get(0).click();
        System.out.println("The second product was added to cart.");
        Thread.sleep(2000);
        clickByClass("div[class='sf-SalesFrontCash-3zIbr hb_sfc_close']","Close sales front cash.");
        clickById("shoppingCart", "Go to shopping cart.");
        System.out.println(message);

    }

    public void removeCartItems(String message) throws Exception{
        WebElement cartItemCount = driver.findElement(By.id("cartItemCount"));
        int cartCount = Integer.valueOf(cartItemCount.getText());
        if(cartCount != 0)
        {
            clickById("shoppingCart", "Go to shopping cart.");
            Thread.sleep(1000);
            clickById("edit_basket_button", "Go to edit basket button.");
            Thread.sleep(1000);
            clickByClass("button[class='sc-AxjAm bnVKnv']", "Remove all items from cart.");
            Thread.sleep(1000);
            clickByClass("button[class='red_3m-Kl']", "Proceed removing the items.");
            Thread.sleep(1000);
            // After removing items, search box is not visible if windows size is not full screen.
            // So automation tool has to go main page to search any product. This is a bug.
            driver.get("https://www.hepsiburada.com/");
        }
    }

    public void verifyItemCountIs2InCart(String message){
        WebElement basketItemCount = driver.findElement(By.xpath("//span[contains(@class,'basket_itemCount_54lDX')]"));
        System.out.println("Get basket item count.");
        String arr[] = basketItemCount.getText().split(" ", 2);
        int cartCount = Integer.valueOf(arr[0]);
        System.out.println("Get value of car count as integer." + cartCount);
        Assert.assertEquals(2, cartCount);
        System.out.println("Assert equals to 2 and cart count.");
        System.out.println(message);
    }

    public String getStockCodeFromTable(String id, String message){
        String stockCode = "";
        WebElement table = driver.findElement(By.xpath(id));
        List<WebElement> rowsList = table.findElements(By.tagName("tr"));

        List<WebElement> columnsList = null;

        for (WebElement row : rowsList) {
            columnsList = row.findElements(By.tagName("td"));

            if(row.getText().contains("Stok Kodu")){
                for (WebElement column : columnsList) {
                    stockCode = column.getText();
                    System.out.println("Getting the stock code.");
                }
            }

        }
        return stockCode;
    }

    public void verifyCartProducts(String stockCode, String message){
        try {
            WebElement products =  driver.findElement(By.id("onboarding_item_list"));
            List<WebElement> productList= products.findElements(By.xpath("//li[@data-sku = '" + stockCode + "']"));
            if(productList.size() != 2){
                Assert.fail("Different item found in the cart.");
            }
        }
        catch (Exception ex){
            Assert.fail("Item could not be found in the cart.");
        }
        System.out.println(message);
    }
}
