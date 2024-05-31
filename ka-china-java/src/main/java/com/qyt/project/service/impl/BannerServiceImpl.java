package com.qyt.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qyt.project.common.ErrorCode;
import com.qyt.project.exception.BusinessException;
import com.qyt.project.mapper.BannerMapper;
import com.qyt.project.model.dto.banner.BannerAddDto;
import com.qyt.project.model.dto.banner.BannerUpdateDto;
import com.qyt.project.model.entity.Banner;
import com.qyt.project.service.BannerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * banner服务实现
 *
 * @author Linskin
 * @date 2023/6/13
 */
@Slf4j
@Service
public class BannerServiceImpl extends ServiceImpl<BannerMapper, Banner> implements BannerService {

    @Resource
    private BannerMapper bannerMapper;

    public void validData(String name, String cover) {
        if (StringUtils.isAnyBlank(name, cover)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "请求参数不完整");
        }
        if (StringUtils.isNotBlank(name) && (name.length() < 2 || name.length() > 10)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "名称长度需在2-10之间");
        }
    }

    @Override
    public long addBanner(BannerAddDto bannerAddDto, Long userId) {
        String name = bannerAddDto.getName();
        String cover = bannerAddDto.getCover();
        validData(name, cover);
        Banner banner = new Banner();
        banner.setName(name);
        banner.setCover(cover);
        banner.setUserId(userId);
        int insert = bannerMapper.insert(banner);
        if (insert <= 0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "创建失败，数据库错误");
        }
        return banner.getId();
    }

    @Override
    public boolean updateBanner(BannerUpdateDto bannerUpdateDto) {
        Long id = bannerUpdateDto.getId();
        String name = bannerUpdateDto.getName();
        String cover = bannerUpdateDto.getCover();
        if (id == null || id == 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "未收到bannerId");
        }
        validData(name, cover);
        UpdateWrapper<Banner> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id);
        updateWrapper.set("name", name);
        updateWrapper.set("cover", cover);
        int update = bannerMapper.update(new Banner(), updateWrapper);
        return update > 0;
    }
}
