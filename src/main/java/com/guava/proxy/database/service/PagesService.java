package com.guava.proxy.database.service;


import com.guava.proxy.database.pojo.Pages;
import org.springframework.transaction.annotation.Transactional;

public interface PagesService {
    Pages getEmployee(long id);

    @Transactional(readOnly = false)
    void addNewEmployee(Pages employee);
    Boolean getPageByUrl(String url);
}
