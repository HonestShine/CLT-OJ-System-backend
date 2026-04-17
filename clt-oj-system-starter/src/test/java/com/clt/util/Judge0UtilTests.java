package com.clt.util;

import com.clt.utils.Judge0Util;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class Judge0UtilTests {

    @Test
    public void runCodeTest() throws Exception {
        JSONObject jsonObject = new JSONObject();
        //Python3
        jsonObject = Judge0Util.runCode(109, "print('Hello Word!')", "", 10.0, 134217);
        System.out.println(jsonObject);
        //Java
        jsonObject = Judge0Util.runCode(91, "public class Main {\npublic static void main(String[] args) {\nSystem.out.println(\"Hello Word!\");\n}\n}", "", 10.0, 134217);
        System.out.println(jsonObject);
        String statusOfTestCase = jsonObject.getJSONObject("status").optString("description");
        String stdoutOfTestCase = jsonObject.optString("stdout");
        String stderrOfTestCase = jsonObject.optString("stderr");
        String compileOutputOfTestCase = jsonObject.optString("compile_output");
        Double runtimeOfTestCase = jsonObject.getDouble("time");
        Double memoryOfTestCase = jsonObject.getDouble("memory");
        String messageOfTestCase = jsonObject.optString("message");
        System.out.println(statusOfTestCase);
        System.out.println(stdoutOfTestCase);
        System.out.println(stderrOfTestCase);
        System.out.println(compileOutputOfTestCase);
        System.out.println(runtimeOfTestCase);
        System.out.println(memoryOfTestCase);
        System.out.println(messageOfTestCase);
    }

}
