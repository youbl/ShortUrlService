package com.beinet.shorturl.controllers;

import com.beinet.shorturl.Util;
import com.beinet.shorturl.repository.entity.Urls;
import com.beinet.shorturl.services.UrlsService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public void getUrl(HttpServletRequest request, HttpServletResponse response, String id) throws Exception {
        if (id == null)
            id = Util.getId(request);

        if (id == null || id.length() <= 0) {
            response.getOutputStream().write("id can't be empty.".getBytes());
            // return "id can't be empty.";
            return;
        }

        String url = urlsService.getUrl(id);

        // 302跳转
        response.sendRedirect(url);
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

    /**
     * 短码使用自增还是随机串
     *
     * @return 是否
     */
    @GetMapping("useCode")
    public boolean isUseCode() {
        return urlsService.useCode();
    }

    @GetMapping("qrcode")
    public String getQrcode(String url) throws IOException, WriterException {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        //编码
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        //边框距
        hints.put(EncodeHintType.MARGIN, 0);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix img = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, 400, 400, hints);
        return getImageBase64(img);
    }

    /**
     * 图片转base64
     *
     * @param img 图片
     * @return 字符串
     * @throws IOException exp
     */
    public String getImageBase64(BitMatrix img) throws IOException {
        // BufferedImage img = getImage(text);
        byte[] bytes;
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
            MatrixToImageWriter.writeToStream(img, "png", stream);
            bytes = stream.toByteArray();
        }
        BASE64Encoder encoder = new BASE64Encoder();
        String png_base64 = encoder.encodeBuffer(bytes).trim();//转换成base64串
        return png_base64.replaceAll("[\\r\\n]", "");
    }
}
