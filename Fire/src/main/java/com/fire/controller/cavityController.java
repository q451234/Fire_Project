package com.fire.controller;

import com.fire.common.Result;
import com.fire.util.Common;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Api(tags = "孔洞相关接口列表")
@Slf4j
@RestController
@RequestMapping("/cavity")
public class cavityController {

    @Value("${py-exe}")
    private String exe;

    @ApiOperation(value = "孔洞提取")
    @GetMapping("/seg")
    public Result<?> extractCavity() {
        String py = "./Fire-py/cavity.py";
        Common.executePy(py, exe, "");
        return Result.success("孔洞提取成功");
    }

    @ApiOperation(value = "孔洞特征导出")
    @GetMapping("/export")
    public Result<?> exportCavity() {
        String py = "./Fire-py/cavity_export.py";
        Common.executePy(py, exe, "");
        List<List<String>> table = Common.exportTable("./Fire-py/feature/cavity.csv");
        return Result.success(table);
    }
}
