package com.clone.rottentomato.crawling.service;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 *  WebElement 관련 유틸리티 기능을 제공하는 서비스 클래스
 */
@Slf4j
public class WebElementService {
    private WebDriverWait wait;
    private WebDriver webDriver;

    /**
     * 기본 생성자 - WebDriver를 기반으로 WebDriverWait을 설정 (기본 대기 시간: 5초)
     */
    public WebElementService(WebDriver driver) {
        this.webDriver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
    }

    /**
     * 사용자 지정 대기 시간을 설정할 수 있는 생성자
     */
    public WebElementService(WebDriver driver, int waitSecond) {
        this.webDriver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(waitSecond));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
    }

    // ==================== CSS Selector 기반 요소 찾기 ====================
    /** CSS Selector 을 이용해 요소 찾아서 반환 */
    public WebElement getByCssSelectore(String selector) {
        return getPresenceElement(By.cssSelector(selector));
    }

    /** 부모 요소 내에서 CSS Selector 을 이용해 자식요소 찾아서 반환 */
    public WebElement getByCssSelectore(WebElement parentElement, String selector) {
        return getPresenceChildElement(parentElement, By.cssSelector(selector));
    }

    // ==================== ID 기반 요소 찾기 ====================
    /** ID를 이용하여 요소 찾아서 반환 */
    public WebElement getById(String id) {
        return getPresenceElement(By.id(id));
    }

    public WebElement getIndexById(int index, String idValue) {
        List<WebElement> elementList = getListById(idValue);
        if(elementList.size() > index) return elementList.get(index);
        return null;
    }

    public List<WebElement> getListById(String idValue) {
        List<WebElement> elementList = webDriver.findElements(By.id(idValue));
        if(!CollectionUtils.isEmpty(elementList)) return elementList;
        return null;
    }

    /**  부모 요소 내에서 ID를 이용하여 자식 요소 찾아 반환 */
     public WebElement getById(WebElement parentElement, String id) {
        return getPresenceChildElement(parentElement, By.id(id));
    }

    // ==================== Class Name 기반 요소 찾기 ====================

    /** 클래스명을 이용하여 요소 찾아 반환 */
    public WebElement getByClassName(String className) {
        return getPresenceElement(By.className(className));
    }

    /** 부모 요소 내에서 클래스명을 이용해 자식 요소를 찾아 반환 */
    public WebElement getByClassName(WebElement parentElement, String className) {
        return findElement(parentElement, By.className(className));
    }

    /** 부모 요소 내에서 클래스명을 이용해 자식 요소를 찾아 반환 */
    public List<WebElement> getListByClassName(WebElement parentElement, String className) {
        return findElements(parentElement, By.className(className));
    }

    /** 부모 요소 내에서 클래스명을 이용해 자식 요소들을 찾아 그 중 n 번째 요소를 반환
     * @return WebElement(index 번째 요소가 존재한다면) / 없다면 null */
    public WebElement getIndexThElementByClassNameList(WebElement parentElement, String className, int index) {
        List<WebElement> elementList = findElements(parentElement, By.className(className));
        if(elementList.size() > index) return elementList.get(index);
        return null;
    }

    /** 부모 요소 내에서 해당 클래스 중, visible (보이는) 자식 요소를 찾아 반환*/
    public WebElement getByClassNameOfVisible(WebElement parentElement, String className) {
        return getVisableElement(parentElement, By.className(className));
    }

    /** 특정 클래스들을 모두 포함하는 요소 찾기 */
    public WebElement getByMultipleClassNames(String... classNames) {
        String selector = "." + String.join(".", classNames); // .class1.class2.class3 형식 생성
        return getPresenceElement(By.cssSelector(selector));
    }

    /** 특정 클래스들을 모두 포함하는 요소 리스트 찾기 */
    public List<WebElement> getListByMultipleClassNames(String... classNames) {
        String selector = "." + String.join(".", classNames);
        return findElements(By.cssSelector(selector));
    }

    /** 부모 요소 내에서 특정 클래스들을 모두 포함하는 자식 요소 찾기 */
    public WebElement getByMultipleClassNames(WebElement parentElement, String... classNames) {
        String selector = "." + String.join(".", classNames);
        return findElement(parentElement, By.cssSelector(selector));
    }

    /** 부모 요소 내에서 특정 클래스들을 모두 포함하는 모든 자식 요소 찾기 */
    public List<WebElement> getListByMultipleClassNames(WebElement parentElement, String... classNames) {
        String selector = "." + String.join(".", classNames);
        return findElements(parentElement, By.cssSelector(selector));
    }

    public WebElement getIndexByMultipleClassNames(int index,  String... classNames) {
        List<WebElement> elementList = getListByMultipleClassNames(classNames);
        if(!CollectionUtils.isEmpty(elementList) && elementList.size() > index) return elementList.get(index);
        return null;
    }


    // ==================== TagName 기반 요소 찾기 ====================

    /** 태크명을 이용해 요소를 찾아 반환 */
    public WebElement getByTagName(String tagName) {
        return getPresenceElement(By.tagName(tagName));
    }

    /** 부모 요소 내에서 해당 태그명을 가진 자식 요소를 찾아 반환 */
    public WebElement getByTagName(WebElement parentElement, String tagName) {
        return findElement(parentElement, By.tagName(tagName));
    }

    /** 부모 요소 내에서 특정 태그명의 모든 자식 요소 목록 반환 */
    public List<WebElement> getListByTagName(WebElement parentElement, String tagName) {
        getPresenceChildElement(parentElement, By.tagName(tagName));
        return findElements(parentElement, By.tagName(tagName));
    }

    /** 부모 요소 내에서 해당 태그명 중, visible (보이는) 자식 요소를 찾아 반환*/
    public WebElement getByTagNameOfVisible(WebElement parentElement, String tagName) {
        return getVisableElement(parentElement, By.tagName(tagName));
    }


    // ==================== page 관련 함수  ====================
    /** 페이지가 완전히 로드될 때까지 대기 */
    public void waitForPageLoad() {
        new WebDriverWait(webDriver, Duration.ofSeconds(10)).until(webDriver ->
                Objects.equals((String) ((org.openqa.selenium.JavascriptExecutor) webDriver)
                        .executeScript("return document.readyState"), "complete")
        );
    }

    // ==================== 해당 service 에서 사용하는 공통 함수  ====================
    /** 요소가 dom 에 현재 존재한다면 반환 (visiable 필수 x) */
    private WebElement getPresenceElement(By by) {
        this.checkValidRequest();
        // 특정 by 의 요소들이 나타날 때까지 기다린 후 반환
        return wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

    /** 자식 요소가 dom 에 현재 존재한다면 반환 (visiable 필수 x) */
    private WebElement getPresenceChildElement(WebElement parentElement, By by) {
        this.checkValidRequest();
        // 부모 요소의 자식 요소 중 특정 by 의 요소들이 나타날 때까지 기다린 후 반환
        return wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(parentElement, by));
    }

    /** 자식 요소가 여러 개 존재할 때 리스트로 반환 */
    private List<WebElement> getPresenceChildElements(WebElement parentElement, By by) {
        this.checkValidRequest();
        // 부모 요소의 자식 요소 중 특정 by의 요소들이 나타날 때까지 기다린 후 리스트 반환
        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(by));
    }

    /** 요소가 dom 에 존재한다면 반환 (visiable 필수 x) */
    private WebElement getVisableElement(WebElement parentElement, By by) {
        this.checkValidRequest();
        // 특정 by 의 요소들이 보일 때 까지 기다린 후 반환
        return wait.until(ExpectedConditions.visibilityOf(parentElement.findElement(by)));
    }

    /** 요소가 있다면 반환, 없으면 null */
    private WebElement findElement(WebElement element, By by) {
        try {
            List<WebElement> elements = element.findElements(by);
            if(!elements.isEmpty()) return elements.get(0);
        }catch (NoSuchElementException e) {
            log.error("[findElementError] {} 이름의 요소가 존재하지 않습니다.", by.toString(), e);
        }
        return null;
    }

    private List<WebElement> findElements(WebElement element, By by) {
        try {
            return element.findElements(by);
        }catch (NoSuchElementException e) {
            return null;
        }
    }

    private List<WebElement> findElements(By by) {
        try {
            return webDriver.findElements(by);
        }catch (NoSuchElementException e) {
            return null;
        }
    }

    /** 유효성 체크 */
    private void checkValidRequest(){
        if (Objects.isNull(this.wait)) {
            throw new IllegalArgumentException("WebDriverWait가 존재하지 않습니다.");
        }
    }
}
