package com.liangyonghui.rhineoj.judge.codesandbox.impl;

import com.liangyonghui.rhineoj.judge.codesandbox.CodeSandBox;
import com.liangyonghui.rhineoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.liangyonghui.rhineoj.judge.codesandbox.model.ExecuteCodeResponse;
import com.liangyonghui.rhineoj.judge.codesandbox.model.JudgeInfo;
import com.liangyonghui.rhineoj.model.enums.QuestionSubmitStatusEnum;

import java.util.List;

public class ExampleCodeSandBox implements CodeSandBox {

    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest){
        List<String> inputList = executeCodeRequest.getInputList();
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setMessage("测试执行成功");
        executeCodeResponse.setOutputList(inputList);
        executeCodeResponse.setStatus(QuestionSubmitStatusEnum.SUCCESS.getText());
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMemory(100L);
        judgeInfo.setTime(100L);
        executeCodeResponse.setJudgeInfo(judgeInfo);
        System.out.println("ExampleCodeSandBox executeCode");
        return executeCodeResponse;
    }
}
