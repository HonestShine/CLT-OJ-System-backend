package com.clt.service;

import com.clt.dto.SubmissionCodeDTO;
import com.clt.entity.JudgeResult;

public interface JudgeService {

    /**
     * 判题
     */
    JudgeResult judge(SubmissionCodeDTO submissionCodeDTO) throws RuntimeException;

}
