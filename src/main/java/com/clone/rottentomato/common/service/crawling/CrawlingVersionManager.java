package com.clone.rottentomato.common.service.crawling;

import lombok.extern.slf4j.Slf4j;


import java.io.File;
import java.io.IOException;

import static com.clone.rottentomato.common.constant.CommonConst.OS.*;

@Slf4j
public class CrawlingVersionManager {
    private static final String VALID_CHROME_VERSION = "114.0.5735.90"; // 해당 프로젝트에서 사용할 크롬 버전

    /** 각 운영체제별 기본 크롬 실행 파일 경로 */
    private static final String WINDOWS_CHROME_PATH = "C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe";
    private static final String MAC_CHROME_PATH = "/Applications/Google Chrome.app/Contents/MacOS/Google Chrome";
    private static final String LINUX_CHROME_PATH = "/usr/bin/google-chrome";

    private static final boolean isWin = RUNNING_OS.equals(OS_WIN);
    private static final boolean isLinux = RUNNING_OS.equals(OS_LINUX);

    /** 현재 설치된 크롬 브라우저의 버전이, 설정한 유효 버전인지 확인 하고,
     * 유효하지 않은 경우 삭제 후 재설치 하는 메서드 */
    public static void setValidChromeVersion(){
        // os에 맞는 크롬 실행파일 경로 판단
        String chromePath = MAC_CHROME_PATH;
        if(isWin) chromePath = WINDOWS_CHROME_PATH;
        else if(isLinux) chromePath = LINUX_CHROME_PATH;

        try{
            // 1. 크롬 실행 파일 존재 여부 확인
            File chromeExecutable = new File(chromePath);
            if(!chromeExecutable.exists()) {
                log.debug("Chrome is not installed. Installing desired version " + VALID_CHROME_VERSION);

            }
        }catch(Exception e){

        }
    }

    /** chrome 다운로드 & 설치하는 메서드 */
    private static void installChrome() throws IOException, InterruptedException {
        // 운영체제에 따라 다운로드할 크롬 설치 파일의 url 지정
        String downloadUrl = "";
    }
}
