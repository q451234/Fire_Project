package com.fire.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
}
