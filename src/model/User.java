/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.Vector;

/**
 *
 * @author nxulu
 */
public class User implements Serializable{
    private String userName;
    private String password;
    private int elo;
    private int win;
    private int id;

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    
    
    public User(String userName, int elo, int win) {
        this.userName = userName;
        this.elo = elo;
        this.win = win;
    }

    public User(String userName, String password, int elo, int win) {
        this.userName = userName;
        this.password = password;
        this.elo = elo;
        this.win = win;
    }
    
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getElo() {
        return elo;
    }

    public void setElo(int elo) {
        this.elo = elo;
    }

    public int getWin() {
        return win;
    }

    public void setWin(int win) {
        this.win = win;
    }

    public Vector<?> toObject() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
     public Object[] toO() {
        return new Object[]{userName, elo};
    }
    
    
}
