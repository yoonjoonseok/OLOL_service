package com.ll.olol.boundedContext.recruitment.service;

import com.ll.olol.boundedContext.recruitment.entity.RecruitmentArticle;
import com.ll.olol.boundedContext.recruitment.repository.RecruitmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecruitmentService {
    private final RecruitmentRepository recruitmentRepository;

    public Optional<RecruitmentArticle> findById(Long id) {
        return recruitmentRepository.findById(id);
    }
}
