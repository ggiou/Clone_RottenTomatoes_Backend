package com.clone.rottentomato.crawling.service;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

import static com.clone.rottentomato.common.constant.CommonConst.OS.*;
import static com.clone.rottentomato.crawling.constant.CrawlingUrlConst.*;


/** selenium 을 통한 crawling 을 위해 webDriver 을 관리하는 서비스
 * - os별 chromeDriver 경로를 자동화 해 window, mac, linux 에서 모두 동작 가능 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WebDriverService {
    private static WebDriverService instance;
    private WebDriver driver;

    private String DRIVER_RUNNING_OS;   // 서버가 실행 중인 os
    private String LAST_DIR_PATH;
    private String WEB_DRIVER_BASIC_PATH;


    /** 싱글톤 인스턴스 반환 메서드 (인스턴스 한번만 생성하고 재사용)*/
    public static synchronized WebDriverService getInstance() {
        if (instance == null) {
            instance = new WebDriverService();
        }
        return instance;
    }

    /** Chrome WebDriver 인스턴스 반환 (없다면 새로운 webDriver 생성)*/
    public WebDriver getChromeDriver() {
        if(driver == null) {
            initializeDriver(); // webDriver가 없다면 초기화하고 재설정
        }
        return driver;
    }

    /** Chrome WebDriver 인스턴스 반환 (없다면 새로운 webDriver 생성, WebDriverManager 를 통해 가져오는지)*/
    public WebDriver getChromeDriver(boolean getByManger) {
        if(driver == null) {
            if(getByManger) {
                // WebDriverManager를 활용하여 ChromeDriver 자동 다운로드 및 설정
                WebDriverManager.chromedriver().setup();
                // ChromeDriver 실행
                ChromeOptions options = new ChromeOptions();
                driver = new ChromeDriver(options);
                log.info("WebDriverManager를 사용하여 ChromeDriver 설정 완료");
            }else {
                initializeDriver(); // webDriver가 없다면 초기화하고 재설정
                log.info("114.0.5735.90 버전의 ChromeDriver 설정 완료");
            }
        }
        return driver;
    }

    /** webDriver 을 실행 os에 맞게 초기화하고 설정하는 메서드
     * <br>- 실행 중인 os 확인해 적합한 chromeDriver 의 경로를 설정 후 webDriver 반환 */
    private void initializeDriver() {
        if (StringUtils.isBlank(DRIVER_RUNNING_OS)) {
            // 실행 중인 os 설정
            DRIVER_RUNNING_OS = RUNNING_OS;

            // Chrome 브라우저가 바르게 설치 되어있는지 확인     // 기존 : (아니라면 재 설치 진행)
            CrawlingVersionManager.setValidChromeVersion();

            // 실행 중인 운영체제에 맞는 ChromeDriver 기본 경로 설정
            setBasicPathByRunningOs();

            // ChromeDriver의 전체 경로 설정
            String chromeDriverPath = WEB_DRIVER_BASIC_PATH + LAST_DIR_PATH + getAppPathByOs("chromedriver");

            // Unix 기반 운영체제(macOS, Linux)에서는 ChromeDriver 실행 권한 부여
            setChmod(chromeDriverPath);

            // WebDriver 시스템 속성 설정
            System.setProperty("webdriver.chrome.driver", chromeDriverPath);

            // ChromeWebDriver 실행
            ChromeOptions options = new ChromeOptions();
            driver = new ChromeDriver(options);
        }
    }

    /**
     * 실행 중인 운영체제에 따라 ChromeDriver 의 기본 경로를 설정하는 함수
     */
    private void setBasicPathByRunningOs() {
        switch (RUNNING_OS) {
            case OS_WIN -> {
                LAST_DIR_PATH = OS_WIN_DIR_PATH;
                WEB_DRIVER_BASIC_PATH = WINDOW_WEB_DRIVER_BASIC_PATH;
            }
            case OS_MAC -> {
                LAST_DIR_PATH = OS_MAC_DIR_PATH;
                WEB_DRIVER_BASIC_PATH = MAC_LINUX_WEB_DRIVER_BASIC_PATH;
            }
            case OS_LINUX -> {
                LAST_DIR_PATH = OS_LINUX_DIR_PATH;
                WEB_DRIVER_BASIC_PATH = MAC_LINUX_WEB_DRIVER_BASIC_PATH;
            }
            default -> {
                log.error("작동 OS 재 설정 필요");
                throw new IllegalArgumentException("Selenium 크롤링을 위한 OS가 존재하지 않습니다.");
            }
        }
    }

    /**
     * Unix(macOS, Linux) 환경에서 실행 파일에 실행 권한을 부여하는 메서드
     * Windows에서는 실행 권한 부여가 필요하지 않음
     * @param checkPath 실행 권한을 부여할 파일 경로
     */
    private void setChmod(String checkPath) {
        if (RUNNING_OS.equals(OS_WIN)) return;

        if (!Files.isExecutable(Paths.get(checkPath))) {
            ProcessBuilder pd = new ProcessBuilder("chmod", "+x", checkPath);
            try {
                Process p = pd.start();
                int exitCode = p.waitFor();
                if (exitCode != 0) {
                    throw new RuntimeException("파일 권한 부여에 실패했습니다. : " + checkPath);
                }
            } catch (IOException | InterruptedException e) {
                log.error("[WebDriverService - setChmod] 크롤링을 위해 실행 파일에 실행 권한을 부여하는데 실패했습니다.");
                throw new RuntimeException("[WebDriverService - setChmod] \n" + e);
            }
        }
    }

    /**
     * 실행 중인 OS에 따라 ChromeDriver의 실행 파일 경로를 반환
     *
     * @param appName 실행 파일 이름 (예: "chromedriver")
     * @return OS에 맞는 실행 파일 경로
     */
    private String getAppPathByOs(String appName) {
        return RUNNING_OS.equals(OS_WIN) ? "\\" + appName + ".exe" : "/" + appName;
    }

    /**
     * 현재 실행 중인 WebDriver를 종료하는 메서드
     * WebDriver를 종료한 후 null로 설정하여 새로운 WebDriver를 생성할 수 있도록 함
     */
    public void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    /**
     * 현재 실행 중인 창을 종료하는 메서드
     * WebDriver 창을 종료
     */
    public void closePage() {
        if (driver != null) {
            driver.close();
        }
    }

    /** 웹페이지를 가져오는 메서드 (세션 만료시 자동으로 드라이버를 새로 시작) */
    private WebElementService getPageWithResetBySessionError(String url, WebElementService elementService) {
        WebDriver d = getChromeDriver(true);
        try {
            d.get(url);  // 웹 페이지 요청
        } catch (Exception e) {
            if (e.getMessage().contains("invalid session id")) {
                log.error("세션 만료 오류 발생. 새 드라이버 세션을 시작합니다.");
                d = resetDriver();  // 새 드라이버 세션 시작
                d.get(url);  // 새 드라이버로 페이지 요청
            } else {
                // 다른 예외 처리
                log.error("페이지 로딩 중 오류 발생: ", e);
                throw e;  // 예외 재던지기
            }
        }
        // driver 을 상속받아, 해당 서비스 사용이 가능하니 페이지가 변경될 때마다 해당 페이지로 새로 elementService를 생성해줘야 한다
        elementService = new WebElementService(d);
        return elementService;
    }

    public WebElementService getPage(String url, WebElementService elementService) {
        return getPageWithResetBySessionError(url, elementService);
    }

    /** 세션이 만료되었을 때 새 WebDriver 세션을 시작하는 메서드 */
    public WebDriver resetDriver() {
        quitDriver(); // 기존 드라이버 종료
        return getChromeDriver(true); // 새 드라이버 생성 및 반환
    }
}
