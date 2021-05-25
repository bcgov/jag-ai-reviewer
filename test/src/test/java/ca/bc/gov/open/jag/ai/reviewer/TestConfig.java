package ca.bc.gov.open.jag.ai.reviewer;

import ca.bc.gov.open.jag.ai.reviewer.config.BrowserScopePostProcessor;
import ca.bc.gov.open.jag.ai.reviewer.page.AiReviewerAdminClientPage;
import ca.bc.gov.open.jag.ai.reviewer.services.DocumentTypeConfigService;
import ca.bc.gov.open.jag.ai.reviewer.services.ExtractDocumentService;
import ca.bc.gov.open.jag.ai.reviewer.services.ExtractNotificationService;
import ca.bc.gov.open.jag.ai.reviewer.services.OauthService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class TestConfig {

    @Value("${default.timeout:30}")
    private int timeout;

    @Bean
    public OauthService oauthService() {
        return new OauthService();
    }

    @Bean
    public ExtractDocumentService extractDocumentService() {
        return new ExtractDocumentService();
    }

    @Bean
    public DocumentTypeConfigService documentTypeConfigService() {
        return new DocumentTypeConfigService();
    }

    @Bean
    public ExtractNotificationService extractNotificationService() {
        return new ExtractNotificationService();
    }

    @Bean
    public static BeanFactoryPostProcessor beanFactoryPostProcessor() {
        return new BrowserScopePostProcessor();
    }

    @Bean
    @Scope("browserscope")
    public WebDriver chromeDriver() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.setHeadless(true);
        options.addArguments("--window-size=1920,1080");

        return new ChromeDriver(options);
    }

    @Bean
    @Scope("prototype")
    public WebDriverWait webDriverWait(WebDriver driver) {
        return new WebDriverWait(driver, this.timeout);
    }

    @Bean
    @Scope("prototype")
    public AiReviewerAdminClientPage aiReviewerAdminClientPage() {
        return new AiReviewerAdminClientPage();
    }

}
