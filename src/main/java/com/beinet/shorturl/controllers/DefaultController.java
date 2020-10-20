package com.beinet.shorturl.controllers;

import com.beinet.shorturl.Util;
import com.beinet.shorturl.repository.entity.Urls;
import com.beinet.shorturl.services.UrlsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
public class DefaultController {

    private final UrlsService urlsService;

    public DefaultController(UrlsService urlsService) {
        this.urlsService = urlsService;
    }

    /**
     * 根据url里的字符串，获取地址后302跳转，url格式参考： http://127.0.0.1?abcd，根据abcd读取配置跳转
     *
     * @param request  请求上下文
     * @param response 响应上下文
     * @param id       url里的id=xx
     * @return 无
     * @throws Exception 可能的转换异常
     */
    @GetMapping("")
    public String getUrl(HttpServletRequest request, HttpServletResponse response, String id) throws Exception {
        if (id == null)
            id = Util.getId(request);

        if (id == null || id.length() <= 0)
            return "id can't be empty.";

        String url = urlsService.getUrl(id);

        // 302跳转
        response.sendRedirect(url);
        return url;
    }

    /**
     * 添加一个短网址
     *
     * @param request 请求上下文
     * @param id      大于0表示更新
     * @param url     要保存的url
     * @param desc    要保存的说明信息
     * @return 得到的id
     * @throws Exception 可能的转换异常
     */
    @PostMapping
    public String addUrl(HttpServletRequest request, Long id, String url, String desc) throws Exception {
        return urlsService.addUrl(url, desc, id, Util.getClientIP(request));
    }

    @GetMapping("all")
    public List<Urls> getAll() throws Exception {
        return urlsService.getAll();
    }

}
