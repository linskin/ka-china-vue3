package com.qyt.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qyt.project.model.dto.banner.BannerAddDto;
import com.qyt.project.model.dto.banner.BannerUpdateDto;
import com.qyt.project.model.entity.Banner;

/**
 * banner服务
 *
 * @author Linskin
 * @date 2023/6/13
 */
public interface BannerService extends IService<Banner> {

    /**
     * banner创建
     *
     * @param bannerAddDto banner添加信息
     * @param userId 用户ID
     * @return bannerId
     */
    long addBanner(BannerAddDto bannerAddDto, Long userId);

    /**
     * banner更新
     *
     * @param bannerUpdateDto banner更新信息
     * @return 状态
     */
    boolean updateBanner(BannerUpdateDto bannerUpdateDto);

}
