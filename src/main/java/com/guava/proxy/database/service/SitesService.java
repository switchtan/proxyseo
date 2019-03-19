package com.guava.proxy.database.service;

import com.guava.proxy.database.pojo.Sites;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SitesService {
    Sites get(int id);

    @Transactional(readOnly = false)
    void add(Sites employee);

    List<Sites> getAll();
    public int getOneSiteId();
}
