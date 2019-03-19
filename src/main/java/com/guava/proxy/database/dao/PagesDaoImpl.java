package com.guava.proxy.database.dao;

import com.guava.proxy.database.pojo.Pages;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public class PagesDaoImpl implements PagesDao {

    @Autowired
    private SessionFactory sessionFactory;

    protected Session getSession() {
        return this.sessionFactory.getCurrentSession();
    }

    @Autowired
    HibernateTemplate hibernateTemplate;

    @Override
    public Serializable save(Pages page) {
        try {
//            List<Pages> page_list= (List<Pages>) hibernateTemplate.find("from Pages u where u.title=?",page.getTitle());
//            if(page_list.size()==0) {
//                System.out.println("add page:"+page.getTitle());
                return hibernateTemplate.save(page);

//            }else{
//                System.out.println("found:"+page.getTitle());
//            }
        }catch (DataIntegrityViolationException e){
            e.printStackTrace();
        }
        return null;

        //return getSession().save(page);
    }

    @Override
    public Pages findById(final Serializable id) {
        return getSession().get(Pages.class, id);
    }

    @Override
    public Pages findByUrl(String id) {
        List<Pages> page_list= (List<Pages>) hibernateTemplate.find("from Pages u where u.fromurl=?",id);
        if(page_list.size()>0){
            return page_list.get(0);
        }else {
            return null;
        }
    }
}