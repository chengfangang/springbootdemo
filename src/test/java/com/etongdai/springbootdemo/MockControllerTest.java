package com.etongdai.springbootdemo;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.etongdai.springbootdemo.api.rest.DictApi;
import com.etongdai.springbootdemo.entity.Dict;
import com.etongdai.springbootdemo.service.DictService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Mvc Mockup
 *
 * @author hotleave
 */
@RunWith(SpringRunner.class)
@WebMvcTest(DictApi.class)
public class MockControllerTest {
  @Autowired
  private MockMvc mvc;

  @MockBean
  private DictService dictService;

  @Test
  public void getOne() throws Exception {
    Dict dict = new Dict();
    dict.setCode("gender");
    dict.setId(1L);
    dict.setName("性别");
    dict.setParentId(0L);
    dict.setRank(1);

    // 调用dictService.findOne(1L)时返回dict
    given(dictService.findOne(1L)).willReturn(dict);

    mvc.perform(get("/api/v1/dicts/1").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json("{\"id\":1,\"code\":\"gender\",\"name\":\"性别\",\"rank\":1,\"parentId\":0}"));
  }
}
