package com.liangyonghui.rhineoj.judge.codesandbox;

import com.liangyonghui.rhineoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.liangyonghui.rhineoj.judge.codesandbox.model.ExecuteCodeResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@AllArgsConstructor
public class CodeSandboxProxy implements CodeSandBox {


    private final CodeSandBox codeSandBox;

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        log.info("代码沙箱请求信息"+executeCodeRequest.toString());
        ExecuteCodeResponse executeCodeResponse = codeSandBox.executeCode(executeCodeRequest);
        log.info("代码沙箱响应信息"+executeCodeResponse.toString());
        return executeCodeResponse;
    }
}
