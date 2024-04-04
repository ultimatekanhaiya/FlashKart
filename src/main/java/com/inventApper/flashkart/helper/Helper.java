package com.inventApper.flashkart.helper;

import com.inventApper.flashkart.dtos.PageableResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.List;

public class Helper {

    public static <U, V> PageableResponse<V> getPageableResponse(Page<U> page, Class<V> type) {

        List<V> content = page.getContent().stream().map(obj -> new ModelMapper().map(obj, type)).toList();

        PageableResponse<V> response = new PageableResponse<>();
        response.setContent(content);
        response.setPageNumber(page.getNumber());
        response.setPageSize(page.getSize());
        response.setTotalPages(page.getTotalPages());
        response.setTotalElements(page.getTotalElements());
        response.setLastPage(page.isLast());

        return response;

    }
}
