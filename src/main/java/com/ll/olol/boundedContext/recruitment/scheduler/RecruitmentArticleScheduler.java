package com.ll.olol.boundedContext.recruitment.scheduler;

import com.ll.olol.boundedContext.recruitment.service.RecruitmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RecruitmentArticleScheduler {

    @Autowired
    private RecruitmentService recruitmentService;


}
