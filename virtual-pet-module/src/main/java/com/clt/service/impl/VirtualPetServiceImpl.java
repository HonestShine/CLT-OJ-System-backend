package com.clt.service.impl;

import com.clt.entity.VirtualPet;
import com.clt.enums.SubmissionStatus;
import com.clt.mapper.VirtualPetMapper;
import com.clt.service.VirtualPetService;
import com.clt.vo.BasicInformationOfVirtualPetVO;
import com.clt.vo.PunchVO;
import com.clt.vo.UpdateExperienceVO;
import com.clt.vo.UpdateVirtualPetVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class VirtualPetServiceImpl implements VirtualPetService {

    @Autowired
    private VirtualPetMapper virtualPetMapper;

    /**
     * 获取虚拟宠物
     */
    @Override
    public BasicInformationOfVirtualPetVO getVirtualPet(Integer userId) {
        Integer virtualPetId = virtualPetMapper.getIdByUserId(userId);
        if (virtualPetId == null || virtualPetId <= 0) {
            //随机生成宠物名称
            String name = "CLT-" + (int)(Math.random() * 1000000);
            virtualPetMapper.createVirtualPet(userId, name);
        }
        VirtualPet virtualPet = virtualPetMapper.getVirtualPet(userId);
        return new BasicInformationOfVirtualPetVO(virtualPet.getId(),virtualPet.getUserId(), virtualPet.getName(), virtualPet.getExperience(), virtualPet.getLevel(), virtualPet.getCreatedAt(), virtualPet.getPunchDate(), virtualPet.getNumberOfPunchOuts());
    }

    /**
     * 更新虚拟宠物
     */
    @Override
    public UpdateVirtualPetVO updateVirtualPet(VirtualPet virtualPet) {
        virtualPetMapper.updateVirtualPet(virtualPet);
        String name = virtualPetMapper.getNameById(virtualPet.getId());
        return new UpdateVirtualPetVO(virtualPet.getId(), name);
    }

    /**
     *  签到
     */
    @Override
    public PunchVO punch(Integer userId) {
        //获取打卡时间
        LocalDateTime previousPunchDateTime = virtualPetMapper.getPunchDate(userId);
        VirtualPet virtualPet = new VirtualPet();
        virtualPet.setId(virtualPetMapper.getIdByUserId(userId));
        if (previousPunchDateTime == null || ChronoUnit.DAYS.between(previousPunchDateTime.toLocalDate(), LocalDateTime.now().toLocalDate()) == 1) {
            virtualPet.setPunchDate(LocalDateTime.now());
            virtualPet.setNumberOfPunchOuts(virtualPetMapper.getNumberOfPunchOuts(userId) + 1);
            virtualPetMapper.updateVirtualPet(virtualPet);
        } else if (ChronoUnit.DAYS.between(previousPunchDateTime.toLocalDate(), LocalDateTime.now().toLocalDate()) > 1) {
            virtualPet.setPunchDate(LocalDateTime.now());
            virtualPet.setNumberOfPunchOuts(1);
            virtualPetMapper.updateVirtualPet(virtualPet);
        }
        return new PunchVO(virtualPet.getId(), virtualPet.getPunchDate(), virtualPet.getNumberOfPunchOuts());
    }

    /**
     * 增加经验
     */
    @Override
    public UpdateExperienceVO updateExperience(VirtualPet virtualPet) {
        BigInteger oldExperience = virtualPetMapper.getExperience(virtualPet.getId());
        BigInteger newExperience = oldExperience.add(virtualPet.getExperience());
        int newLevel = getLevel(newExperience);
        virtualPet.setExperience(newExperience);
        virtualPet.setLevel(newLevel);
        virtualPetMapper.updateVirtualPet(virtualPet);
        return new UpdateExperienceVO(virtualPet.getId(), newExperience, newLevel);
    }

    /**
     * 返回话术
     */
    @Override
    public String getPhrases(String submissionStatus, Integer userId) {
        Integer id = virtualPetMapper.getIdByUserId(userId);
        List<String> phrases = new ArrayList<>();
        if (SubmissionStatus.ACCEPTED.message.equalsIgnoreCase(submissionStatus)) {
            phrases = virtualPetMapper.getAcceptedPhrases(id);
        }else if (SubmissionStatus.WRONG_ANSWER.message.equalsIgnoreCase(submissionStatus)) {
            phrases = virtualPetMapper.getWrongAnswerPhrases(id);
        } else if (SubmissionStatus.TIME_LIMIT_EXCEEDED.message.equalsIgnoreCase(submissionStatus)) {
            phrases = virtualPetMapper.getTimeLimitExceededPhrases(id);
        } else if (SubmissionStatus.COMPILE_ERROR.message.equalsIgnoreCase(submissionStatus)) {
            phrases = virtualPetMapper.getCompileErrorPhrases(id);
        } else if (SubmissionStatus.RUNTIME_ERROR_SIGSEGV.message.equalsIgnoreCase(submissionStatus)
        || SubmissionStatus.RUNTIME_ERROR_SIGXFSZ.message.equalsIgnoreCase(submissionStatus)
        || SubmissionStatus.RUNTIME_ERROR_SIGFPE.message.equalsIgnoreCase(submissionStatus)
        || SubmissionStatus.RUNTIME_ERROR_SIGABRT.message.equalsIgnoreCase(submissionStatus)
        || SubmissionStatus.RUNTIME_ERROR_NZEC.message.equalsIgnoreCase(submissionStatus)
        || SubmissionStatus.RUNTIME_ERROR_OTHER.message.equalsIgnoreCase(submissionStatus)) {
            phrases = virtualPetMapper.getRuntimeErrorPhrases(id);
        }else if (SubmissionStatus.INTERNAL_ERROR.message.equalsIgnoreCase(submissionStatus)) {
            phrases = virtualPetMapper.getInternalErrorPhrases(id);
        }else if (SubmissionStatus.EXEC_FORMAT_ERROR.message.equalsIgnoreCase(submissionStatus)) {
            phrases = virtualPetMapper.getExecFormatErrorPhrases(id);
        }else if (SubmissionStatus.MEMORY_OUT_OF_RANGE.message.equalsIgnoreCase(submissionStatus)) {
            phrases = virtualPetMapper.getMemoryOutOfRangePhrases(id);
        }
        if (phrases.isEmpty()) {
            return null;
        }
        //获取随机索引
        int index = (int)(Math.random() * phrases.size());
        return binarySearch(phrases, index);
    }

    /**
     * 根据经验值转换等级
     */
    private int getLevel(BigInteger experience) {
        if (experience == null || experience.signum() <= 0) {
            return 1;
        }

        int maxLevel = 100;

        int level = (int) Math.sqrt(experience.doubleValue() / 100.0);

        return Math.max(1, Math.min(level, maxLevel));
    }

    /**
     * 二分查找查找指定索引的字符串
     */
    private String binarySearch(List<String> list, int index) {
        int left = 0;
        int right = list.size() - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            int compare = list.get(mid).compareTo(list.get(index));
            if (compare == 0) {
                return list.get(mid);
            } else if (compare < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return null;
    }

}
