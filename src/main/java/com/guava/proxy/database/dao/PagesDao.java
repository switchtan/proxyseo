package com.guava.proxy.database.dao;

import com.guava.proxy.database.pojo.Pages;

import java.io.Serializable;

public interface PagesDao {
    Serializable save(Pages page);

    Pages findById(Serializable id);
    Pages findByUrl(String id);
}
