package com.qyt.project;

import com.qyt.project.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * 用户服务测试
 *
 * @author QYT-WNX
 * @date 2023/6/12
 */
@Slf4j
@SpringBootTest
public class UserServiceTests {

    @Resource
    private UserService userService;

}
