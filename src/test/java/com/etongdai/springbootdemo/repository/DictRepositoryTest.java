package com.etongdai.springbootdemo.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.etongdai.springbootdemo.entity.Dict;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Repository测试
 *
 * <p>DataJpaTest注解的单元测试会在测试结束时自动回滚， 如果你不想这样，可以使用{@code @Transactional(propagation =
 * Propagation.NOT_SUPPORTED)}禁用事务</p>
 *
 * @author hotleave
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class DictRepositoryTest {
  @Autowired
  private DictRepository dictRepository;

  @Test
  public void findByParentIdOrderByRank() throws Exception {
    List<Dict> dictList = dictRepository.findByParentIdOrderByRank(1L);

    assertNotNull(dictList);
    assertEquals(2, dictList.size());
  }

}