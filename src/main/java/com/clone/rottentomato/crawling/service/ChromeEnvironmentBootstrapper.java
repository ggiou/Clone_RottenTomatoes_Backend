package com.clone.rottentomato.crawling.service;

import com.clone.rottentomato.common.constant.CommonConst;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

import static com.clone.rottentomato.common.constant.CommonConst.OS.*;

/** 크롤링을 위한 chrome 환경 설치 관리자 */
@Component
@Slf4j
public class ChromeEnvironmentBootstrapper {
    /** 각 운영체제별 기본 크롬 실행 파일 경로 */
    private static final String WINDOWS_CHROME_PATH = "C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe";
    private static final String MAC_CHROME_PATH = "/Applications/Google Chrome.app/Contents/MacOS/Google Chrome";
    private static final String LOCAL_LINUX_PATH = CommonConst.USER_HOME + "/chrome-bin";
    private static final String LOCAL_CHROME_BINARY = LOCAL_LINUX_PATH + "/opt/google/chrome/google-chrome";

    @PostConstruct
    public void init() throws IOException, InterruptedException {
        // 현재 실행 중인 os
        String os = RUNNING_OS;
        String chromePath = detectChromePath(os);

        // 크롬이 설치 되어있지 않다면, 크롬 설치
        if(Objects.isNull(chromePath)){
            log.info("[ChromeEnv] 크롬 브라우저가 존재 하지 않습니다. 설치를 시작합니다.");
            installChrome(os);
            chromePath = detectChromePath(os);
        }

        if(Objects.isNull(chromePath) || !Files.exists(Paths.get(chromePath))){
            throw new RuntimeException("[ChromeEnv] 크롬 브라우저 설치에 실패했습니다. 반복 실패하면, 수동 설치 후 서버 재가동이 필요합니다.");
        }

        log.info("[ChromeEnv] Chrome 브라우저 설치 경로 : " + chromePath);
    }

    /** os 별 크롬 설치 경로 탐지
     * @return path or null - 크롬이 설치 x 일 경우 null */
    private String detectChromePath(String os){
        try {
            if(OS_WIN.equals(os)) return Files.exists(Paths.get(WINDOWS_CHROME_PATH)) ? WINDOWS_CHROME_PATH : null;
            if(OS_MAC.equals(os)) return Files.exists(Paths.get(MAC_CHROME_PATH)) ? MAC_CHROME_PATH : null;
            if(OS_LINUX.equals(os)){
                // 1. 먼저 시스템에 크롬이 설치되어 있는지 확인
                ProcessBuilder pb = new ProcessBuilder("which", "google-chrome");
                Process process = pb.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String chrome = reader.readLine();
                if(StringUtils.isNotBlank(chrome)) return chrome;

                // 2. 로컬 설치 경로 확인
                return Files.exists(Paths.get(LOCAL_CHROME_BINARY)) ? LOCAL_CHROME_BINARY : null;
            }
        }catch (Exception e){
            return null;
        }
        return null;
    }

    /** os 별 chrome 브라우저 설치 */
    private void installChrome(String os) throws IOException, InterruptedException {
        if (OS_WIN.equals(os)) {
            log.info("[ChromeEnv] (Window) 크롬 수동 설치가 필요합니다.. 링크 : https://www.google.com/chrome/");
            throw new InterruptedException("[ChromeEnv] chrome 브라우저 설치 후 서버 재 실행이 필요합니다.");
        }else if (OS_MAC.equals(os)) {
            log.info("[ChromeEnv] (Mac) 크롬 브라우저 설치를 시작합니다.. ");
            new ProcessBuilder("brew", "install", "--cask", "google-chrome").inheritIO().start().waitFor();
        }else if (OS_LINUX.equals(os)) {
            if(canUseSudo()){
                log.info("[ChromeEnv] (Linux - sudo) 크롬 브라우저 설치를 시작합니다.. ");
                String[] commands = {
                        "bash", "-c",
                        "wget -q https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb && " +
                        "sudo apt install -y ./google-chrome-stable_current_amd64.deb"
                };
                new ProcessBuilder(commands).inheritIO().start().waitFor();
            }else {
                log.info("[ChromeEnv] (Linux - locally) 크롬 브라우저 설치를 시작합니다.. ");
                String[] commands = {
                        "bash", "-c",
                        String.join(" && ", new String[]{
                                "mkdir -p " + LOCAL_LINUX_PATH,
                                "cd " + LOCAL_LINUX_PATH,
                                "wget -q https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb",
                                "ar x google-chrome-stable_current_amd64.deb",
                                "tar -xf data.tar.xz"
                        })
                };
                new ProcessBuilder(commands).inheritIO().start().waitFor();
            }
            
        }
    }

    /** linux 에서 sudo 명령어 사용이 가능한지 확인 */
    private boolean canUseSudo(){
        try{
            ProcessBuilder pb = new ProcessBuilder("sudo", "-n", "true");
            Process process = pb.start();
            int exitCode = process.waitFor();
            return exitCode == 0;
        }catch (Exception e){
            return false;
        }
    }
}
