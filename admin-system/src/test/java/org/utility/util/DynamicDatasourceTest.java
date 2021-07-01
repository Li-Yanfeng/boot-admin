package org.utility.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 动态数据源测试 { @DS(value = "dsName") }
 * 1.没有@DS, 默认数据源
 * 2. dsName可以为组名也可以为具体某个库的名称
 *
 * @author Li Yanfeng

 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DynamicDatasourceTest {

    /**
     * 动态数据源 测试
     */
    @Test
    public void testDynamicDatasource() {

    }
}
