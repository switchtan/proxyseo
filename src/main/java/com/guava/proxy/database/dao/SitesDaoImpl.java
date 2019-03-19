package com.guava.proxy.database.dao;

import com.guava.proxy.database.pojo.Sites;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public class SitesDaoImpl implements SitesDao {

    @Autowired
    private SessionFactory sessionFactory;

    protected Session getSession() {
        return this.sessionFactory.getCurrentSession();
    }

    @Override
    public Serializable save(Sites site) {
        return getSession().save(site);
    }

    @Override
    public Sites findById(final Serializable id) {
        return getSession().get(Sites.class, id);
    }

    @Override
    public List<Sites> findAll() {
        return null;
    }
}