package com.example.ex3.product.repository.search;

import com.example.ex3.product.dto.ProductDTO;
import com.example.ex3.product.dto.ProductListDTO;
import com.example.ex3.product.entity.ProductEntity;
import com.example.ex3.product.entity.QProductEntity;
import com.example.ex3.product.entity.QProductImage;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class ProductSearchImpl extends QuerydslRepositorySupport implements ProductSearch {

    public ProductSearchImpl() {
        super(ProductEntity.class);
    }

    @Override
    public Page<ProductListDTO> list(Pageable pageable) {
        QProductEntity productEntity = QProductEntity.productEntity;
        QProductImage productImage = QProductImage.productImage;

        JPQLQuery<ProductEntity> query = from(productEntity);
        query.leftJoin(productEntity.images, productImage);

        //where productImage.idx = 0
        query.where(productImage.idx.eq(0));

        //Long pno, String pname, int price, String writer, String productImage
        JPQLQuery<ProductListDTO> dtojpqlQuery = query.select(Projections.bean(
            ProductListDTO.class,
            productEntity.pno,
            productEntity.pname,
            productEntity.price,
            productEntity.writer,
            productImage.fileName.as("productImage")
        ));

        this.getQuerydsl().applyPagination(pageable, dtojpqlQuery);

        List<ProductListDTO> dtoList = dtojpqlQuery.fetch();

        long count = dtojpqlQuery.fetchCount();

        return new PageImpl<>(dtoList, pageable, count);
    }

    @Override
    public Page<ProductDTO> listWithAllImages(Pageable pageable) {
        QProductEntity productEntity = QProductEntity.productEntity;
        JPQLQuery<ProductEntity> query = from(productEntity);
        this.getQuerydsl().applyPagination(pageable, query);
        List<ProductEntity> entityList = query.fetch();
        long count = query.fetchCount();
        List<ProductDTO> dtoList = entityList.stream().map(ProductDTO::new).toList();
        return new PageImpl<>(dtoList, pageable, count);
    }

    @Override
    public Page<ProductDTO> listFetchAllImages(Pageable pageable) {
        QProductEntity productEntity = QProductEntity.productEntity;
        QProductImage productImage = QProductImage.productImage;

        JPQLQuery<ProductEntity> query = from(productEntity);
        query.leftJoin(productEntity.images, productImage).fetchJoin();

        this.getQuerydsl().applyPagination(pageable, query);
        List<ProductEntity> entityList = query.fetch();
        List<ProductDTO> dtoList = entityList.stream().map(ProductDTO::new).toList();
        long count = query.fetchCount();

        return new PageImpl<>(dtoList, pageable, count);
    }
}
