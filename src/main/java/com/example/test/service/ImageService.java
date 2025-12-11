package com.example.test.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public class ImageService {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String supabaseKey;

    @Value("${supabase.bucket}")
    private String bucketName;

    public String uploadImage(MultipartFile file) {
        try {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            String url = supabaseUrl + "/storage/v1/object/" + bucketName + "/" + fileName;
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + supabaseKey);

            headers.setContentType(MediaType.parseMediaType(file.getContentType()));

            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<byte[]> requestEntity = new HttpEntity<>(file.getBytes(), headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return supabaseUrl + "/storage/v1/object/public/" + bucketName + "/" + fileName;
            } else {
                throw new RuntimeException("이미지 업로드 실패 : " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("이미지 업로드 실패 : " + e);
        }

    }

    public void deleteImage(String imageUrl) {
        if(imageUrl == null) { return; }

        try {
            String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);

            String url = supabaseUrl + "/storage/v1/object/" + bucketName + "/" + fileName;
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + supabaseKey);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.exchange(url, HttpMethod.DELETE, new HttpEntity<>(headers), String.class);
            System.out.println("이미지 삭제 완료 : " + fileName);
        } catch (Exception e) {
            System.err.println("이미지 삭제 실패 (응 무시해) : " + e.getMessage());
        }
    }
}
