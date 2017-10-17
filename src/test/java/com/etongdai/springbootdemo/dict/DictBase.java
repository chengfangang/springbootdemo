package com.etongdai.springbootdemo.dict;

import static org.mockito.BDDMockito.given;

import com.etongdai.springbootdemo.SpringbootdemoApplication;
import com.etongdai.springbootdemo.api.rest.DictApi;
import com.etongdai.springbootdemo.entity.Dict;
import com.etongdai.springbootdemo.service.DictService;
import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.internal.matchers.GreaterThan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

/**
 * @author hotleave
 */
@RunWith(SpringRunner.class)
@WebMvcTest(DictApi.class)
@ContextConfiguration(classes = SpringbootdemoApplication.class)
public class DictBase {
  @MockBean
  private DictService dictService;

  @Autowired
  private DictApi dictApi;

  @Autowired
  private MockMvc mockMvc;

  @Before
  public void setup() {
    Dict dict = new Dict();
    dict.setId(1L);
    dict.setCode("gender");
    dict.setName("性别");
    dict.setRank(1);
    dict.setParentId(0L);

    given(dictService.findOne(Matchers.longThat(new GreaterThan<>(0L)))).willReturn(dict);
    given(dictService.findOne(-1L)).willReturn(null);
    RestAssuredMockMvc.mockMvc(mockMvc);
  }
}
