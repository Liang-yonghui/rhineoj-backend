package com.liangyonghui.rhineoj.judge;

import cn.hutool.json.JSONUtil;
import com.liangyonghui.rhineoj.common.ErrorCode;
import com.liangyonghui.rhineoj.exception.BusinessException;
import com.liangyonghui.rhineoj.judge.codesandbox.CodeSandBox;
import com.liangyonghui.rhineoj.judge.codesandbox.CodeSandboxFactory;
import com.liangyonghui.rhineoj.judge.codesandbox.CodeSandboxProxy;
import com.liangyonghui.rhineoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.liangyonghui.rhineoj.judge.codesandbox.model.ExecuteCodeResponse;
import com.liangyonghui.rhineoj.judge.strategy.DefaultJudgeStragtegy;
import com.liangyonghui.rhineoj.judge.strategy.JudgeContext;
import com.liangyonghui.rhineoj.judge.strategy.JudgeStragtegy;
import com.liangyonghui.rhineoj.model.dto.question.JudgeCase;
import com.liangyonghui.rhineoj.judge.codesandbox.model.JudgeInfo;
import com.liangyonghui.rhineoj.model.entity.Question;
import com.liangyonghui.rhineoj.model.entity.QuestionSubmit;
import com.liangyonghui.rhineoj.model.enums.QuestionSubmitStatusEnum;
import com.liangyonghui.rhineoj.service.QuestionService;
import com.liangyonghui.rhineoj.service.QuestionSubmitService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class JudgeServiceImpl implements JudgeService {

    @Value("${codesandbox.type:example}")
    private String type;

    @Resource
    QuestionService questionService;

    @Resource
    QuestionSubmitService questionSubmitService;

    @Resource
    JudgeManager judgeManager;

    @Override
    public QuestionSubmit doJudge(long questionSubmitId) {

        QuestionSubmit questionSubmit = questionSubmitService.getById(questionSubmitId);
        if (questionSubmit == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "提交信息不存在");
        }
        Long questionId = questionSubmit.getQuestionId();
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目不存在");
        }
        Integer status = questionSubmit.getStatus();
        if (!status.equals(QuestionSubmitStatusEnum.WAITING.getValue())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "题目正在判题中");
        }
        QuestionSubmit questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setQuestionId(questionId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.RUNNING.getValue());
        boolean update = questionSubmitService.updateById(questionSubmitUpdate);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新失败");
        }

        CodeSandBox codeSandBox = CodeSandboxFactory.newInstance(type);
        CodeSandboxProxy codeSandboxProxy = new CodeSandboxProxy(codeSandBox);
        String judgeCase = question.getJudgeCase();
        String language = questionSubmit.getLanguage();
        String code = questionSubmit.getCode();
        List<JudgeCase> judgeCaseList = JSONUtil.toList(judgeCase, JudgeCase.class);
        List<String> inputList = judgeCaseList.
                stream().
                map(JudgeCase::getInput).
                collect(Collectors.toList());
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest
                .builder().
                language(language).
                inputList(inputList).
                code(code).
                build();
        ExecuteCodeResponse executeCodeResponse = codeSandboxProxy.executeCode(executeCodeRequest);
        JudgeInfo judgeInfo = executeCodeResponse.getJudgeInfo();
        List<String> outputList = executeCodeResponse.getOutputList();

        JudgeContext judgeContext = new JudgeContext();
        judgeContext.setJudgeInfo(judgeInfo);
        judgeContext.setInputList(inputList);
        judgeContext.setOutputList(outputList);
        judgeContext.setJudgeCaseList(judgeCaseList);
        judgeContext.setQuestion(question);
        judgeContext.setQuestionSubmit(questionSubmit);
        JudgeStragtegy judgeStragtegy = new DefaultJudgeStragtegy();
        JudgeInfo judgeInfo_result = judgeManager.doJudge(judgeContext);

        questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setQuestionId(questionId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.SUCCESS.getValue());
        questionSubmitUpdate.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo_result));
        update = questionSubmitService.updateById(questionSubmitUpdate);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新失败");
        }
        QuestionSubmit questionSubmitServiceById = questionSubmitService.getById(questionId);
        return questionSubmitServiceById;
    }
}
