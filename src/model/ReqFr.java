/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.sql.Date;

/**
 *
 * @author nxulu
 */
public class ReqFr implements Serializable {

    private String sendName;
    private Date date;
    private int id;
    private int idSend;
    private int idRev;

    public ReqFr(String sendName, Date date, int id, int idSend, int idRev) {
        this.sendName = sendName;
        this.date = date;
        this.id = id;
        this.idSend = idSend;
        this.idRev = idRev;
    }

    
    
    public int getIdSend() {
        return idSend;
    }

    public void setIdSend(int idSend) {
        this.idSend = idSend;
    }

    public int getIdRev() {
        return idRev;
    }

    public void setIdRev(int idRev) {
        this.idRev = idRev;
    }
    
    

    public ReqFr(String sendName, Date date, int id) {
        this.sendName = sendName;
        this.date = date;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ReqFr(String sendName, Date date) {
        this.sendName = sendName;
        this.date = date;
    }

    public String getSendName() {
        return sendName;
    }

    public void setSendName(String sendName) {
        this.sendName = sendName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Object[] toO() {
        return new Object[]{id, sendName, date};
    }
}
