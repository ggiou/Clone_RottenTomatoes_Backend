package com.clone.rottentomato.crawling.service;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

/**
 *  WebElement 관련 유틸리티 기능을 제공하는 서비스 클래스
 */
@Slf4j
public class WebElementService {
    private WebDriverWait wait;

    /**
     * 기본 생성자 - WebDriver를 기반으로 WebDriverWait을 설정 (기본 대기 시간: 10초)
     */
    public WebElementService(WebDriver driver) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /**
     * 사용자 지정 대기 시간을 설정할 수 있는 생성자
     */
    public WebElementService(WebDriver driver, int waitSecond) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(waitSecond));
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
        return getPresenceChildElement(parentElement, By.className(className));
    }

    /** 부모 요소 내에서 해당 클래스 중, visible (보이는) 자식 요소를 찾아 반환*/
    public WebElement getByClassNameOfVisible(WebElement parentElement, String className) {
        return getVisableElement(parentElement, By.className(className));
    }

    // ==================== TagName 기반 요소 찾기 ====================

    /** 태크명을 이용해 요소를 찾아 반환 */
    public WebElement getByTagName(String tagName) {
        return getPresenceElement(By.tagName(tagName));
    }

    /** 부모 요소 내에서 해당 태그명을 가진 자식 요소를 찾아 반환 */
    public WebElement getByTagName(WebElement parentElement, String tagName) {
        return getPresenceChildElement(parentElement, By.tagName(tagName));
    }

    /** 부모 요소 내에서 특정 태그명의 모든 자식 요소 목록 반환 */
    public List<WebElement> getListByTagName(WebElement parentElement, String tagName) {
        getPresenceChildElement(parentElement, By.tagName(tagName));
        return parentElement.findElements(By.tagName(tagName));
    }

    /** 부모 요소 내에서 해당 태그명 중, visible (보이는) 자식 요소를 찾아 반환*/
    public WebElement getByTagNameOfVisible(WebElement parentElement, String tagName) {
        return getVisableElement(parentElement, By.tagName(tagName));
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

    /** 요소가 dom 에 존재한다면 반환 (visiable 필수 x) */
    private WebElement getVisableElement(WebElement parentElement, By by) {
        this.checkValidRequest();
        // 특정 by 의 요소들이 보일 때 까지 기다린 후 반환
        return wait.until(ExpectedConditions.visibilityOf(parentElement.findElement(by)));
    }

    /** 유효성 체크 */
    private void checkValidRequest(){
        if (Objects.isNull(this.wait)) {
            throw new IllegalArgumentException("WebDriverWait가 존재하지 않습니다.");
        }
    }
}
