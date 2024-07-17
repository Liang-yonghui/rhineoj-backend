package com.liangyonghui.rhineoj.judge.codesandbox.impl;

import com.liangyonghui.rhineoj.judge.codesandbox.CodeSandBox;
import com.liangyonghui.rhineoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.liangyonghui.rhineoj.judge.codesandbox.model.ExecuteCodeResponse;

public class ThirdPartyCodeSandBox implements CodeSandBox {

    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest){
        System.out.println("第三方代码沙箱示例");
        return null;
    }
}
