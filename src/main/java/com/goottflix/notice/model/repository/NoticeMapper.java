package com.goottflix.notice.model.repository;

import com.goottflix.notice.model.Notice;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NoticeMapper {

    //전체 공지사항 리스트
    List<Notice> findAll();

    //공지사항 작성
    void save(Notice notice);

    //공지사항 수정
    void update(Notice notice);

    //공지사항 삭제
    void delete(Long id);



}
