package com.liangyonghui.rhineoj.judge.codesandbox;

import com.liangyonghui.rhineoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.liangyonghui.rhineoj.judge.codesandbox.model.ExecuteCodeResponse;

public interface CodeSandBox {

    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
