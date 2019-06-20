package com.rookied.student.service.impl;

import com.rookied.student.service.AlyPreService;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @desciption:
 * @author: Demon
 * @version: 1.0 2019-04-21 17:03
 **/
@Service
@SuppressWarnings("all")
public class AlyPreServiceImpl implements AlyPreService {
    /**
     * 分析
     *
     * @param maj 专业
     * @return none
     */
    @Override
    public String analyse(String maj) {
        String[] arg = new String[]{"python", "src/main/resources/python/analyse.py", maj};
        try {
            Process pr = Runtime.getRuntime().exec(arg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 预测
     *
     * @param maj  专业
     * @param data 成绩
     * @return none
     */
    @Override
    public String predict(String maj, String data) {
        //String data1 = "97.0,65.0,85.0,86.0,91.0,52.0,85.0,97.0,87.0,83.0,94.0,84.0,86.0,87.0,87.0,83.0";
        String[] arg = new String[]{"python", "src/main/resources/python/predict.py", maj, data};
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
        }
        return result;
    }
}
