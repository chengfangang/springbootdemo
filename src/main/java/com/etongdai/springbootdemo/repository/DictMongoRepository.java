package com.etongdai.springbootdemo.repository;

import com.etongdai.springbootdemo.document.Dict;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author hotleave
 */
public interface DictMongoRepository extends MongoRepository<Dict, String> {
}
