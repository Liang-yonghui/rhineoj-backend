package com.liangyonghui.rhineoj.judge;

import com.liangyonghui.rhineoj.judge.strategy.DefaultJudgeStragtegy;
import com.liangyonghui.rhineoj.judge.strategy.JavaLanguageJudgeStragtegy;
import com.liangyonghui.rhineoj.judge.strategy.JudgeContext;
import com.liangyonghui.rhineoj.judge.strategy.JudgeStragtegy;
import com.liangyonghui.rhineoj.judge.codesandbox.model.JudgeInfo;
import com.liangyonghui.rhineoj.model.entity.QuestionSubmit;
import org.springframework.stereotype.Service;

/**
 * 判题管理，简化调用
 */
@Service
public class JudgeManager {

    JudgeInfo doJudge(JudgeContext judgeContext){
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        String language = questionSubmit.getLanguage();
        JudgeStragtegy judgeStragtegy = new DefaultJudgeStragtegy();
        if ("java".equals(language)){
            judgeStragtegy = new JavaLanguageJudgeStragtegy();
        }
        JudgeInfo judgeInfo = judgeStragtegy.doJudge(judgeContext);
        return judgeInfo;
    }
}
