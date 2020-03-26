package com.beinet.shorturl;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

@SpringBootTest
class numTests {
    @Test
    void TestTransfer() throws Exception {
        TestStrToNum("0000001", 1);
        TestStrToNum("0000001002", 238330);
        TestStrToNum("0000zzzzzzz", 10000000);

        for (int i = 0; i < 10; i++) {
            TestNumToStr(i, String.valueOf(i));
        }

        TestNumToStr(61, "z");
        TestNumToStr(62, "10");
        TestNumToStr(63, "11");
        TestNumToStr(122, "1y");
        TestNumToStr(123, "1z");
        TestNumToStr(124, "20");
        TestNumToStr(125, "21");

        TestNumToStr(3842, "zy");
        TestNumToStr(3843, "zz");
        TestNumToStr(3844, "100");
        TestNumToStr(3845, "101");
        TestNumToStr(3846, "102");

        TestNumToStr(238326, "zzy");
        TestNumToStr(238327, "zzz");
        TestNumToStr(238328, "1000");
        TestNumToStr(238329, "1001");
        TestNumToStr(238330, "1002");
    }


    void TestNumToStr(long num, String strNum) throws Exception {
        String str1 = util.ConvertToStr(num);
        //var str2 = NumHelper.ConvertToStr2(num);
        Assert.isTrue(strNum.equals(str1), "");
        //Assert.AreEqual(str2, str1);

        long numRet = util.ConvertToNum(str1);
        Assert.isTrue(numRet == num, "");
    }

    void TestStrToNum(String strNum, long num) throws Exception {
        long numOk = util.ConvertToNum(strNum);
        Assert.isTrue(num == numOk);
    }
}
