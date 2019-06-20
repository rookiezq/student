package com.rookied.student.util;

import org.junit.Test;
import org.python.core.PyFunction;
import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @desciption: 测试调用python
 * @author: Demon
 * @version: 1.0 2019-04-20 10:22
 **/
@SuppressWarnings("all")
public class PythonTest {
    public static void main(String[] args) throws IOException, InterruptedException {
        Integer a = 1, b = 2;
        String data = "97.0,65.0,85.0,86.0,91.0,52.0,85.0,97.0,87.0,83.0,94.0,84.0,86.0,87.0,87.0,83.0";
        String[] argg = new String[] { "python", "src/main/resources/python/analyse.py","0"};
        Process pr = Runtime.getRuntime().exec(argg);

        BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
        String line;
        String result = "";
        while ((line = in.readLine()) != null) {
//                line = decodeUnicode(line);
            result += line;
        }
        in.close();
        pr.waitFor();
        System.out.println(result);

    }

    @Test
    public void test1(){
        PythonInterpreter interpreter = new PythonInterpreter();
        interpreter.exec("days=['mod','Tue','Wed','Thu','Fri','Sat','Sun']");
        interpreter.exec("print(days[1])");

    }

    @Test
    public void test2(){
/*        String data1 = "97.0,65.0,85.0,86.0,91.0,52.0,85.0,97.0,87.0,83.0,94.0,84.0,86.0,87.0,87.0,83.0";
        String[] arg = new String[]{"python", "src/main/resources/python/predict.py", "0", data1};
        String result = "";
        try {
            Process pr = Runtime.getRuntime().exec(arg);
            BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
//                line = decodeUnicode(line);
                result += line;
            }
            in.close();
            pr.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }
}
