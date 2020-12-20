package com.iceberry.keysave;

import org.litepal.crud.DataSupport;

public class Key extends DataSupport {
    private String nickname;
    private String sort;
    private String account;
    private String iconPath;
    private String password;
    //private int key_id;

    public Key() {
    }

    public Key(String nickname, String sort, String account, String iconPath, String password) {
        this.nickname = nickname;
        this.sort = sort;
        this.account = account;
        this.iconPath = iconPath;
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
