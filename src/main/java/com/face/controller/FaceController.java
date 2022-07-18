package com.face.controller;

import com.face.annotation.FaceLog;
import com.face.bean.result.FaceResult;
import com.face.service.FaceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author tanyongpeng
 * <p>des</p>
 **/
@RestController
@RequestMapping("/face")
@Api("人脸验证接口")
@Slf4j
public class FaceController {

    @Autowired
    FaceService faceService;

    @PostMapping("/vef")
    @ApiOperation(value="人脸验证", notes="根据传入的base64编码和数据的base64编码进行对比")
    @FaceLog
    public FaceResult faceVef(@RequestBody String imageBase){
        return faceService.vef(imageBase);
    }

    @GetMapping("/test")
    public String faceVeftest(){
        return "tse";
    }

}
