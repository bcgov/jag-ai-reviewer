package ca.bc.gov.open.jag.ai.reviewer.page;

import ca.bc.gov.open.jag.ai.reviewer.Keys;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;

public class AiReviewerAdminClientPage extends BasePage {


    @Value("${IDIR_USERNAME:bobross}")
    private String idirUsername;

    @Value("${IDIR_PASSWORD:changeme}")
    private String idirPassword;

    //Page Objects:
    @FindBy(id = "username")
    private WebElement usernameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "kc-login")
    private WebElement signInButton;

    private static final String DOCUMENT_TYPE_LIST = "document-type-list";

    @FindBy(xpath = "//*[@data-testid='add-btn']")
    private WebElement addConfigBtn;

    @FindBy(xpath = "//*[@id='root']/div/div/button[2]")
    private WebElement submitBtn;

    @FindBy(xpath = "//*[@id='root']/div/div/button[1]")
    private WebElement cancelBtn;

    @FindBy(id = "new-config-textfield")
    private WebElement jsonInputTextArea;

    @FindBy(xpath = "//*[@data-testid='update-btn']")
    private WebElement updateConfigBtn;

    @FindBy(xpath = "//*[contains(@data-testid, 'delete')]")
    private WebElement deleteConfigBtn;

    @FindBy(xpath = "//*[@class='document-type-list']/ul/li/span[5]")
    private WebElement documentDescription;

    @FindBy(xpath = "//*[@class='document-type-list']/ul/li/span[3]")
    private WebElement documentType;

    @FindBy(xpath = "//*[@class='document-type-list']/ul/li/span")
    private WebElement documentConfigList;

    public void addNewDocTypeConfiguration() throws IOException {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(DOCUMENT_TYPE_LIST)));

        addConfigBtn.click();

        File resource = new ClassPathResource(
                MessageFormat.format("payload/{0}", Keys.DOCUMENT_TYPE_CONFIG_PAYLOAD)).getFile();

        String configJson = new String(Files.readAllBytes(Paths.get(String.valueOf(resource))));

        jsonInputTextArea.sendKeys(configJson);
        submitBtn.click();

    }

    public void updateDocTypeConfiguration() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(DOCUMENT_TYPE_LIST)));

        updateConfigBtn.click();
        String actualJson = jsonInputTextArea.getText().replace("Response to Civil Claim", "Updated Response to Civil Claim");

        jsonInputTextArea.sendKeys(org.openqa.selenium.Keys.CONTROL + "a");
        jsonInputTextArea.sendKeys(org.openqa.selenium.Keys.DELETE);

        jsonInputTextArea.sendKeys(actualJson);
        submitBtn.click();

    }

    public void deleteDocTypeConfiguration() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(DOCUMENT_TYPE_LIST)));
        deleteConfigBtn.click();
    }

    public String getDocumentDescription() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(DOCUMENT_TYPE_LIST)));
        return documentDescription.getText();
    }

    public String getDocumentType() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(DOCUMENT_TYPE_LIST)));
        return documentType.getText();
    }

    public boolean verifyTheDocumentListIsEmpty() {
        return documentConfigList.getText().isEmpty();
    }

    public void loginWithIdir() {
        wait.until(ExpectedConditions.titleContains("Sign in to ai-reviewer"));
        wait.until(ExpectedConditions.visibilityOf(usernameField));
        usernameField.sendKeys(idirUsername);
        passwordField.sendKeys(idirPassword);
        signInButton.click();
        wait.until(ExpectedConditions.titleIs("AI Reviewer Admin Client"));
    }
}
