package com.etongdai.springbootdemo;

import com.etongdai.springbootdemo.entity.Dict;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author hotleave
 */
@Ignore("该测试应在Client端运行")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureStubRunner(ids = "com.etongdai:springbootdemo:+:stubs:6666", workOffline = true)
public class DictClientStubTest {
  @Autowired
  private RestTemplateBuilder builder;

  @Test
  public void getDict() throws Exception {
    Dict dict = builder.build().getForObject("http://localhost:6666/api/v1/dicts/{id}", Dict.class, 1L);
    Assert.assertNotNull(dict);
    Assert.assertEquals(Long.valueOf(1L), dict.getId());
  }
}
