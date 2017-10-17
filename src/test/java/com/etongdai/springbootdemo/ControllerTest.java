package com.etongdai.springbootdemo;

import com.etongdai.springbootdemo.entity.Dict;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * 启动Web容器测试Controller
 *
 * @author hotleave
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ControllerTest {
  @Autowired
  private TestRestTemplate restTemplate;

  /**
   * 当前所使用的端口号
   */
  @LocalServerPort
  private int port;

  /**
   * exchange
   */
  @Test
  public void getDictItems() throws Exception {
    ParameterizedTypeReference<List<Dict>> responseType = new ParameterizedTypeReference<List<Dict>>() {
    };

    List<Dict> dictList = restTemplate.exchange("/api/v1/dicts/{id}/items", HttpMethod.GET, null, responseType, 1).getBody();

    Assert.assertNotNull(dictList);
    Assert.assertEquals(2, dictList.size());
  }

  /**
   * getForObject
   */
  @Test
  public void findOne() throws Exception {
    Dict dict = restTemplate.getForObject("/api/v1/dicts/{id}", Dict.class, -1);

    Assert.assertNotNull(dict);
    Assert.assertEquals("gender", dict.getCode());
  }

}
