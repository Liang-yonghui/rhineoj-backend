package com.liangyonghui.rhineoj.judge.strategy;

import com.liangyonghui.rhineoj.judge.codesandbox.model.JudgeInfo;

/**
 * 判题策略
 */
public interface JudgeStragtegy {

    /**
     * 执行判题
     * @param judgeContext
     * @return
     */
    JudgeInfo doJudge(JudgeContext judgeContext);
}
