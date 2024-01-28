package com.fire.controller;

import com.fire.common.Constant;
import com.fire.common.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

@Api(tags = "孔洞相关接口列表")
@Slf4j
@RestController
@RequestMapping("/cavity")
public class cavityController {

    @ApiOperation(value = "孔洞提取")
    @GetMapping("/seg")
    public Result<?> extractCavity() {
        try {
            String exe = "D:\\anaconda\\python.exe";
            String py = "./Fire-py/cavity.py";

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
        return Result.success("孔洞提取成功");
    }

    @ApiOperation(value = "孔洞特征导出")
    @GetMapping("/export")
    public Result<?> exportCavity() {
        try {
            String exe = "D:\\anaconda\\python.exe";
            String py = "./Fire-py/cavity_export.py";

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
        return Result.success("孔洞特征表格生成成功");
    }
}
