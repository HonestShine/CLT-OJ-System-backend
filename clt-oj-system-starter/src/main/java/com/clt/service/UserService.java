package com.clt.service;

import com.clt.dto.UserChangeDTO;
import com.clt.entity.User;
import com.clt.vo.AllUserResultVO;
import com.clt.vo.UserRankVO;
import com.clt.vo.UserResultVO;
import com.clt.vo.UserUpdateVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    /**
     * 用户名校验
     */
    boolean isValidUsername(String username);

    /**
     * 密码校验
     */
    boolean isValidPassword(String password);

    /**
     * 通过用户名判断用户是否存在
     */
    boolean isExistByUsername(String username);

    /**
     * 注册
     */
    User register(String username, String password);

    /**
     * 用户名密码登录
     */
    User loginByUsername(String username, String password);

    /**
     * 根据用户名获取密码
     */
    String getPasswordByUsername(String username);

    /**
     * 根据用户ID查询用户信息
     */
    User getUserById(Integer id);

    /**
     * 根据用户名查询用户信息
     */
    User getUserByUsername(String username);

    /**
     * 修改用户密码
     */
    boolean changePassword(UserChangeDTO userChangeDTO);

    /**
     * 获取所有用户信息
     */
    List<AllUserResultVO> getAllUsers();

    /**
     * 获取用户信息
     */
    UserResultVO getUserInfoById(Integer id);

    /**
     * 修改用户信息
     */
    void updateUserInfo(User user, Integer userId);

    /**
     * 获取用户信息
     */
    UserUpdateVO getUserInfo(Integer userId);

    /**
     * 上传用户头像
     */
    String uploadAvatar(String oldAvatarUrl, MultipartFile avatar, Integer userId);

    /**
     * 删除用户
     */
    boolean deleteUser(Integer userId);

    /**
     * 所有用户排名信息列表
     */
    List<UserRankVO> getAllUserRank();
}
