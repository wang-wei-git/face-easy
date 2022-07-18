package com.face.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.face.bean.Face;
import com.face.bean.result.FaceResult;
import com.face.mapper.FaceMapper;
import com.face.server.FaceContrastServer;
import com.face.service.FaceService;
import com.face.utils.JwtUtils;
import com.face.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author typsusan
* @description 针对表【face】的数据库操作Service实现
* @createDate 2022-07-17 03:33:50
*/
@Service
public class FaceServiceImpl extends ServiceImpl<FaceMapper, Face>
    implements FaceService {

    @Autowired
    FaceContrastServer faceContrastServer;

    @Override
    public FaceResult vef(String imageBase) {
        imageBase = JSONUtil.parseObj(imageBase).getStr("imageBase");
        List<Face> faceList = lambdaQuery().orderByDesc(Face::getVefNum).list();
        int faceLength = faceList.size();
        for (Face face : faceList) {
            FaceResult faceResult = faceContrastServer.faceContrast(face.getFaceBase(), imageBase);
            if (faceResult.getCode() == FaceResult.SUCCESS_CODE ){
                if (faceResult.getScore() > FaceResult.SATISFY_SCORE){
                    if (face.getFaceStatus() == 0){
                        // 成功
                        lambdaUpdate().set(Face::getVefNum,face.getVefNum()+1).eq(Face::getFid,face.getFid()).update();
                        faceResult.setMsg(TimeUtils.timeQuantum()+"好,"+face.getFaceName());
                        faceResult.setName(face.getFaceName());
                        Map<String,String> map = new HashMap<>();
                        map.put("score",String.valueOf(faceResult.getScore()));
                        map.put("faceName",faceResult.getName());
                        faceResult.setToken(JwtUtils.genereteToken(map));
                        return faceResult;
                    }else {
                        // 失败 人脸被禁用
                        lambdaUpdate().set(Face::getVefNum,face.getVefNum()+1).eq(Face::getFid,face.getFid()).update();
                        faceResult.setMsg(face.getFaceName()+",当前人脸被禁用");
                        faceResult.setName(face.getFaceName());
                        faceResult.setCode(FaceResult.FORBIDDEN_FACE);
                        return faceResult;
                    }
                }else {
                    if (faceLength == 1){
                        // 人脸库没有检测到人脸
                        return FaceResult.error(FaceResult.NOT_FOUND_FACE,"人脸库不存在该人脸",faceResult.getScore());
                    }
                    faceLength --;
                }
            }else {
                // 接口返回异常
                return faceResult;
            }
        }
        // 空异常
        return FaceResult.error(FaceResult.NULL_ERROR,"空指针异常");
    }

}




