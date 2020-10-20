package com.beinet.shorturl.repository;

import com.beinet.shorturl.repository.entity.Urls;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlsRepository extends JpaRepository<Urls, Long> {
    /**
     * 指定的url是否已经存在
     *
     * @param url
     * @return true false
     */
    boolean existsByUrl(String url);

    /**
     * 获取最新的一条记录
     *
     * @return 最新记录
     */
    Urls findTopByOrderByIdDesc();

    /**
     * 指定的短码是否存在
     *
     * @param code 短码
     * @return true false
     */
    boolean existsByCode(String code);

    /**
     * 根据短码查找记录
     *
     * @param code 短码
     * @return 记录
     */
    Urls findByCode(String code);
}
