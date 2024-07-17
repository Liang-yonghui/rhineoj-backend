package com.liangyonghui.rhineoj.judge.codesandbox;

import com.liangyonghui.rhineoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.liangyonghui.rhineoj.judge.codesandbox.model.ExecuteCodeResponse;
import com.liangyonghui.rhineoj.model.enums.QuestionSubmitLanguagEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

@SpringBootTest
class CodeSandBoxTest {

    @Value("${codesandbox.type:example}")
    String type;


    @Test
    void codeSandboxTest1() {
        System.out.println("type"+type);
        CodeSandBox codeSandBox = CodeSandboxFactory.newInstance(type);
        String code = "\n" +
                "public class Main {\n" +
                "\n" +
                "    public static void main(String[] args) {\n" +
                "        int a = Integer.parseInt(args[0]);\n" +
                "        int b = Integer.parseInt(args[1]);\n" +
                "        System.out.println(\"result:\" + (a + b));\n" +
                "    }\n" +
                "}\n";
        String language = QuestionSubmitLanguagEnum.JAVA.getValue();
        List<String> inputList = Arrays.asList("1 2", "3 4");
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder().language(language).code(code).inputList(inputList).build();
        ExecuteCodeResponse executeCodeResponse = codeSandBox.executeCode(executeCodeRequest);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String type = scanner.next();
            CodeSandBox codeSandBox = CodeSandboxFactory.newInstance(type);
            String code = "\n" +
                    "public class Main {\n" +
                    "\n" +
                    "    public static void main(String[] args) {\n" +
                    "        int a = Integer.parseInt(args[0]);\n" +
                    "        int b = Integer.parseInt(args[1]);\n" +
                    "        System.out.println(\"result:\" + (a + b));\n" +
                    "    }\n" +
                    "}\n";
            String language = QuestionSubmitLanguagEnum.JAVA.getValue();
            List<String> inputList = Arrays.asList("1,2", "3,4");
            ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder().language(language).code(code).inputList(inputList).build();
            ExecuteCodeResponse executeCodeResponse = codeSandBox.executeCode(executeCodeRequest);
        }
    }

}