package com.clone.rottentomato.common.service.crawling;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Component;

import static com.clone.rottentomato.common.constant.CommonConst.OS.*;


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

    /** webDriver 을 실행 os에 맞게 초기화하고 설정하는 메서드
     * <br>- 실행 중인 os 확인해 적합한 chromeDriver 의 경로를 설정 후 webDriver 반환 */
    private void initializeDriver() {
        if(StringUtils.isBlank(DRIVER_RUNNING_OS)) {
            // 실행 중인 os 설정
            DRIVER_RUNNING_OS = RUNNING_OS;


        }

    }
}
