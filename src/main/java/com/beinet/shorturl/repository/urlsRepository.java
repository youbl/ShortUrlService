package com.beinet.shorturl.repository;

import com.beinet.shorturl.repository.entity.urls;
import org.springframework.data.jpa.repository.JpaRepository;

public interface urlsRepository extends JpaRepository<urls, Long> {
    /**
     * 指定的url是否已经存在
     * @param url
     * @return
     */
    boolean existsByUrl(String url);

    /**
     * 获取最新的一条记录
     * @return
     */
    urls findTopByOrderByIdDesc();
}
