package com.demo.MyApp.admin.product.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ProductDto {
    private Long productId;
    private String productNm;
    private double price;
    private double shipping;
    private String author;
    private String content;
    private MultipartFile file;
    private  String fileNm;
    private  String filePath;
    private  String category;
    private String subCategory;
}
