package com.fire.controller;

import com.fire.common.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.List;

@Api(tags = "晶粒相关接口列表")
@Slf4j
@RestController
@RequestMapping("/grain")
public class grainController {

    @ApiOperation(value = "晶粒提取")
    @GetMapping("/seg")
    public Result<?> extractGrain() {
        try {
            String exe = "D:\\anaconda\\python.exe";
            String py = "./Fire-py/grain.py";

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
        return Result.success("晶粒提取成功");
    }

    @ApiOperation(value = "晶粒区域裁剪")
    @PostMapping("/corp")
    public Result<?> corpGrain(@RequestBody List<Double> box) {
        int top = (int) Math.round(box.get(0));
        int left = (int) Math.round(box.get(1));
        int width = (int) Math.round(box.get(2));
        int height = (int) Math.round(box.get(3));

        try {
            String exe = "D:\\anaconda\\python.exe";
            String py = "./Fire-py/grain_corp.py";

            Process process = Runtime.getRuntime().exec(exe + " " + py + " " + top + " " + left + " " + width + " " + height);
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
        return Result.success("晶粒区域裁剪成功");
    }

    @ApiOperation(value = "晶粒特征提取")
    @GetMapping("/export")
    public Result<?> exportGrain() {
        try {
            String exe = "D:\\anaconda\\python.exe";
            String py = "./Fire-py/grain_export.py";

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
        return Result.success("晶粒特征提取成功");
    }
}
