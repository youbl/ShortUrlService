package com.beinet.shorturl.repository;

import com.beinet.shorturl.repository.entity.urls;
import org.springframework.data.jpa.repository.JpaRepository;

public interface urlsRepository extends JpaRepository<urls, Long> {
    boolean existsByUrl(String url);
}
