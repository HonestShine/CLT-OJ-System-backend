package com.clt.controller;

import com.clt.entity.Result;
import com.clt.entity.User;
import com.clt.service.UserService;
import com.clt.utils.JwtUtil;
import com.clt.vo.AllUserResultVO;
import com.clt.vo.UserRankVO;
import com.clt.vo.UserResultVO;
import com.clt.vo.UserUpdateVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;


@RequestMapping("/users")
@RestController
public class UserController {

    @Autowired
    private UserService userService;


    @Autowired
    private JwtUtil jwtUtil;

    //获取所有用户的信息
    @GetMapping
    public Result getAllUsers() {
        List<AllUserResultVO> users = userService.getAllUsers();
        if (users == null) {
            return Result.error("获取用户信息失败");
        }
        return Result.success(users);
    }

    //获取指定用户信息
    @GetMapping("/{id}")
    public Result getUserById(@PathVariable Integer id) {
        UserResultVO user = userService.getUserInfoById(id);
        if (user == null) {
            return Result.error("404", "用户ID：" + id + "不存在");
        }
        return Result.success(user);
    }

    //获取当前用户信息
    @GetMapping("/me")
    public Result getCurrentUserInfo(@RequestHeader("token") String token) {
        //利用token获取用户ID
        Integer userId = jwtUtil.parseToken(token).get("id", Integer.class);
        UserResultVO user = userService.getUserInfoById(userId);
        if (user == null) {
            return Result.error("401", "未登录");
        }
        return Result.success(user);
    }

    //更新用户信息
    @PutMapping
    public Result updateUserInfo(@RequestBody User user, @RequestHeader("token") String token) {
        //利用token获取用户ID
        Integer userId = jwtUtil.parseToken(token).get("id", Integer.class);
        if (
                (user.getNickname() == null || user.getNickname().isEmpty())
                || user.getHobby() == null
                || user.getIntroduction() == null
        ) {
            return Result.error("请填写需要更新的信息");
        }
        //更新用户信息
        userService.updateUserInfo(user, userId);
        UserUpdateVO result = userService.getUserInfo(userId);
        return Result.success(result);
    }

    /**
     * 上传用户头像
     */
    @PostMapping("/upload")
    public Result uploadAvatar(@RequestParam("newAvatar") MultipartFile newAvatar, @RequestHeader("token") String token) {
        //利用token获取用户ID和头像
        Integer userId = jwtUtil.parseToken(token).get("id", Integer.class);
        String oldAvatar = jwtUtil.parseToken(token).get("avatar", String.class);
        if (newAvatar == null || newAvatar.isEmpty()) {
            return Result.error("文件不能为空");
        }
        //上传文件
        String url = userService.uploadAvatar(oldAvatar, newAvatar, userId);
        if (url == null) {
            return Result.error("上传失败");
        }
        return Result.success(url);
    }

    /**
     * 删除用户
     */
    @DeleteMapping
    public Result deleteUser(@RequestHeader("token") String token) {
        //利用token获取用户ID
        Integer userId = jwtUtil.parseToken(token).get("id", Integer.class);
        //删除用户
        boolean result = userService.deleteUser(userId);
        if (!result) {
            return Result.error("404", "用户ID：" + userId + "不存在");
        }
        return Result.success();
    }

    /**
     * 所有用户排名信息列表
     */
    @GetMapping("/rank")
    public Result getAllUserRank() {
        List<UserRankVO> result = userService.getAllUserRank();
        if (result == null) {
            return Result.error("404", "没有可用用户信息");
        }
        return Result.success(result);
    }
}
