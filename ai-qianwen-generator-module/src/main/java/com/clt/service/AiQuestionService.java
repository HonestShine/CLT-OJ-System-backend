package com.clt.service;

import com.clt.vo.QuestionVO;

public interface AiQuestionService {

    QuestionVO generateQuestion(String promptWord);

}
