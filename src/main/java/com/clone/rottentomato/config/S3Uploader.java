package com.clone.rottentomato.config;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Uploader {

//    private final AmazonS3 S3Client;
    private final AmazonS3 s3Client;


    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    @PostConstruct
    public AmazonS3Client awsS3Client() {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
        return (AmazonS3Client) AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();
    }


    //    이미지 업로드
    public String uploadAndResizeImage(MultipartFile file) throws IOException {
        // 이미지 리사이징
        try (InputStream inputStream = file.getInputStream()) {
            BufferedImage resizedImage = resizeImage(inputStream);
            // S3에 업로드
            String imageUrl = uploadImageToS3(resizedImage, file.getSize());
            return imageUrl;
        } catch (IOException e) {
            log.error("이미지 업로드 및 리사이징 중 오류 발생: {}", e.getMessage());
            throw new IllegalArgumentException("이미지 리사이징 및 업로드 중 오류 발생");
        }
    }


    //    PDF파일
    public List<String> uploadPDF(List<MultipartFile> pdfFiles) {
        List<String> pdfUrls = new ArrayList<>();
        for (MultipartFile pdfFile : pdfFiles) {
            String pdfUrl = uploadPDFToS3(pdfFile);
            pdfUrls.add(pdfUrl);
        }
        return pdfUrls;
    }


    //    pdf 업로드 로직
    private String uploadPDFToS3(MultipartFile pdfFile) {
        String fileName = createFileName(pdfFile.getOriginalFilename());
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(pdfFile.getSize());
        objectMetadata.setContentType(pdfFile.getContentType());

        try (InputStream inputStream = pdfFile.getInputStream()) {
            s3Client.putObject(new PutObjectRequest(bucket , fileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            return s3Client.getUrl(bucket , fileName).toString();
        } catch (IOException e) {
            log.error("S3파일 저장 중 오류 발생: {}",e.getMessage());
            throw new IllegalArgumentException("S3파일 저장 중 예외 발생");
        }
    }


    public void deleteFile(String url) {
        String key = extractKeyFromUrl(url);
        try {
            s3Client.deleteObject(bucket, key);
        } catch (AmazonServiceException e) {
            log.error("S3 삭제 중 오류 발생: {}", e.getMessage());
            throw new IllegalArgumentException("S3파일 삭제 중 예외 발생");
        }
    }

    private String extractKeyFromUrl(String url) {
        // URL에서 파일 이름 추출 (마지막 슬래시 이후의 문자열)
        int lastSlashIndex = url.lastIndexOf("/");
        if (lastSlashIndex != -1 && lastSlashIndex < url.length() - 1) {
            return url.substring(lastSlashIndex + 1);
        } else {
            throw new IllegalArgumentException("Invalid URL format: " + url);
        }
    }


    // 이미지파일명 중복 방지
    private String createFileName(String fileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    // 파일 유효성 검사
    private String getFileExtension(String fileName) {
        if (fileName.length() == 0) {
            throw new IllegalArgumentException("S3파일 저장 중 예외 발생");
        }
        ArrayList<String> fileValidate = new ArrayList<>();
        fileValidate.add(".jpg");
        fileValidate.add(".jpeg");
        fileValidate.add(".png");
        fileValidate.add(".jfif");
        fileValidate.add(".jif");
        fileValidate.add(".jpe");
        fileValidate.add(".JPG");
        fileValidate.add(".JPEG");
        fileValidate.add(".PNG");
        fileValidate.add(".JFIF");
        fileValidate.add(".JIF");
        fileValidate.add(".JPE");
        fileValidate.add(".PDF");
        fileValidate.add(".pdf");
        String idxFileName = fileName.substring(fileName.lastIndexOf("."));
        if (!fileValidate.contains(idxFileName)) {
            throw new IllegalArgumentException("잘못된 포멧입니다.");
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }

    public String uploadMypage(MultipartFile file) {
        String imgUrlList;
        String fileName = createFileName(file.getOriginalFilename());
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());

        try (InputStream inputStream = file.getInputStream()) {
            s3Client.putObject(new PutObjectRequest(bucket + "/post/image", fileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            imgUrlList = (s3Client.getUrl(bucket + "/post/image", fileName).toString());
        } catch (IOException e) {
            throw new IllegalArgumentException("S3파일 저장 중 예외 발생");
        }

        return imgUrlList;
    }


    private BufferedImage resizeImage(InputStream inputStream) throws IOException {
        // 이미지 리사이징 로직을 구현합니다.
        // 예시로, 이미지를 400px로 리사이징하는 코드를 작성합니다.
        BufferedImage originalImage = ImageIO.read(inputStream);
        int newWidth = 400;
        int newHeight = (originalImage.getHeight() * newWidth) / originalImage.getWidth();
        Image resizedImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        BufferedImage bufferedResizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        bufferedResizedImage.getGraphics().drawImage(resizedImage, 0, 0, null);
        return bufferedResizedImage;
    }

    private String uploadImageToS3(BufferedImage image, long contentLength) throws IOException {
        // S3에 이미지를 업로드하는 로직을 구현합니다.
        // 예시로, 파일 이름을 랜덤으로 생성하여 업로드하는 코드를 작성합니다.
        String fileName = UUID.randomUUID().toString() + ".jpg";
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            ImageIO.write(image, "jpg", os);
            try (InputStream is = new ByteArrayInputStream(os.toByteArray())) {
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(contentLength);
                s3Client.putObject(new PutObjectRequest(bucket, fileName, is, new ObjectMetadata()));
            }
        } catch (IOException e) {
            log.error("이미지 업로드 중 오류 발생: {}", e.getMessage());
            throw new IllegalArgumentException("이미지 리사이징 및 업로드 중 오류 발생");
        }
        return s3Client.getUrl(bucket, fileName).toString();
    }

}