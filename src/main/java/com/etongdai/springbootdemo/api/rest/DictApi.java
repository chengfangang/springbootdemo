package com.etongdai.springbootdemo.api.rest;

import static com.etongdai.springbootdemo.utils.DictConstans.API_VERSION;
import static com.etongdai.springbootdemo.utils.DictConstans.ERROR_BLOCK_BY_BLACK_LIST;

import com.etongdai.springbootdemo.entity.Dict;
import com.etongdai.springbootdemo.exception.BusinessException;
import com.etongdai.springbootdemo.exception.RecordNotFoundException;
import com.etongdai.springbootdemo.service.DictService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

/**
 * 数据字典
 *
 * @author hotleave
 */
@RestController
@RequestMapping(API_VERSION + "/dicts")
@Slf4j
@Validated
@Api("数据字典")
public class DictApi {
  private final DictService dictService;

  public DictApi(DictService dictService) {
    this.dictService = dictService;
  }

  @ApiOperation("新增")
  @PostMapping
  public Dict save(@Valid @RequestBody Dict dict) {
    log.debug("dict to be save: {}", dict);

    return dictService.save(dict);
  }

  @ApiOperation("修改")
  @PutMapping("/{id}")
  public Dict update(@PathVariable Long id, @Valid @RequestBody Dict dict) {
    log.debug("dict to be update: {}", dict);

    return dictService.save(dict);
  }

  @ApiOperation("根据ID获取记录")
  @GetMapping("/{id}")
  public Dict getOne(@PathVariable Long id) {
    log.debug("get dict.id={}", id);

    Dict dict = dictService.findOne(id);
    if (dict == null) {
      throw new RecordNotFoundException("Dict.id=" + id);
    }

    return dict;
  }

  @ApiOperation("根据ID删除记录")
  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) {
    log.debug("delete dict.id={}", id);

    dictService.delete(id);
  }

  @ApiOperation("查询")
  @GetMapping
  public Page<Dict> findAll(Dict dict, Pageable pageable) {
    log.debug("list dicts by page, param={}, pageable={}", dict, pageable);

    return dictService.findAll(dict, pageable);
  }

  @ApiOperation("获取字典项列表")
  @GetMapping("/{parentId}/items")
  public List<Dict> getDictItems(@PathVariable Long parentId) {
    return dictService.getDictItems(parentId);
  }

  @ApiOperation("获取字典项列表(自定义JPQL查询)")
  @GetMapping("/{id}/customItems")
  public Page<Dict> customPageableQuery(@PathVariable("id") Long parentId, Pageable pageable) {
    return dictService.customPageableQuery(parentId, pageable);
  }

  @ApiOperation("数据校验用")
  @GetMapping("/dummy")
  public String demostrateValidation(@RequestParam @Size(min = 3, max = 10, message = "必须是3到10个字符") String name,
      @RequestParam @Min(value = 10, message = "必须大于10") int number) {

    if ("black".equals(name)) {
      throw new BusinessException(ERROR_BLOCK_BY_BLACK_LIST, name);
    }

    return "Hello " + name + ", your input is " + number;
  }
}
