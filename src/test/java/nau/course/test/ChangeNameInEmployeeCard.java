package nau.course.test;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import java.util.concurrent.TimeUnit;

public class ChangeNameInEmployeeCard {

    public  WebDriver driver;
    @Before
        public void initialiseDriver() {
        System.setProperty("webdriver.gecko.driver", "C:\\WebDriver\\bin\\geckodriver.exe");
        driver=new FirefoxDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
    }
    @Test
    /** testChangeNameInEmployeeCard
     * тест имитирует следующие действия пользователя
     * логинится http://test-m.nsd.naumen.ru/,
     * login "ro"
     * password "123"
     * пытается заменить в карточке сотрудника фамилию имя и отчечство на
     * "ТестИмя" "ТестФамилия" "ТестОтчество"
     * проверяет поменялись ли в карточке фамилия имя и отчество на требуемые
     * затем редактироует карточку в исходное состояние
     */
    public void testChangeNameInEmployeeCard(){
        driver.get("http://test-m.nsd.naumen.ru/");
        driver.findElement(By.id("username")).sendKeys("ro");
        driver.findElement(By.cssSelector("#password")).sendKeys("123");
        driver.findElement(By.xpath("//form/input")).submit();
        driver.findElement(By.xpath("//div[@class='gwt-ToolPanel " +
                "toolPanelIsInsideUIDisplayContent FixedToolPanel']/table[@class='buttonsGroup']")).click();
//получаем текущие значения полей? для возврата после прохождения теста

        String family=driver.findElement(By.id("gwt-debug-lastName-value")).getAttribute("value");
        String name=driver.findElement(By.id("gwt-debug-firstName-value")).getAttribute("value");
        String patronomic=driver.findElement(By.id("gwt-debug-middleName-value")).getAttribute("value");

        WebElement textFildFamily= driver.findElement(By.id("gwt-debug-lastName-value"));
        textFildFamily.clear();
        textFildFamily.sendKeys("ТестФамилия");

        WebElement textFildName= driver.findElement(By.id("gwt-debug-firstName-value"));
        textFildName.clear();
        textFildName.sendKeys("ТестИмя");

        WebElement textFildPatronomic= driver.findElement(By.id("gwt-debug-middleName-value"));
        textFildPatronomic.clear();
        textFildPatronomic.sendKeys("ТестОтчество");

        WebElement submitChanges= driver.findElement(By.id("gwt-debug-apply"));
        submitChanges.click();

        //проверить результаты
        //без этого ожидания на получении TestName или TestFamily или TestPatronomic с частотой около 20% тест падает
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("Ошибка, ожидание 1000мс. не выполнено");
            e.printStackTrace();
        }
        String testFamily=driver.findElement(By.id("gwt-debug-firstName-value")).getText();
        String testName=driver.findElement(By.id("gwt-debug-lastName-value")).getText();
        String testPatronomic=driver.findElement(By.id("gwt-debug-middleName-value")).getText();

        String testFIO=testFamily.concat(testName).concat(testPatronomic);
        String msg=String.format("Изменение фио не удалось, пытались изменить на %s , получили %s",
                "ТестИмяТестФамилияТестОтчество",testFIO );
        Assert.assertTrue(msg, "ТестИмяТестФамилияТестОтчество".equals(testFIO));

        //вернуть как было
        driver.findElement(By.xpath("//td/div[@class='g-button edit_icon_iconsForControls_blue " +
                "buttonFirst buttonLast']")).click();
/*!!!! непонятное поведение !!!!!
* без следующих трх строк элементы не находятся, несмотря на
* то, что эти строки написаны ранее( строки 48,52,56)
 */
        textFildFamily =driver.findElement(By.id("gwt-debug-lastName-value"));
        textFildName= driver.findElement(By.id("gwt-debug-firstName-value"));
        textFildPatronomic= driver.findElement(By.id("gwt-debug-middleName-value"));
        textFildFamily.clear();
        textFildFamily.sendKeys(family);
        textFildName.clear();
        textFildName.sendKeys(name);
        textFildPatronomic.clear();
        textFildPatronomic.sendKeys(patronomic);
        driver.findElement(By.id("gwt-debug-apply")).click();
    }

    @After
    public void closeFirefoxWindow(){
        driver.quit();
    }
}

