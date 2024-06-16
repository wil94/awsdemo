package com.example.ai.awsdemo.model.entity;

public class QueryEntity {

    int id;
    String name;
    String alias;
    String value;
    String service;

    public QueryEntity(int id, String name, String alias, String value, String service) {
        this.id = id;
        this.name = name;
        this.alias = alias;
        this.value = value;
        this.service = service;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
}
