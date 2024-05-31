package com.qyt.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.qyt.project.annotation.AuthCheck;
import com.qyt.project.annotation.LogAnnotation;
import com.qyt.project.common.ErrorCode;
import com.qyt.project.common.IdRequest;
import com.qyt.project.common.IdsRequest;
import com.qyt.project.common.ResultUtils;
import com.qyt.project.constant.UserConstant;
import com.qyt.project.exception.BusinessException;
import com.qyt.project.model.dto.banner.BannerAddDto;
import com.qyt.project.model.dto.banner.BannerQueryDto;
import com.qyt.project.model.dto.banner.BannerUpdateDto;
import com.qyt.project.model.entity.Banner;
import com.qyt.project.model.vo.BannerVo;
import com.qyt.project.model.vo.user.UserSimpleVo;
import com.qyt.project.model.vo.user.UserVo;
import com.qyt.project.service.BannerService;
import com.qyt.project.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * banner接口控制器
 *
 * @author Linskin
 * @date 2023/6/13
 */
@RestController
@RequestMapping("/banner")
public class BannerController {

    @Resource
    private BannerService bannerService;

    @Resource
    private UserService userService;

    /**
     * banner添加
     *
     * @param bannerAddDto banner添加信息
     * @param request 请求
     * @return bannerId
     */
    @PostMapping("/add")
    @AuthCheck(anyRole = {UserConstant.SUPER_ADMIN, UserConstant.ADMIN_ROLE})
    @LogAnnotation(title = "banner添加")
    public ResultUtils<Long> addBanner(@RequestBody BannerAddDto bannerAddDto, HttpServletRequest request) {
        if (bannerAddDto == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserVo currentUser = userService.getCurrentUser(request);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        long bannerId = bannerService.addBanner(bannerAddDto, currentUser.getId());
        return ResultUtils.success(bannerId, "创建成功");
    }

    /**
     * banner删除
     *
     * @param idRequest bannerId
     * @return 状态
     */
    @DeleteMapping("/delete")
    @AuthCheck(anyRole = {UserConstant.SUPER_ADMIN, UserConstant.ADMIN_ROLE})
    @LogAnnotation(title = "banner删除")
    public ResultUtils<Boolean> deleteBanner(@RequestBody IdRequest idRequest) {
        if (idRequest == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean delete = bannerService.removeById(idRequest.getId());
        return ResultUtils.success(delete, "删除成功");
    }

    /**
     * banner批量删除
     *
     * @param idsRequest bannerId集合
     * @return 状态
     */
    @PostMapping("/delete/batch")
    @AuthCheck(anyRole = {UserConstant.SUPER_ADMIN, UserConstant.ADMIN_ROLE})
    @LogAnnotation(title = "banner批量删除")
    public ResultUtils<Boolean> deleteBannerBatch(@RequestBody IdsRequest idsRequest) {
        if (idsRequest == null || idsRequest.getIds().size() == 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean delete = bannerService.removeBatchByIds(idsRequest.getIds());
        return ResultUtils.success(delete);
    }

    /**
     * banner更新
     *
     * @param bannerUpdateDto banner更新信息
     * @return 状态
     */
    @PutMapping("/update")
    @AuthCheck(anyRole = {UserConstant.SUPER_ADMIN, UserConstant.ADMIN_ROLE})
    @LogAnnotation(title = "banner更新")
    public ResultUtils<Boolean> updateBanner(@RequestBody BannerUpdateDto bannerUpdateDto) {
        if (bannerUpdateDto == null || bannerUpdateDto.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean update = bannerService.updateBanner(bannerUpdateDto);
        return ResultUtils.success(update, "更新成功");
    }

    /**
     * 获取banner最新一条数据
     *
     * @return banner返回实体
     */
    @GetMapping("/latest")
    @LogAnnotation(title = "获取banner最新一条数据")
    public ResultUtils<BannerVo> latestBanner() {
        QueryWrapper<Banner> queryWrapper = new QueryWrapper<>();
        queryWrapper.last("limit 1");
        queryWrapper.orderByDesc("updateTime");
        Banner banner = bannerService.getOne(queryWrapper);
        if (banner == null) {
            return ResultUtils.success(new BannerVo());
        }
        BannerVo bannerVo = new BannerVo();
        BeanUtils.copyProperties(banner, bannerVo);
        UserSimpleVo userSimpleVo = userService.getUserSimpleById(banner.getUserId());
        bannerVo.setUserInfo(userSimpleVo);
        return ResultUtils.success(bannerVo);
    }

    /**
     * banner分页
     *
     * @param bannerQueryDto banner查询信息
     * @return banner分页数据
     */
    @GetMapping("/page")
    @AuthCheck(anyRole = {UserConstant.SUPER_ADMIN, UserConstant.ADMIN_ROLE})
    @LogAnnotation(title = "banner分页")
    public ResultUtils<Page<BannerVo>> bannerPage(BannerQueryDto bannerQueryDto) {
        long current = 1;
        long pageSize = 10;
        String name = null;
        if (bannerQueryDto != null) {
            current = bannerQueryDto.getCurrent();
            pageSize = bannerQueryDto.getPageSize();
            name = bannerQueryDto.getName();
        }
        QueryWrapper<Banner> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);
        queryWrapper.orderByDesc("updateTime");
        Page<Banner> bannerPage = bannerService.page(new Page<>(current, pageSize), queryWrapper);
        Page<BannerVo> bannerVoPage = new PageDTO<>(bannerPage.getCurrent(), bannerPage.getSize(), bannerPage.getTotal());
        List<BannerVo> bannerVoList = bannerPage.getRecords().stream().map(banner -> {
            BannerVo bannerVo = new BannerVo();
            BeanUtils.copyProperties(banner, bannerVo);
            UserSimpleVo userSimpleVo = userService.getUserSimpleById(banner.getUserId());
            bannerVo.setUserInfo(userSimpleVo);
            return bannerVo;
        }).collect(Collectors.toList());
        bannerVoPage.setRecords(bannerVoList);
        return ResultUtils.success(bannerVoPage);
    }
}
