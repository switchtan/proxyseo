package com.guava.proxy.database.dao;

import com.guava.proxy.database.pojo.Sites;

import java.io.Serializable;
import java.util.List;

public interface SitesDao {
    Serializable save(Sites employee);

    Sites findById(Serializable id);
    public List<Sites> findAll();
}
