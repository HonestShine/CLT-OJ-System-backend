package com.clt.controller;

import com.clt.dto.UserChangeDTO;
import com.clt.entity.Result;
import com.clt.entity.User;
import com.clt.exception.InconsistentPasswordsException;
import com.clt.exception.NewPasswordIsNotValidException;
import com.clt.exception.NullUserException;
import com.clt.exception.SameOldAndNewPasswordsException;
import com.clt.utils.JwtUtil;
import com.clt.vo.AuthVO;
import com.clt.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
@RestController
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 注册
     */
    @PostMapping("/register")
    public Result register(@RequestBody User requestUser) {

        if (!(requestUser.getUsername().isEmpty() || requestUser.getPassword().isEmpty())) {

            if (!userService.isValidUsername(requestUser.getUsername())) {
                return Result.error("用户名不合法：只能使用字母和数字，不能以数字开头，且最多15个字符");
            }
            if (!userService.isValidPassword(requestUser.getPassword())) {
                return Result.error("密码强度不足：必须包含大写字母、小写字母、数字、特殊符号中的至少三类，且长度只能在8~16位之间");
            }

            if (userService.isExistByUsername(requestUser.getUsername())) {
                return Result.error("用户已被注册");
            }

            User responseUser = userService.register(requestUser.getUsername(), requestUser.getPassword());

            if (responseUser != null) {
                //获取token
                String token = jwtUtil.generateToken(responseUser);
                AuthVO authVO = new AuthVO(token, responseUser);
                return Result.success(authVO);
            }

            return Result.error("注册失败");
        }
        return Result.error("没有有效参数");
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        String username = user.getUsername();
        String password = user.getPassword();

        if (username.isEmpty()) {
            return Result.error("用户名不能为空");
        }
        if (password.isEmpty()) {
            return Result.error("密码不能为空");
        }

        User responseUser;

        if (!userService.isValidUsername(username)) {
            return Result.error("用户名只能使用字母和数字，不能以数字开头，且最多15个字符");
        }

        if (!userService.isExistByUsername(username)) {
            return Result.error("用户名或密码错误");
        }

        responseUser = userService.loginByUsername(username, password);

        if (responseUser == null) {
            return Result.error("用户名或密码错误");
        }

        //获取token
        String token = jwtUtil.generateToken(responseUser);
        AuthVO authVO = new AuthVO(token, responseUser);
        return Result.success(authVO);
    }

    /**
     * 忘记密码
     */
    @PutMapping("/change")
    public Result change(@RequestBody UserChangeDTO userChangeDTO) {
        if (userChangeDTO == null) {
            return Result.error("没有有效参数");
        }
        if (userChangeDTO.getUsername() == null || userChangeDTO.getUsername().isEmpty()) {
            return Result.error("用户名不能为空");
        }
        if (userChangeDTO.getOldPassword() == null || userChangeDTO.getOldPassword().isEmpty()) {
            return Result.error("旧密码不能为空");
        }
        if (userChangeDTO.getNewPassword() == null || userChangeDTO.getNewPassword().isEmpty()) {
            return Result.error("新密码不能为空");
        }

        try {
            userService.changePassword(userChangeDTO);
        } catch (NullUserException e) {
            return Result.error("用户不存在");
        } catch (SameOldAndNewPasswordsException e){
            return Result.error("新旧密码一致");
        } catch (InconsistentPasswordsException e) {
            return Result.error("密码错误");
        } catch (NewPasswordIsNotValidException e) {
            return Result.error("新密码强度不足：必须包含大写字母、小写字母、数字、特殊符号中的至少三类，且长度只能在8~16位之间");
        } catch (RuntimeException e) {
            return Result.error("系统内部错误");
        }

        return Result.success();
    }

    /**
     * 登出
     */
    @PostMapping("/logout")
    public Result logout(HttpServletRequest request) {

        if (jwtUtil.parseToken(request.getHeader("token")) == null) {
            return Result.error("未登录，无法登出");
        }

        // 使 Token 失效
        jwtUtil.invalidateToken(request.getHeader("token"));

        return Result.success("登出成功");
    }

}
