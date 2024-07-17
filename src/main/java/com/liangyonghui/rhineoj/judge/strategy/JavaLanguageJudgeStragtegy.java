package com.liangyonghui.rhineoj.judge.strategy;

import cn.hutool.json.JSONUtil;
import com.liangyonghui.rhineoj.model.dto.question.JudgeCase;
import com.liangyonghui.rhineoj.judge.codesandbox.model.JudgeInfo;
import com.liangyonghui.rhineoj.model.dto.question.JudgeConfig;
import com.liangyonghui.rhineoj.model.entity.Question;
import com.liangyonghui.rhineoj.model.enums.JudgeInfoMessageEnum;

import java.util.List;
import java.util.Optional;

public class JavaLanguageJudgeStragtegy implements JudgeStragtegy {
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

        Long outputMemory = Optional.ofNullable(judgeInfo.getMemory()).orElse(0L);
        Long outputTime = Optional.ofNullable(judgeInfo.getTime()).orElse(0L);
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

        for (int i = 0; i < judgeCaseList.size(); i++) {
            if (!judgeCaseList.get(i).getOutput().equals(outputList.get(i))){
                judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
                judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
                return judgeInfoResponse;
            }
        }
        String judgeConfigStr = question.getJudgeConfig();
        JudgeConfig judgeConfig = JSONUtil.toBean(judgeConfigStr, JudgeConfig.class);
        // 假设java程序需要额外执行10s钟
        Long needMemory = judgeConfig.getMemoryLimit();
        Long needTime = judgeConfig.getTimeLimit();
        long JAVA_PROGRAM_TIME_COST = 10000L;
        if (outputMemory - JAVA_PROGRAM_TIME_COST > needMemory ){
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
