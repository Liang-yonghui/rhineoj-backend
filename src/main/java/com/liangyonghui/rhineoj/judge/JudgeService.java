package com.liangyonghui.rhineoj.judge;

import com.liangyonghui.rhineoj.model.entity.QuestionSubmit;
import com.liangyonghui.rhineoj.model.vo.QuestionSubmitVO;

public interface JudgeService {


    QuestionSubmit doJudge(long questionSubmitId);
}
