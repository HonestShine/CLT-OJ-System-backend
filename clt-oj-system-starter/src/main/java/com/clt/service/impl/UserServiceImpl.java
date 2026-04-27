package com.clt.service.impl;

import com.clt.dto.UserChangeDTO;
import com.clt.entity.User;
import com.clt.enums.UserType;
import com.clt.exception.UserHobbyOutOfRangeException;
import com.clt.exception.UserIntroductionOutOfRangeException;
import com.clt.exception.UserNicknameOutOfRangeException;
import com.clt.mapper.SolvedProblemCountMapper;
import com.clt.mapper.SubmissionMapper;
import com.clt.mapper.UserMapper;
import com.clt.service.UserService;
import com.clt.utils.AliyunOSSUtil;
import com.clt.vo.AllUserResultVO;
import com.clt.vo.UserRankVO;
import com.clt.vo.UserResultVO;
import com.clt.vo.UserUpdateVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SolvedProblemCountMapper solvedProblemCountMapper;

    @Autowired
    private SubmissionMapper submissionMapper;

    @Autowired
    private AliyunOSSUtil aliyunOSSUtil;

    // 校验规则常量定义
    private static final String USERNAME_REGEX = "^[a-zA-Z][a-zA-Z0-9]*$";
    private static final String PASSWORD_UPPERCASE_REGEX = ".*[A-Z].*";
    private static final String PASSWORD_LOWERCASE_REGEX = ".*[a-z].*";
    private static final String PASSWORD_DIGIT_REGEX = ".*\\d.*";
    private static final String PASSWORD_SPECIAL_REGEX = ".*[.#?+\\-*].*";

    // 长度限制常量
    private static final int MAX_USERNAME_LENGTH = 15;
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_PASSWORD_LENGTH = 16;

    //默认头像
    private static final String DEFAULT_AVATAR_OBJECT_KEY = "2026/04/ca39bd34-5f39-4817-91be-1575ff500c3c.jpg";

    /**
     * 校验用户名合法性
     * 规则：只能用大、小写英文字母和数字，不能为空字符串，不能以数字开头，不能超过15个字符
     */
    public boolean isValidUsername(String username) {
        if (username == null || username.isEmpty()) {
            return false;
        }
        if (username.length() > MAX_USERNAME_LENGTH) {
            return false;
        }
        return username.matches(USERNAME_REGEX);
    }

    /**
     * 校验密码强度
     * 规则：至少包含大写字母、小写字母、数字、特殊符号（.#?+-*）中的三类，长度8-16位
     */
    public boolean isValidPassword(String password) {
        if (password == null || password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH) {
            return false;
        }

        // 检查是否只包含允许的字符
        if (!password.matches("^[a-zA-Z0-9.#?+\\-*]+$")) {
            return false;
        }

        // 统计满足的类别数量
        int categoryCount = 0;
        if (password.matches(PASSWORD_UPPERCASE_REGEX)) categoryCount++;
        if (password.matches(PASSWORD_LOWERCASE_REGEX)) categoryCount++;
        if (password.matches(PASSWORD_DIGIT_REGEX)) categoryCount++;
        if (password.matches(PASSWORD_SPECIAL_REGEX)) categoryCount++;

        return categoryCount >= 3;
    }

    /**
     * 通过用户名判断是否存在
     */
    @Override
    public boolean isExistByUsername(String username) {
        return userMapper.findByUsername(username) != null;
    }

    /**
     * 注册
     */
    @Override
    public User register(String username, String password) {

        User user = new User(null, username, password, UserType.USER.code, LocalDateTime.now(), generateRandomName(), null, null, null);

        int result = userMapper.insert(user);

        if (result == 0) {
            return null;
        }
        return userMapper.findUserByUsername(username);
    }

    /**
     * 随机生成名称
     */
    public String generateRandomName() {
        return "User_" + (int) (Math.random() * 1000000);
    }

    /**
     * 用户名密码登录
     */
    @Override
    public User loginByUsername(String username, String password) {
        if (userMapper.findPasswordByUsername(username).equals(password)) {
            return userMapper.findUserByUsername(username);
        }
        return null;
    }

    /**
     * 通过用户名获取密码
     */
    @Override
    public String getPasswordByUsername(String username) {
        return userMapper.findPasswordByUsername(username);
    }

    /**
     * 通过用户ID获取用户信息
     */
    @Override
    public User getUserById(Integer id) {
        return userMapper.findUserById(id);
    }

    /**
     * 通过用户名获取用户信息
     */
    @Override
    public User getUserByUsername(String username) {
        return userMapper.findUserByUsername(username);
    }

    /**
     * 修改用户密码
     */
    @Override
    public boolean changePassword(UserChangeDTO userChangeDTO) {
        if (getPasswordByUsername(userChangeDTO.getUsername()) == null || getPasswordByUsername(userChangeDTO.getUsername()).isEmpty()) {
            return false;
        }
        userMapper.changePassword(userChangeDTO.getNewPassword(), userChangeDTO.getUsername());

        return true;
    }

    /**
     * 获取所有用户信息
     */
    @Override
    public List<AllUserResultVO> getAllUsers() {
        List<AllUserResultVO> results = userMapper.getAllUsers();
        results.forEach(result -> {
            result.setSolvedCount(solvedProblemCountMapper.getSolvedCount(result.getId()));
            result.setSubmissionCount(solvedProblemCountMapper.getSubmissionCount(result.getId()));
        });
        if (results.isEmpty()) {
            return null;
        }
        return results;
    }

    /**
     * 获取指定用户信息
     */
    @Override
    public UserResultVO getUserInfoById(Integer id) {
        User user = userMapper.findUserById(id);

        if (user == null) {
            return null;
        }

        return new UserResultVO(
                user.getId(),
                user.getUsername(),
                user.getNickname(),
                user.getAvatar(),
                user.getHobby(),
                user.getIntroduction(),
                solvedProblemCountMapper.getSolvedCount(id),
                solvedProblemCountMapper.getSubmissionCount(id),
                submissionMapper.getLastSubmissionTime(id)
        );
    }

    /**
     * 更新用户信息
     */
    @Override
    public void updateUserInfo(User user, Integer userId) throws RuntimeException {
        if (user.getNickname().length() > 15) {
            throw new UserNicknameOutOfRangeException("用户昵称长度超出限制(15个字符)");
        }
        if (user.getHobby().length() > 200) {
            throw new UserHobbyOutOfRangeException("用户爱好长度超出限制(200个字符)");
        }
        if (user.getIntroduction().length() > 200) {
            throw new UserIntroductionOutOfRangeException("用户简介长度超出限制(200个字符)");
        }

        userMapper.updateUserInfo(user, userId);
    }

    /**
     * 获取用户信息
     */
    @Override
    public UserUpdateVO getUserInfo(Integer userId) {
        User user = userMapper.findUserById(userId);

        if (user == null) {
            return null;
        }

        return new UserUpdateVO(
                user.getId(),
                user.getNickname(),
                user.getHobby(),
                user.getAvatar(),
                user.getIntroduction(),
                solvedProblemCountMapper.getSolvedCount(userId),
                solvedProblemCountMapper.getSubmissionCount(userId),
                submissionMapper.getLastSubmissionTime(userId)
        );
    }

    /**
     * 上传头像
     */
    @Override
    public String uploadAvatar(String oldAvatarUrl, MultipartFile avatar, Integer userId) throws Exception {
        String fileName;

        //删除原有头像
        //从URL地址中获取文件名  例如：url：https://clt-oj-avatar-store.oss-cn-chengdu.aliyuncs.com/2026/04/7e6fd37a-9329-496e-a52d-900d509fe587.jpg
        int schemeEndIndex = oldAvatarUrl.indexOf("://") + 3;  // 找到 "://" 后的位置 +3 = 协议结束后的位置
        int slashIndex = oldAvatarUrl.indexOf('/', schemeEndIndex);  // 找到域名后的第一个 '/'
        String objectKey = oldAvatarUrl.substring(slashIndex + 1);  // 取 '/' 后面的内容
        if (objectKey.isEmpty()) {
            throw new RuntimeException("原头像文件获取失败");
        }
        //排除默认头像
        if (!objectKey.equalsIgnoreCase(DEFAULT_AVATAR_OBJECT_KEY)) {
            aliyunOSSUtil.delete(objectKey);
        }
        //上传头像
        fileName = aliyunOSSUtil.upload(avatar.getBytes(), avatar.getOriginalFilename());
        userMapper.updateUserInfo(new User(userId, null, null, null, null, null, null, null, fileName), userId);

        return fileName;
    }

    /**
     * 删除用户
     */
    @Override
    public boolean deleteUser(Integer userId) {
        return userMapper.deleteUser(userId) > 0;
    }

    /**
     * 获取所有用户排名
     */
    @Override
    public List<UserRankVO> getAllUserRank() {
        List<UserRankVO> results = userMapper.getUsersRank();
        AtomicInteger rank = new AtomicInteger(1);
        results.forEach(result -> {
            result.setSolvedCount(solvedProblemCountMapper.getSolvedCount(result.getUserId()));
            result.setSubmissionCount(solvedProblemCountMapper.getSubmissionCount(result.getUserId()));
            //将通过率acceptance的格式设置为两位小数+%，如0.123456789 -> 12.35%
            result.setAcceptance(result.getSubmissionCount() == 0 ? "0.00%" : (String.format("%.2f%%", result.getSolvedCount() * 100.0 / result.getSubmissionCount())));
            result.setRank(rank.getAndIncrement());
        });
        return results;
    }

    /**
     * 打卡
     */
    @Override
    public void punch(Integer userId) {
        LocalDateTime leastSubmissionDateTime = submissionMapper.getLastSubmissionTime(userId);
        if (leastSubmissionDateTime == null) {
            // 打卡
            userMapper.addPunchCount(userId);
            return;
        }
        LocalDate leastSubmissionDate = leastSubmissionDateTime.toLocalDate();
        if (ChronoUnit.DAYS.between(leastSubmissionDate, LocalDate.now()) >= 1) {
            // 打卡
            userMapper.addPunchCount(userId);
        }
    }
}
