package com.clt.mapper;

import com.clt.entity.VirtualPet;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface VirtualPetMapper {
    /**
     * 获取虚拟宠物
     */
    VirtualPet getVirtualPet(@Param("userId") Integer userId);

    /**
     * 获取虚拟宠物id
     */
    Integer getIdByUserId(@Param("userId") Integer userId);

    /**
     * 创建虚拟宠物
     */
    void createVirtualPet(@Param("userId") Integer userId, @Param("petName") String petName);

    /**
     * 更新虚拟宠物
     */
    void updateVirtualPet(@Param("virtualPet") VirtualPet virtualPet);

    /**
     * 获取虚拟宠物经验
     */
    BigInteger getExperience(@Param("id") Integer id);

    /**
     * 获取虚拟宠物打卡时间
     */
    LocalDateTime getPunchDate(@Param("userId") Integer userId);

    /**
     * 获取虚拟宠物打卡次数
     */
    Integer getNumberOfPunchOuts(@Param("userId") Integer userId);

    /**
     * 获取话术
     */
    List<String> getAcceptedPhrases(@Param("id") Integer id);

    List<String> getWrongAnswerPhrases(@Param("id") Integer id);

    List<String> getTimeLimitExceededPhrases(Integer id);

    List<String> getCompileErrorPhrases(Integer id);

    List<String> getRuntimeErrorPhrases(Integer id);

    List<String> getInternalErrorPhrases(Integer id);

    List<String> getExecFormatErrorPhrases(Integer id);

    List<String> getMemoryOutOfRangePhrases(Integer id);

    /**
     * 获取虚拟宠物名称
     */
    String getNameById(Integer id);
}
