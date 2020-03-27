package com.beinet.shorturl.controllers;

import com.beinet.shorturl.repository.entity.urls;
import com.beinet.shorturl.repository.urlsRepository;
import com.beinet.shorturl.util;
import lombok.Synchronized;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
public class defaultController {

    private final urlsRepository _repository;

    public defaultController(urlsRepository _repository) {
        this._repository = _repository;
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
            id = util.getId(request);

        if (id == null || id.length() <= 0)
            return "id can't be empty.";

        Long dbId = util.ConvertToNum(id);
        Optional<urls> record = _repository.findById(dbId);
        if (!record.isPresent())
            return "id: " + id + " was not found.";

        String url = record.get().getUrl();
        if (!util.isUrl(url))
            return "configed url is invalid: " + url;

        // 302跳转
        response.sendRedirect(url);
        return url;
    }

    /**
     * 添加一个短网址
     * @param request 请求上下文
     * @param id 大于0表示更新
     * @param url 要保存的url
     * @param desc 要保存的说明信息
     * @return 得到的id
     * @throws Exception 可能的转换异常
     */
    @PostMapping
    public String addUrl(HttpServletRequest request, Long id, String url, String desc) throws Exception {
        // 用于Demo站点，不允许创建超过1万行
        urls record = _repository.findTopByOrderByIdDesc();
        if(record != null && record.getId() > 10000)
            throw new Exception("Demo code only add 10000 record.");

        if (!util.isUrl(url))
            throw new Exception("argument url is invalid: " + url);
         url = url.trim();

        // todo: 验证url是否有效，返回的响应码大于399时，报错

        // 验证url是否已经存在，存在时，不新增。注：url列未建索引，数据大了可能性能低下
        // if(_repository.existsByUrl(url))
        //    return "-1";

        if (desc == null)
            desc = "";

        record = new urls();
        if(id > 0)
            record.setId(id);
        record.setUrl(url);
        record.setDesc(desc.trim());
        record.setAddtime(LocalDateTime.now());
        record.setAddip(util.getClientIP(request));

        record = saveRecord(record);
        String ret = util.ConvertToStr(record.getId());
        return ret;
    }

    @GetMapping("all")
    public List<urls> getAll() {
        return _repository.findAll();
    }

    @Synchronized // Sqlite增删改，要串行
    urls saveRecord(urls record) {
        return _repository.save(record);
    }
}
