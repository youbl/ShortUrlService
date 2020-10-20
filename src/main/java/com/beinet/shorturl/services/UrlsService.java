package com.beinet.shorturl.services;

import com.beinet.shorturl.Util;
import com.beinet.shorturl.repository.UrlsRepository;
import com.beinet.shorturl.repository.entity.Urls;
import lombok.Synchronized;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UrlsService {
    private final UrlsRepository urlsRepository;
    // Value注解不支持actuator接口刷新，改用env
    private final Environment environment;

    public UrlsService(UrlsRepository urlsRepository, Environment environment) {
        this.urlsRepository = urlsRepository;
        this.environment = environment;
    }

    /**
     * 当前系统是否使用随机短码 提供服务
     *
     * @return 是否
     */
    public boolean useCode() {
        String type = environment.getProperty("beinet.cn.url-code.type");
        if (StringUtils.isEmpty(type))
            type = "id";
        return type.equalsIgnoreCase("code");
    }

    /**
     * 根据短码，查找url返回
     *
     * @param id 短码
     * @return url
     * @throws Exception 异常
     */
    public String getUrl(String id) throws Exception {
        Urls record = useCode() ? getUrlByCode(id) : getUrlById(id);
        if (record == null)
            throw new Exception("id: " + id + " was not found.");

        String url = record.getUrl();
        if (!Util.isUrl(url))
            throw new Exception("configed url is invalid: " + url);
        return url;
    }

    private Urls getUrlById(String id) throws Exception {
        Long dbId = Util.ConvertToNum(id);
        return urlsRepository.findById(dbId).orElse(null);
    }

    private Urls getUrlByCode(String code) throws Exception {
        return urlsRepository.findByCode(code);
    }

    /**
     * 返回全部数据
     *
     * @return 数据
     */
    public List<Urls> getAll() throws Exception {
        List<Urls> ret = urlsRepository.findAll();
        for (Urls item : ret) {
            String code;
            if (useCode()) {
                code = item.getCode();
                if (StringUtils.isEmpty(code)) {
                    code = Util.ConvertToStr(item.getId()); // 兼容从id切换到code的数据
                }
            } else {
                code = Util.ConvertToStr(item.getId());
            }
            item.setCode(code);
        }
        return ret;
    }

    /**
     * 添加单个url数据
     *
     * @param url  网址
     * @param desc 描述
     * @param id   如果要指定id
     * @param ip   用户ip
     * @return 短码
     * @throws Exception 异常
     */
    public String addUrl(String url, String desc, Long id, String ip) throws Exception {
        // 用于Demo站点，不允许创建超过1万行
        Urls record = urlsRepository.findTopByOrderByIdDesc();
        if (record != null && record.getId() > 10000)
            throw new Exception("Demo code only add 10000 record.");

        if (!Util.isUrl(url))
            throw new Exception("argument url is invalid: " + url);
        url = url.trim();

        // todo: 验证url是否有效，返回的响应码大于399时，报错

        // 验证url是否已经存在，存在时，不新增。注：url列未建索引，数据大了可能性能低下
        // if(_repository.existsByUrl(url))
        //    return "-1";

        if (desc == null)
            desc = "";

        record = new Urls();
        if (id != null && id > 0)
            record.setId(id);
        record.setUrl(url);
        record.setDesc(desc.trim());
        record.setAddtime(LocalDateTime.now());
        record.setAddip(ip);

        record = saveRecord(record);
        return Util.ConvertToStr(record.getId());
    }

    @Synchronized
    // Sqlite增删改，要串行
    private Urls saveRecord(Urls record) {
        if (useCode()) {
            record.setCode(generateCode());
        }
        return urlsRepository.save(record);
    }

    private String generateCode() {
        String code = Util.generateCode(6);
        while (urlsRepository.existsByCode(code)) {
            code = Util.generateCode(6);
        }
        return code;
    }
}
