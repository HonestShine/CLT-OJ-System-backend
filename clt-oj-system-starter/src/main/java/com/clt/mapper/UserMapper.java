package com.clt.mapper;

import com.clt.entity.User;
import com.clt.vo.AllUserResultVO;
import com.clt.vo.UserRankVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {

    /**
     * 通过用户名查询用户
     */
    User findByUsername(@Param("username") String username);

    /**
     * 插入用户
     */
    int insert(User user);

    /**
     * 根据用户名查询密码
     */
    String findPasswordByUsername(@Param("username") String username);

    /**
     * 根据用户名查询用户信息
     */
    User findUserByUsername(@Param("username") String username);

    /**
     * 根据用户ID查询用户信息
     */
    User findUserById(@Param("id") Integer id);

    /**
     * 修改用户密码
     */
    void changePassword(@Param("newPassword") String newPassword, @Param("username") String username);

    /**
     * 查询所有用户信息
     */
    List<AllUserResultVO> getAllUsers();

    /**
     * 更新用户信息
     */
    int updateUserInfo(@Param("user") User user, @Param("userId") Integer userId);

    /**
     * 删除用户
     */
    int deleteUser(Integer userId);

    /**
     * 获取所有用户排名
     */
    List<UserRankVO> getUsersRank();

    void addPunchCount(@Param("userId") Integer userId);
}
