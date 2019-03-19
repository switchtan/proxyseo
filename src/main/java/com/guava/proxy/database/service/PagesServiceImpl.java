package com.guava.proxy.database.service;

import com.guava.proxy.database.dao.PagesDao;
import com.guava.proxy.database.pojo.Pages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PagesServiceImpl implements PagesService {

    @Autowired
    PagesDao employeeDao;

    @Override
    public Pages getEmployee(long id) {
        return employeeDao.findById(id);
    }

    @Override
    @Transactional(readOnly = false)
    public void addNewEmployee(Pages employee) {
        Long id = (Long) employeeDao.save(employee);
    }

    @Override
    public Boolean getPageByUrl(String url) {
        if(employeeDao.findByUrl(url)==null)return true;
        return false;
    }
}