package com.fullstackhub.autokool.models;

import javafx.beans.property.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class User {

    private int id;
    private String name;
    private String password;
    private Role role;
    private int total;
    private int fail;
    private int pass;
    private int incomplete;

    public enum Role{
        USER,
        ADMIN
    }

    private List<Integer> resultList = new ArrayList<>();

    public User(String name, String password) {
        this.name = name;
        this.password = password;
        this.role = Role.USER;
    }

    public User(int id, String name, String password, String role, int total, int fail, int pass, int incomplete) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.role = (role.equals("USER")?Role.USER:Role.ADMIN);
        this.total = total;
        this.fail = fail;
        this.pass = pass;
        this.incomplete = incomplete;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Integer> getResultList() {
        return Collections.unmodifiableList(resultList);
    }

    public void setResultList(List<Integer> resultList) {
        this.resultList = resultList;
    }

    public int getTotal() {
        return total;
    }

    public int getFail() {
        return fail;
    }

    public int getPass() {
        return pass;
    }

    public int getIncomplete() {
        return incomplete;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setFail(int fail) {
        this.fail = fail;
    }

    public void setPass(int pass) {
        this.pass = pass;
    }

    public void setIncomplete(int incomplete) {
        this.incomplete = incomplete;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", total=" + total +
                ", fail=" + fail +
                ", pass=" + pass +
                ", incomplete=" + incomplete +
                '}';
    }

    // Values for Java FX Table view

    public IntegerProperty getIdProperty() {
        return new SimpleIntegerProperty(id);
    }

    public StringProperty getUsernameProperty() {
        return new SimpleStringProperty(name);
    }

    public StringProperty getPasswordProperty() {
        return new SimpleStringProperty(password);
    }

    public SimpleObjectProperty<Role> getRoleProperty() {
        return new SimpleObjectProperty<Role>(role);
    }

    public IntegerProperty getTotalProperty() {
        return new SimpleIntegerProperty(total);
    }

    public IntegerProperty getPassProperty() {
        return new SimpleIntegerProperty(pass);
    }

    public IntegerProperty getFailProperty() {
        return new SimpleIntegerProperty(fail);
    }

    public IntegerProperty getIncompleteProperty() {
        return new SimpleIntegerProperty(incomplete);
    }

}
