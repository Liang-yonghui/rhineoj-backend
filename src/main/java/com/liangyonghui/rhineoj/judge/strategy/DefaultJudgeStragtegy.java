package com.liangyonghui.rhineoj.judge.strategy;

import cn.hutool.json.JSONUtil;
import com.liangyonghui.rhineoj.model.dto.question.JudgeCase;
import com.liangyonghui.rhineoj.judge.codesandbox.model.JudgeInfo;
import com.liangyonghui.rhineoj.model.entity.Question;
import com.liangyonghui.rhineoj.model.enums.JudgeInfoMessageEnum;

import java.util.List;

public class DefaultJudgeStragtegy implements JudgeStragtegy {
    /**
     * 执行判题
     * @param judgeContext
     * @return
     */
    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext) {

        JudgeInfo judgeInfo = judgeContext.getJudgeInfo();
        List<String> inputList = judgeContext.getInputList();
        List<String> outputList = judgeContext.getOutputList();
        Question question = judgeContext.getQuestion();
        List<JudgeCase> judgeCaseList = judgeContext.getJudgeCaseList();
        String judgeConfigStr = question.getJudgeConfig();
        JudgeInfo judgeConfig = JSONUtil.toBean(judgeConfigStr, JudgeInfo.class);
        Long needMemory = judgeConfig.getMemory();
        Long needTime = judgeConfig.getTime();
        Long outputMemory = judgeInfo.getMemory();
        Long outputTime = judgeInfo.getTime();
        JudgeInfo judgeInfoResponse = new JudgeInfo();
        JudgeInfoMessageEnum judgeInfoMessageEnum = JudgeInfoMessageEnum.ACCEPTED;
        judgeInfoResponse.setMemory(outputMemory);
        judgeInfoResponse.setTime(outputTime);


        // 根据沙箱的执行结果，设置题目的判题状态和信息
        // 先判断沙箱执行的结果输出数量是否和预期输出数量相等
        if (inputList.size() != outputList.size()){
            judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }

        for (int i = 0; i < inputList.size(); i++) {
            if (!outputList.get(i).equals(inputList.get(i))){
                judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
                judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
                return judgeInfoResponse;
            }
        }

        if (outputMemory > needMemory ){
            judgeInfoMessageEnum = JudgeInfoMessageEnum.MEMORY_LIMIT_EXCEEDED;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }
        if (outputTime > needTime ){
            judgeInfoMessageEnum = JudgeInfoMessageEnum.TIME_LIMIT_EXCEEDED;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }
        judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
        return judgeInfoResponse;
    }
}
