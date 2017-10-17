package com.etongdai.springbootdemo.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.etongdai.springbootdemo.entity.Dict;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * 服务测试
 *
 * @author hotleave
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class DictServiceTest {
  @Autowired
  private DictService dictService;

  @Test
  public void getDictItems() throws Exception {
    List<Dict> dictList = dictService.getDictItems(1L);

    assertNotNull(dictList);
    assertEquals(2, dictList.size());
  }

}