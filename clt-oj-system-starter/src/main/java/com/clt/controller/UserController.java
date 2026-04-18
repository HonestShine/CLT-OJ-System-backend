package com.clt.controller;

import com.clt.entity.Result;
import com.clt.entity.User;
import com.clt.exception.FileStorageSpaceOutOfRangeException;
import com.clt.exception.FileSuffixException;
import com.clt.exception.NullFileException;
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
            return Result.error("404", "获取用户信息失败");
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
            return Result.error("至少需要昵称/爱好/简介其中一项信息");
        }
        //更新用户信息
        try {
            userService.updateUserInfo(user, userId);
        } catch (RuntimeException e) {
            return Result.error("400", e.getMessage());
        }
        UserUpdateVO result = userService.getUserInfo(userId);
        return userService.getUserInfo(userId) != null ? Result.success(result) : Result.error("500", "服务器错误");
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
        String url;
        try {
            url = userService.uploadAvatar(oldAvatar, newAvatar, userId);
        }catch (NullFileException e) {
            return Result.error("文件内容不能为空");
        }catch (FileSuffixException e) {
            return Result.error("文件格式错误(只支持 JPG|JPEG、PNG、GIF 格式图片)");
        }catch (FileStorageSpaceOutOfRangeException e) {
            return Result.error("文件大小不能超过5MB");
        }catch (Exception e) {
            return Result.error("500", e.getMessage());
        }

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
