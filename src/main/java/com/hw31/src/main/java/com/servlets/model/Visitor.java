package com.servlets.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Visitor {

    private String ip;
    private String userAgent;
    private LocalDateTime created;

    public Visitor(String ip, String userAgent, LocalDateTime created) {
        this.ip = ip;
        this.userAgent = userAgent;
        this.created = created;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Visitor{");
        sb.append("id='").append(ip).append('\'');
        sb.append(", userAgent='").append(userAgent).append('\'');
        sb.append(", created=").append(created);
        sb.append('}');
        return sb.toString();
    }
}
