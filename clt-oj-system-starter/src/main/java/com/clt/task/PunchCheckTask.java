package com.clt.task;

import com.clt.mapper.EChartsMapper;
import com.clt.mapper.UserMapper;
import com.clt.vo.AllUserResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Component
public class PunchCheckTask {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private EChartsMapper eChartsMapper;

    /**
     * 每天凌晨 01:00:00 检查所有用户的打卡状态
     * cron表达式：秒 分 时 日 月 周
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void checkAllUsersPunch() {
        log.info("开始执行每日打卡检查任务，时间：{}", LocalDateTime.now());

        try {
            List<AllUserResultVO> allUsers = userMapper.getAllUsers();

            if (allUsers == null || allUsers.isEmpty()) {
                log.info("没有用户需要检查");
                return;
            }

            int checkedCount = 0;
            int resetCount = 0;

            for (AllUserResultVO user : allUsers) {
                Integer userId = user.getId();

                OffsetDateTime lastSubmissionTime = eChartsMapper.getLastSubmissionTime(userId);

                if (lastSubmissionTime == null) {
                    log.debug("用户 {} 没有提交记录，跳过", userId);
                    continue;
                }

                LocalDate lastSubmissionDate = lastSubmissionTime.toLocalDate();
                LocalDate today = LocalDate.now();
                LocalDate yesterday = today.minusDays(1);

                long daysBetween = ChronoUnit.DAYS.between(lastSubmissionDate, today);

                if (daysBetween > 1 || lastSubmissionDate.isBefore(yesterday)) {
                    eChartsMapper.cleanPunchCount(userId);
                    resetCount++;
                    log.info("用户 {} 打卡中断，已重置打卡计数（最后提交日期：{}，今天：{}，间隔：{}天）",
                            userId, lastSubmissionDate, today, daysBetween);
                } else {
                    log.debug("用户 {} 打卡正常（最后提交日期：{}，今天：{}，间隔：{}天）",
                            userId, lastSubmissionDate, today, daysBetween);
                }

                checkedCount++;
            }

            log.info("每日打卡检查任务完成，共检查 {} 个用户，重置 {} 个用户", checkedCount, resetCount);

        } catch (Exception e) {
            log.error("每日打卡检查任务执行失败", e);
        }
    }
}
