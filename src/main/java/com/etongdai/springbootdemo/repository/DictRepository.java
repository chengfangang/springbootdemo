package com.etongdai.springbootdemo.repository;

import com.etongdai.springbootdemo.entity.Dict;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 数据字典
 *
 * @author hotleave
 */
public interface DictRepository extends JpaRepository<Dict, Long>, JpaSpecificationExecutor<Dict> {
  /**
   * 查询指定字典的所有字典项
   *
   * @param parentId 字典ID
   * @return 字典项列表
   */
  List<Dict> findByParentIdOrderByRank(Long parentId);

  /**
   * 基于{@link Query @Query}的自定义查询
   *
   * @param parentId 上级ID
   * @param pageable 分页信息
   * @return 指定字典的所有字典项
   */
  @Query("select dict from Dict dict where parentId = ?1 order by rank desc")
  Page<Dict> customPageableQuery(Long parentId, Pageable pageable);
}
