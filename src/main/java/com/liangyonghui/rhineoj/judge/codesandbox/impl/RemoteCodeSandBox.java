package com.liangyonghui.rhineoj.judge.codesandbox.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.util.StringUtils;
import com.liangyonghui.rhineoj.common.ErrorCode;
import com.liangyonghui.rhineoj.exception.BusinessException;
import com.liangyonghui.rhineoj.judge.codesandbox.CodeSandBox;
import com.liangyonghui.rhineoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.liangyonghui.rhineoj.judge.codesandbox.model.ExecuteCodeResponse;

public class RemoteCodeSandBox implements CodeSandBox {

    private static final String AUTH_REQUEST_HEADER = "auth";

    private static final String AUTH_REQUEST_SECRET = "secretKey";


    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest){
        System.out.println("远程代码沙箱");
        String url = "http://localhost:8090/executeCode";
        String json = JSONUtil.toJsonStr(executeCodeRequest);
        String responseStr = HttpUtil.createPost(url)
                .header(AUTH_REQUEST_HEADER, AUTH_REQUEST_SECRET)
                .body(json)
                .execute()
                .body();
        if (StringUtils.isBlank(responseStr)) {
            throw new BusinessException(ErrorCode.API_REQUEST_ERROR,"exampleCode remoteSandbox error, message = {}" + responseStr);
        }
        return JSONUtil.toBean(responseStr, ExecuteCodeResponse.class);
    }
}
