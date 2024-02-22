package com.fire.controller;

import com.fire.common.Constant;
import com.fire.common.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.HashMap;

@Api(tags = "图像处理相关接口列表")
@Slf4j
@RestController
@RequestMapping("/img")
public class ImgController {

    @Value("${py-exe}")
    private String exe;

    @ApiOperation(value = "返回前端发送来的图片")
    @PostMapping("/upload")
    public Result<?> returnImg(@RequestBody MultipartFile file) {
        File directory = new File("");//参数为空
        String path =directory.getAbsolutePath();

        String originalFilename = file.getOriginalFilename();

        assert originalFilename != null;

        try {
            //将文件保存指定目录
            file.transferTo(new File(path + "\\Fire-py\\img\\origin.jpg"));
            return Result.success("图片传输成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.fail(Constant.FAIL_CODE_4,"图片上传错误");
    }

    @ApiOperation(value = "熔化区分割")
    @GetMapping("/seg")
    public Result<?> segMeltZoo() {
        try {
            String py = "./Fire-py/seg.py";

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
        return Result.success("熔化区分割成功");
    }

    @ApiOperation(value = "图像识别")
    @GetMapping("/classify")
    public Result<?> classify() {
        String line = null;
        try {
            String py = "./Fire-py/swin.py";

            Process process = Runtime.getRuntime().exec(exe + " " + py);
            //获取结果的同时设置输入流编码格式"gb2312"
            InputStreamReader isr = new InputStreamReader(process.getInputStream(),"gb2312");
            LineNumberReader input = new LineNumberReader(isr);

            line = input.readLine();
            log.info(line);
            input.close();
            isr.close();
            process.waitFor();
        } catch (InterruptedException | IOException e) {
            log.info("调用python脚本并读取结果时出错：" + e.getMessage());
        }
        return Result.success(line, "熔痕类型识别成功");
    }
}
