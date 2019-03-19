package com.guava.proxy.database.pojo;


import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity

public class Pages {
  @Id
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  private long id;

  @Column
  private String title;

  @Lob
  @Basic(fetch = FetchType.LAZY)
  @Type(type="text")
  @Column
  private String content;

  @Column
  private int siteId;
  @Column
  private int catalog;

  public String getFromurl() {
    return fromurl;
  }

  public void setFromurl(String fromurl) {
    this.fromurl = fromurl;
  }

  @Column
  private String fromurl;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }


  public long getSiteId() {
    return siteId;
  }

  public void setSiteId(int siteId) {
    this.siteId = siteId;
  }


  public String getTitle() {
    return title;
  }

  public void setTitle(String titel) {
    this.title = titel;
  }


  public int getCatalog() {
    return catalog;
  }

  public void setCatalog(int catalog) {
    this.catalog = catalog;
  }

}
