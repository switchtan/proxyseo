package com.guava.proxy.database.pojo;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
public class Sites {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @GenericGenerator(name = "persistenceGenerator", strategy = "increment")
  private long id;

  @Column
  private String domain;
  @Column
  private int templateid;


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public String getDomain() {
    return domain;
  }

  public void setDomain(String domain) {
    this.domain = domain;
  }


  public int getTemplateid() {
    return templateid;
  }

  public void setTemplateid(int templateid) {
    this.templateid = templateid;
  }

}
