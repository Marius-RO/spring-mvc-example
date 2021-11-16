package com.company.util;

import com.company.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class GlobalCategoriesInterceptor implements HandlerInterceptor {

    private final CategoryService categoryService;

    @Autowired
    public GlobalCategoriesInterceptor(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.getSession().setAttribute("globalCategoriesList", categoryService.getAllCategories());
        return true;
    }
}
