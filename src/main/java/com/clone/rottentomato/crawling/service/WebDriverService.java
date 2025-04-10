package com.clone.rottentomato.crawling.service;

import io.github.bonigarcia.wdm.WebDriverManager;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Component;

import java.util.Objects;


/** selenium 을 통한 crawling 을 위해 webDriver 을 관리하는 서비스
 * - ChromeDriver 을 사용해 자동 관리 */
@Slf4j
@Component
public class WebDriverService {
    private WebDriver driver;
    private final Object lock = new Object();   // 동기화 락
    // chromeDriver가 너무 오래 실행되면 크롤링 속도가 느려지고, 메모리 점유가 증가하니, 크롤링 요청횟수 최대치 설정
    private int requestCount = 0;   // 드라이버 요청 횟수
    private static final int MAX_REQUEST = 50; // 드라이버 최대 요청 횟수

    private WebDriverService(){
        // 사용 할때 켜지도록 생성자에는 driver 객체 선언 x
        //setChromeDriver();
    }

    /** Chrome WebDriver 인스턴스 반환 (없다면 새로운 webDriver 생성, WebDriverManager 를 통해 자동 set)*/
    private void setChromeDriver() {
        if(driver == null) {
            synchronized (lock) {   // 동기화 처리
                if(driver == null) {
                    // WebDriverManager를 활용하여 ChromeDriver 자동 다운로드 및 설정
                    WebDriverManager.chromedriver().setup();
                    ChromeOptions options = new ChromeOptions();
                    //options.addArguments("--headless"); // 크롬 창 안띄우기 (불필요한 랜더링 방지)
                    driver = new ChromeDriver(options);
                    log.debug("ChromeDriver 초기화 완료");
                }
            }
        }
    }

    /** 현재 WebDriver 반환 */
    protected WebDriver getDriver(){
        synchronized (lock){
            if (driver ==  null){
                log.debug("WebDriver 가 없습니다. 새로 초기화 합니다.");
                setChromeDriver();
            }
            requestCount++;

            // 요청 횟수가 max 이상이면 드라이버 재시작
            if(requestCount >= MAX_REQUEST){
                log.debug("요청 횟수 {} 회 초과. WebDriver를 재시작합니다.", requestCount);
                resetDriver();
            }
            return driver;
        }
    }

    /**
     * 현재 실행 중인 WebDriver를 종료하는 메서드
     * WebDriver를 종료한 후 null로 설정하여 새로운 WebDriver를 생성할 수 있도록 함
     */
    protected void quitDriver() {
        synchronized (lock) {
            if (driver != null) {
                driver.quit();
                driver = null;
                log.debug("ChromeDriver 종료");
            }
        }
    }

    /** 세션이 오류 발생 시 (만료) 새 WebDriver 세션을 시작하는 메서드 */
    protected void resetDriver() {
        synchronized (lock) {
            quitDriver(); // 기존 드라이버 종료
            setChromeDriver(); // 새 드라이버 생성 및 반환
            requestCount = 0;
        }
    }

    /** Spring 종료 시 ChromeDriver 정리 */
    @PreDestroy
    protected void cleanUp(){
        log.info("서버 종료 중.. ChromeDrive 를 종료 하겠습니다..");
        quitDriver();
    }


}
