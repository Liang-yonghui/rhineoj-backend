package com.liangyonghui.rhineoj.judge.codesandbox;

import com.liangyonghui.rhineoj.judge.codesandbox.impl.ExampleCodeSandBox;
import com.liangyonghui.rhineoj.judge.codesandbox.impl.RemoteCodeSandBox;
import com.liangyonghui.rhineoj.judge.codesandbox.impl.ThirdPartyCodeSandBox;

public class CodeSandboxFactory {

    public static CodeSandBox newInstance(String type) {
        switch (type) {
            case "example":
                return new ExampleCodeSandBox();
            case "remote":
                return new RemoteCodeSandBox();
            case "thirdParty":
                return new ThirdPartyCodeSandBox();
            default:
                return new ExampleCodeSandBox();
        }
    }
}
