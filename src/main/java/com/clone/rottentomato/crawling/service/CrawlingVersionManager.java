package com.clone.rottentomato.crawling.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

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
        if(RUNNING_OS.isBlank()) throw new RuntimeException("실행중인 운영체제가 없습니다.");
        // 운영체제에 맞는 크롬 실행파일 경로 판단
        String chromePath = MAC_CHROME_PATH;
        if(isWin) chromePath = WINDOWS_CHROME_PATH;
        else if(isLinux) chromePath = LINUX_CHROME_PATH;

        try{
            // 원래는 유효하지 않을 경우, 설치하도록 할려 했으나 제대로 동작이 안해, 임시로 수동 설치 하도록 오류 발생
            // 임시 - 크롬 버전 유효성 체크 과정
            // 1. 크롬 실행 파일 존재 여부 확인
            File chromeExecutable = new File(chromePath);
            if(!chromeExecutable.exists()) {
                log.debug("크롬 실행파일이 존재하지 않습니다. 유효 버전 - " + VALID_CHROME_VERSION);
                throw new RuntimeException("크롬 브라우저가 설치되어 있지 않습니다. 크롬 브라우저 114 버전 설치가 필요합니다.");
            }
            // 2. 현재 크롬 버전 가져오기
            ProcessBuilder processBuilder = new ProcessBuilder(chromePath, "--version");
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String versionInfo = reader.readLine();

            // 3. 유효한 버전인지 확인
            if (StringUtils.isEmpty(versionInfo) || !VALID_CHROME_VERSION.equals(versionInfo.split(" ")[2])) {
                log.info("Chrome is not valid version. Your version: " + versionInfo);
                throw new RuntimeException("크롬 브라우저 114 버전 설치가 필요합니다. 기존 " + versionInfo + " 버전은 유효하지 않습니다.");
            }
            
            // 4. 유효한 버전이라면 크롬 자동 업데이트 비활성 화
            setValidChromeVersion();

            /*
            // 기존의 설치하는 과정 코드
            File chromeExecutable = new File(chromePath);
            if(!chromeExecutable.exists()) {
                log.debug("크롬 실행파일이 존재하지 않습니다. 유효한 버전 설치를 진행하겠습니다. " + VALID_CHROME_VERSION);
                // 크롬 설치 후, 자동 업데이트 해제
                installChrome();
                disableChromeAutoUpdate();
                return;
            }

            // 2. 현재 크롬 버전 가져오기
            ProcessBuilder processBuilder = new ProcessBuilder(chromePath, "--version");
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String versionInfo = reader.readLine();

            // 3. 유효한 버전인지 확인
            if (StringUtils.isEmpty(versionInfo) || !VALID_CHROME_VERSION.equals(versionInfo.split(" ")[2])) {
                log.info("크롬 브라우저 114 버전 설치를 진행하겠습니다. 기존 " + versionInfo + " 버전은 유효하지 않습니다.");
                // 기존 크롬 파일 삭제 후 설치하고 업데이트 비활성화 진행
                removeChrome();
                installChrome();
                disableChromeAutoUpdate();
            }
            */

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("유효한 크롬 버전 설치 여부 확인 중 오류가 발생했습니다.\n" + e.getMessage());
        }
    }

    /** chrome 다운로드 & 설치하는 메서드 */
    private static void installChrome() {
        // 운영체제에 따라 다운로드할 크롬 설치 파일의 url 지정
        String downloadUrl = "https://dl.google.com/chrome/mac/stable/GGRO/googlechrome.dmg"; // mac 기본 크롬 설치 경로
        if(isWin){
            downloadUrl = "https://dl.google.com/chrome/install/114.0.5735.90/chrome_installer.exe";
        }else if (isLinux) {
            downloadUrl = "https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb";
        }

        try {
            //  크롬 설치 파일 다운로드
            Process downloadProcess = Runtime.getRuntime().exec("curl -L -o /tmp/chrome_installer " + downloadUrl);
            downloadProcess.waitFor();

            // 운영체제에 따라 다운 받은 크롬 파일을 설치하는 과정 (설치 명령어 실행)
            if (isWin) {
                Runtime.getRuntime().exec("cmd /c start /wait /tmp/chrome_installer");
            } else if (isLinux) {
                Runtime.getRuntime().exec("sudo dpkg -i /tmp/chrome_installer");
            } else {
                // 크롬 설치파일(.dmg)를 가상 디스크로 마운트해 macOs의 디스크 볼륨에 연결
                Process mountProcess = Runtime.getRuntime().exec("hdiutil attach /tmp/chrome_installer");
                mountProcess.waitFor();
                // 마운트된 크롬 앱파일을 /Applications/ 폴더로 복사하여 시스템에 설치
                Process copyProcess = Runtime.getRuntime().exec("cp -R /Volumes/Google\\ Chrome/Google\\ Chrome.app /Applications/");
                copyProcess.waitFor();
                // 설치 끝난 후 필요 없어진 마운트된 디스크를 해제해 정리
                Runtime.getRuntime().exec("hdiutil detach /Volumes/Google\\ Chrome");
            }
        }catch (Exception e){
            //IOException, InterruptedException
            log.error("[chromeVersionManager] installChrome : "+e.getMessage());
            throw new RuntimeException("크롬 브라우저 다운로드 중 에러가 발생했습니다.");
        }

        log.debug("크롬이 정상적으로 설치되었습니다.");
    }

    /** chrome 제거하는 메서드 */
    private static void removeChrome(){
        try {

            // 운영체제별 크롬 파일 제거하는 명령어 실행
            if (isWin) {
                Runtime.getRuntime().exec("cmd /c taskkill /F /IM chrome.exe");
                Runtime.getRuntime().exec("cmd /c rmdir /S /Q \"C:\\Program Files\\Google\\Chrome\"");
            } else if (isLinux) {
                Runtime.getRuntime().exec("sudo apt-get remove --purge google-chrome-stable -y");
            } else {
                Process killProcess = Runtime.getRuntime().exec("pkill -f 'Google Chrome'");
                killProcess.waitFor();
                Process deleteProcess = Runtime.getRuntime().exec("sudo rm -rf /Applications/Google\\ Chrome.app");
                deleteProcess.waitFor();
            }
        }catch (Exception e){
            //IOException, InterruptedException
            log.error("[chromeVersionManager] removeChrome : "+e.getMessage());
            throw new RuntimeException("크롬 브라우저 삭제중 에러가 발생했습니다.");
        }
    }

    /** chrome 브라우저의 자동 업데이트를 비활성화 하는 함수
     * <br> - 버전이 달라지면 크롤링이 제대로 동작하지 않아, 자동업데이트를 꺼줘야 한다. */
    private static void disableChromeAutoUpdate() {
        try {
            if (isWin) {
                Runtime.getRuntime().exec("reg add \"HKLM\\SOFTWARE\\Policies\\Google\\Update\" /v \"AutoUpdateCheckPeriodMinutes\" /t REG_DWORD /d 0 /f");
            } else if (isLinux) {
                Runtime.getRuntime().exec("echo \"Package: google-chrome-stable\" | sudo tee -a /etc/apt/preferences.d/google-chrome");
            } else {
                Runtime.getRuntime().exec("defaults write com.google.Keystone.Agent checkInterval 0");
            }
        }catch (Exception e){
            //IOException, InterruptedException
            log.error("[chromeVersionManager] disableChromeAutoUpdateError : "+e.getMessage());
            throw new RuntimeException("크롬 브라우저 자동 업데이트 비활성화 중 오류가 발생했습니다.");
        }
    }
}
