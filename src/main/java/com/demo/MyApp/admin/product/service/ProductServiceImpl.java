package com.demo.MyApp.admin.product.service;

import com.demo.MyApp.common.entity.Category;
import com.demo.MyApp.common.utill.service.UtillServiceImpl;
import com.demo.MyApp.admin.product.dto.ProductDto;
import com.demo.MyApp.admin.product.entity.Product;
import com.demo.MyApp.admin.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor //생성자 주입코드없이 의존성주입
public class ProductServiceImpl implements ProductService {

    @Autowired
    private final ProductRepository productRepository;

    @Autowired
    private UtillServiceImpl utillService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Transactional
    @Override
    public void insertProduct(ProductDto productDto, MultipartFile file) throws Exception {
        // 파일이 저장될 이미지 경로
//        String UPLOAD_DIR = "C:\\localProject\\MyApp\\src\\main\\resources\\frontend\\public\\upload\\";
        String UPLOAD_DIR = uploadDir;

        // 파일 null 처리
        if (file != null && !file.isEmpty()) {
            // 이미지 업로드 공통 메소드
            utillService.imgUpload(file);
            // 값셋팅
            productDto.setFileNm(file.getOriginalFilename());
            productDto.setFilePath(UPLOAD_DIR + file.getOriginalFilename());
        }

        // DTO를 Entity로 변환하여 save() 메소드에 담아 데이터 삽입
        Product product = Product.toEntity(productDto);
        productRepository.save(product);
    }

    @Override
    public Page<Product> productList(int page, int size) throws Exception {
        return productRepository.findAll(PageRequest.of(page, size));
    }

    @Transactional
    @Override
    public ProductDto productDetail(Long id) throws Exception {
        Optional<Product> product = productRepository.findById(id);
        ProductDto productDto = ProductDto.builder()
                .id(product.get().getId())
                .productNm(product.get().getProductNm())
                .price(product.get().getPrice())
                .content(product.get().getContent())
                .author(product.get().getAuthor())
                .fileNm(product.get().getFileNm())
                .filePath(product.get().getFilePath())
                .build();

        return productDto;
    }

    @Transactional
    @Override
    public void updateProduct(ProductDto productDto, Long id, MultipartFile file) throws Exception {
       Product product = productRepository.findById(id).orElseThrow();

        // 파일이 저장될 이미지 경로
//        String UPLOAD_DIR = "C:\\localProject\\MyApp\\src\\main\\resources\\frontend\\public\\upload\\";
        String UPLOAD_DIR = uploadDir + "\\upload\\";

        // 파일 null 처리
        if (file != null && !file.isEmpty()) {
            // 이미지 업로드 공통 메소드
            utillService.imgUpload(file);
            // 값셋팅
            product.setFileNm(file.getOriginalFilename());
            product.setFilePath(UPLOAD_DIR + file.getOriginalFilename());
        }
        // DTO를 통해 필드를 업데이트 (값이 null이 아닐 때만)
        if (productDto.getProductNm() != null) {
            product.setProductNm(productDto.getProductNm());
        }
        if (productDto.getPrice() > 0) {
            product.setPrice(productDto.getPrice());
        }
        if (productDto.getContent() != null) {
            product.setContent(productDto.getContent());
        }
        if (productDto.getCategory() != null) {
            product.setCategory(productDto.getCategory());
        }
        if (productDto.getAuthor() != null) {
            product.setAuthor(productDto.getAuthor());
        }
        
        productRepository.save(product);
    }

    @Transactional
    @Override
    public void deleteProduct(Long id) throws Exception {
        productRepository.deleteById(id);
    }
}
