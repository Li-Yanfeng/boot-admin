package org.utility.util;

import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author Li Yanfeng
 */
public class DateUtilsTest {

    @Test
    public void test1() {
        long l = System.currentTimeMillis() / 1000;
        LocalDateTime localDateTime = DateUtils.fromTimeStamp(l);
        System.out.println(DateUtils.localDateTimeFormatMdHms(localDateTime));
    }

    @Test
    public void test2() {
        LocalDateTime now = LocalDateTime.now();
        System.out.println(DateUtils.localDateTimeFormatMdHms(now));
        Date date = DateUtils.toDate(now);
        LocalDateTime localDateTime = DateUtils.toLocalDateTime(date);
        System.out.println(DateUtils.localDateTimeFormatMdHms(localDateTime));
        LocalDateTime localDateTime1 = DateUtils.fromTimeStamp(date.getTime() / 1000);
        System.out.println(DateUtils.localDateTimeFormatMdHms(localDateTime1));
    }
}
