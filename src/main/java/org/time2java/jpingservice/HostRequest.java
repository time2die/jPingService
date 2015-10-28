package org.time2java.jpingservice;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author time2java
 */
@Entity
public class HostRequest implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private RequestStatus status = RequestStatus.NEW;
    private String host ;
    private Integer port ;
    private String path ;
    private String reply ;
    private Date date;
    private int code;

    @Override
    public String toString() {
        if( reply != null && reply.length() > 50){
            reply = new String(reply.substring(0, 50)) ;
        }
        
        return "HostRequest{" + "id=" + id +", code=" + code + ", status=" + status + ", host=" + host + ", port=" + port + ", path=" + path + ", reply=" + reply + ", date=" + date +'}';
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public HostRequest() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }

    public String getPath() {
        return path;
    }

    public String getReply() {
        return reply;
    }

    public Date getDate() {
        return date;
    }
}
