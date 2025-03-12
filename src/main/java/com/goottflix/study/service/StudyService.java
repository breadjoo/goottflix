package com.goottflix.study.service;

import com.goottflix.study.model.Study;
import com.goottflix.study.model.StudyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudyService {

    private final StudyMapper studyMapper;

    public void creatStudy(Study study) {

        if (study.getId() != null) {
            studyMapper.write(study);
        }
    }

    public void updateStudy(Study study) {

        if (study.getId() != null) {
            studyMapper.update(study);
        }
    }
}
