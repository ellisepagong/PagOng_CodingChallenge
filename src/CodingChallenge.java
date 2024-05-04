import java.io.File;
import java.time.Duration;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.support.ui.*;

public class CodingChallenge {
    
    //Define file paths for ChromeDriver and Chrome for Testing
    private static final String CHROME_BINARY_PATH = "C:\\Users\\admin\\chrome\\win64-124.0.6367.91\\chrome-win64\\chrome.exe";
    private static final String CHROMEDRIVER_PATH = "C:\\Users\\admin\\chromedriver\\win64-124.0.6367.91\\chromedriver-win64\\chromedriver.exe";
    

    //Define URL for saucedemo log-in and home pages
    private static String LOG_IN_PAGE = "https://www.saucedemo.com/";
    private static String HOME_PAGE = "https://www.saucedemo.com/inventory.html";


    // Method to configure ChromeOptions for Selenium WebDriver.
    // Sets the path to the Chrome for Testing binary and ChromeDriver executable. 
    public static WebDriver getDriver(){
        ChromeOptions options = new ChromeOptions();
        options.setBinary(new File(CHROME_BINARY_PATH));
        System.setProperty("webdriver.chrome.driver", CHROMEDRIVER_PATH);
        WebDriver driver = new ChromeDriver(options);
        return driver;
    }

    // Method to input username with common password in log in page
    public static void login(WebDriver driver, String username){
        WebElement userForm = driver.findElement(By.id("user-name"));
        WebElement passForm = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.id("login-button"));
        
        userForm.click();
        userForm.sendKeys(username);
        
        passForm.click();
        passForm.sendKeys("secret_sauce");
        
        loginButton.click();

        try{ //Check if error message exists
            WebElement errorMessageContainer = driver.findElement(By.className("error-message-container"));
            WebElement errorMessage = errorMessageContainer.findElement(By.tagName("h3"));
            System.out.println("User cannot log in, error message: " + errorMessage.getText());
        }catch(NoSuchElementException e){}
    }

    //Method to execute Scenario 1
    public static void scenario1(WebDriver driver){
        login(driver, "standard_user");
        String url = driver.getCurrentUrl();

        if (url.equals(HOME_PAGE)){ //Check if user is in home page
            System.out.println("User has arrived at Home Page");

            WebElement menuButton = driver.findElement(By.id("react-burger-menu-btn"));
            menuButton.click();

            //wait for sidebar animation before retrieving log-out link
            WebElement logoutLink = (new WebDriverWait(driver, Duration.ofMillis(100))).until(ExpectedConditions.elementToBeClickable(By.id("logout_sidebar_link")));
            logoutLink.click();

            url = driver.getCurrentUrl();
            
            if(url.equals(LOG_IN_PAGE)){ //Check if user logged out successfully
                System.out.println("User logged out successfully");
            }else{
                System.out.println("User cannot log out");
            }
        }else{
            System.out.println("User is unable to log in");
        }
    }

    //Method to execute Scenario 2
    public static void scenario2(WebDriver driver){
        login(driver, "locked_out_user");
    }

    public static void main(String[] args) throws Exception {

        //Configure ChromeDriver and launch Chrome for Testing with saucedemo
        WebDriver driver = getDriver();
        driver.get(LOG_IN_PAGE);
        
        //Specify scenario to execute
        System.out.println("Scenario 1:\n- log in using standard user\n- verify that user is able to navigate to home page");
        System.out.println("- log out\n- verify that user is navigated to login page\n");
        System.out.println("Scenario 2\n- log in using locked out user\n- verify error message\n");
        System.out.print("Choose Scenario: ");
        int scene = System.in.read();

        //Scenario 1
        if (scene==49){scenario1(driver);}
        //Scenario 2
        else if (scene==50){scenario2(driver);}
        else{System.out.println("Not a given scenario");}
    }

}
