package com.clt;

import com.clt.entity.Result;
import com.clt.entity.VirtualPet;
import com.clt.service.VirtualPetService;
import com.clt.utils.JwtUtil;
import com.clt.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/pet")
@RestController
public class VirtualPetController {

    @Autowired
    private VirtualPetService virtualPetService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 获取虚拟宠物
     */
    @GetMapping
    public Result getVirtualPet(@RequestHeader("token") String token) {
        //从token中获取用户ID
        Integer userId = jwtUtil.parseToken(token).get("id", Integer.class);
        if (userId == null || userId <= 0) {
            return Result.error("401", "未登录");
        }
        BasicInformationOfVirtualPetVO virtualPet = virtualPetService.getVirtualPet(userId);
        return Result.success(virtualPet);
    }

    /**
     * 修改虚拟宠物名称
     */
    @PutMapping("/changeName")
    public Result updateVirtualPet(@RequestBody VirtualPet virtualPet) {
        if (virtualPet == null || virtualPet.getId() == null || virtualPet.getId() <= 0) {
            return Result.error("参数不能为空，ID不可用");
        }
        UpdateVirtualPetVO updateVirtualPetVO = virtualPetService.updateVirtualPet(virtualPet);
        return Result.success(updateVirtualPetVO);
    }

    /**
     * 打卡
     */
    @PutMapping("/punch")
    public Result punch(@RequestHeader("token") String token) {
        if (!jwtUtil.validateToken(token)) {
            return Result.error("401", "未登录");
        }
        //从token中获取用户ID
        Integer userId = jwtUtil.parseToken(token).get("id", Integer.class);
        PunchVO punchVO = virtualPetService.punch(userId);
        return Result.success(punchVO);
    }

    /**
     * 更新经验
     */
    @PutMapping("/updateExperience")
    public Result increaseExperience(@RequestBody VirtualPet virtualPet) {
        if (virtualPet == null || virtualPet.getId() == null || virtualPet.getId() <= 0) {
            return Result.error("参数不能为空，ID不可用");
        }
        UpdateExperienceVO updateExperienceVO = virtualPetService.updateExperience(virtualPet);
        return Result.success(updateExperienceVO);
    }

    /**
     * 返回话术
     */
    @GetMapping("/phrases")
    public Result getPhrases(@RequestParam("submissionStatus") String submissionStatus, @RequestHeader("token") String token) {
        if (submissionStatus == null || submissionStatus.isEmpty()) {
            return Result.error("参数不能为空");
        }
        if (!jwtUtil.validateToken(token)) {
            return Result.error("401", "未登录");
        }
        //从token中获取用户ID
        Integer userId = jwtUtil.parseToken(token).get("id", Integer.class);
        String phrases = virtualPetService.getPhrases(submissionStatus, userId);
        if (phrases == null || phrases.isEmpty()) {
            return Result.error("没有找到对应的话术");
        }
        PhrasesVO phrasesVO = new PhrasesVO(phrases);
        return Result.success(phrasesVO);
    }

}
