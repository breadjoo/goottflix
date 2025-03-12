package com.goottflix.study.model;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StudyMapper {

    List<Study> findAll();

    Study findById(Long id);

    void write(Study study);

    void update(Study study);

    void deleteById(Long id);

}

