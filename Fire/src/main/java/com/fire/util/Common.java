package com.fire.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class Common {
    public static List<List<String>> exportTable(String path){
        List<List<String>> table = new ArrayList<>();
        // 创建 reader
        try (BufferedReader br = Files.newBufferedReader(Paths.get(path))) {
            // CSV文件的分隔符
            String DELIMITER = ",";
            // 按行读取
            String line;
            while ((line = br.readLine()) != null) {
                // 分割
                String[] columns = line.split(DELIMITER);

                List<String> data = new ArrayList<>(Arrays.asList(columns));
                table.add(data);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return table;
    }

    public static void executePy(String py, String exe){
        try {
            Process process = Runtime.getRuntime().exec(exe + " " + py);
            //获取结果的同时设置输入流编码格式"gb2312"
            InputStreamReader isr = new InputStreamReader(process.getInputStream(),"gb2312");
            LineNumberReader input = new LineNumberReader(isr);

            String line = null;
            while ((line = input.readLine()) != null) {
                log.info(line);
            }
            input.close();
            isr.close();
            process.waitFor();
        } catch (InterruptedException | IOException e) {
            log.info("调用python脚本并读取结果时出错：" + e.getMessage());
        }
    }
}
