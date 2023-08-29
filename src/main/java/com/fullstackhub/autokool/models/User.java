package com.fullstackhub.autokool.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class User {

    private int id;
    private String name;
    private String password;

    private List<Integer> resultList = new ArrayList<>();

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public User(int id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Integer> getResultList() {
        return Collections.unmodifiableList(resultList);
    }

    public void setResultList(List<Integer> resultList) {
        this.resultList = resultList;
    }
}
