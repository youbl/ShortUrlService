package com.beinet.shorturl.repository.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@Entity
public class Urls {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String url;
    private String desc;
    private LocalDateTime addtime;
    private String addip;

    /**
     * 支持按id，也支持短码跳转
     */
    private String code;
}
