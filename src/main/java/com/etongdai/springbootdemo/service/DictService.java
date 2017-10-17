package com.etongdai.springbootdemo.service;

import com.etongdai.springbootdemo.entity.Dict;
import com.etongdai.springbootdemo.repository.DictRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Predicate;

/**
 * 数据字典服务
 *
 * @author hotleave
 */
@Service
public class DictService {
  private final DictRepository dictRepository;

  public DictService(DictRepository dictRepository) {
    this.dictRepository = dictRepository;
  }

  public Dict save(Dict dict) {
    return dictRepository.save(dict);
  }

  public List<Dict> saveBatch(Iterable<Dict> entities) {
    return dictRepository.save(entities);
  }

  public Dict findOne(Long id) {
    return dictRepository.findOne(id);
  }

  public void delete(Long id) {
    dictRepository.delete(id);
  }

  public Page<Dict> findAll(Dict dict, Pageable pageable) {
    return dictRepository.findAll(toSpecification(dict), pageable);
  }

  public Page<Dict> customPageableQuery(Long parentId, Pageable pageable) {
    return dictRepository.customPageableQuery(parentId, pageable);
  }

  public List<Dict> getDictItems(Long parentId) {
    return dictRepository.findByParentIdOrderByRank(parentId);
  }

  private Specification<Dict> toSpecification(Dict dict) {
    return (root, query, builder) -> {
      List<Predicate> predicateList = new ArrayList<>();

      if (StringUtils.hasText(dict.getName())) {
        predicateList.add(builder.like(root.get("name"), "%" + dict.getName() + "%"));
      }

      if (dict.getParentId() != null) {
        predicateList.add(builder.equal(root.get("parentId"), dict.getParentId()));
      }

      return builder.and(predicateList.toArray(new Predicate[predicateList.size()]));
    };
  }
}
