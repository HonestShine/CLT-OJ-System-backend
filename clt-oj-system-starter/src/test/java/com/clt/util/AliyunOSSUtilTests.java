package com.clt.util;

import com.clt.utils.AliyunOSSUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AliyunOSSUtilTests {

    @Autowired
    private AliyunOSSUtil aliyunOSSUtil;

    @Test
    public void testDelete() {
        aliyunOSSUtil.delete("2026/04/12a9acba-c50c-4574-bc71-5b564f6a3c3c.jpg");
    }

    @Test
    public void testSubString() {
        String url = "https://clt-oj-avatar-store.oss-cn-chengdu.aliyuncs.com/2026/04/12a9acba-c50c-4574-bc71-5b564f6a3c3c.jpg";
        System.out.println(url.substring(url.indexOf('/', "https://".length())));
        int schemeEndIndex = url.indexOf("://") + 3;  // 找到 "://" 后的位置 +3 = 协议结束后的位置
        int slashIndex = url.indexOf('/', schemeEndIndex);  // 找到域名后的第一个 '/'
        String objectKey = url.substring(slashIndex + 1);  // 取 '/' 后面的内容
        System.out.println(objectKey);
    }
}
