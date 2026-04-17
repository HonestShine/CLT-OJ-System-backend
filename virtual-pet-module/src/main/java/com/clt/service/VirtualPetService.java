package com.clt.service;

import com.clt.entity.VirtualPet;
import com.clt.vo.BasicInformationOfVirtualPetVO;
import com.clt.vo.PunchVO;
import com.clt.vo.UpdateExperienceVO;
import com.clt.vo.UpdateVirtualPetVO;

public interface VirtualPetService {

    /**
     * 获取虚拟宠物
     */
    BasicInformationOfVirtualPetVO getVirtualPet(Integer userId);

    /**
     * 更新虚拟宠物
     */
    UpdateVirtualPetVO updateVirtualPet(VirtualPet virtualPet);

    /**
     * 签到
     */
    PunchVO punch(Integer userId);

    /**
     * 增加经验
     */
    UpdateExperienceVO updateExperience(VirtualPet virtualPet);

    /**
     * 获取话术
     */
    String getPhrases(String submissionStatus, Integer userId);
}
