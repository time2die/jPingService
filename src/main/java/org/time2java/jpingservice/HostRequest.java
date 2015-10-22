package org.time2java.jpingservice;

import java.util.Date;

/**
 *
 * @author time2java
 */
public class HostRequest {
    Long id ;
    RequestStatus status ;
    String host ;
    Integer port ;
    String path ;
    String reply ;
    Date date ;

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
