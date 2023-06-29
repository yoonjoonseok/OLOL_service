package com.ll.olol.boundedContext.recruitment.scheduler;

import com.ll.olol.boundedContext.recruitment.entity.RecruitmentArticle;
import com.ll.olol.boundedContext.recruitment.service.RecruitmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class RecruitmentArticleScheduler {

    @Autowired
    private RecruitmentService recruitmentService;


    @Scheduled(fixedDelay = 60 * 1000) // 1분마다 실행 (단위: 밀리초)
    public void triggerEvent() {
        List<RecruitmentArticle> recruitmentArticleList = recruitmentService.findAll();

        LocalDateTime currentTime = LocalDateTime.now();

        for (RecruitmentArticle article : recruitmentArticleList) {
            if (!article.isEventTrigger()) {
                if (article.getRecruitmentArticleForm().getCourseTime().plusSeconds(60L).isBefore(currentTime)) {
                    recruitmentService.sendNotificationAuthor(article);
                }
//            if (article.getRecruitmentArticleForm().getCourseTime().plusHours(2).isBefore(currentTime)) {
//                Member author = rq.getMember();
//                recruitmentService.sendNotificationAuthor(article, author);
//            }
            }


        }
    }

}
