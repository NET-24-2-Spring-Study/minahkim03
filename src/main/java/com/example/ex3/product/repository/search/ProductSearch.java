package com.example.ex3.product.repository.search;

import com.example.ex3.product.dto.ProductDTO;
import com.example.ex3.product.dto.ProductListDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

public interface ProductSearch {
    Page<ProductListDTO> list(Pageable pageable);
    Page<ProductDTO> listWithAllImages(Pageable pageable);
    Page<ProductDTO> listFetchAllImages(Pageable pageable);
}
