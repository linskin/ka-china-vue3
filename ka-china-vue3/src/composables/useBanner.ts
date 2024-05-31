import request from '@/utils/request'

export default () => {

    const addBanner = async (data: BannerModel.BannerAddDto) => {
        return await request<number, ApiModel.IResponse<number>>({
            url: `/banner/add`,
            method: 'POST',
            data: data
        })
    }

    const deleteBanner = async (data: ApiModel.IdRequest) => {
        return await request<boolean, ApiModel.IResponse<boolean>>({
            url: `/banner/delete`,
            method: 'DELETE',
            data: data
        })
    }

    const deleteBannerBatch = async (data: ApiModel.IdsRequest) => {
        return await request<boolean, ApiModel.IResponse<boolean>>({
            url: `/banner/delete/batch`,
            method: 'POST',
            data: data
        })
    }

    const updateBanner = async (data: BannerModel.BannerUpdateDto) => {
        return await request<boolean, ApiModel.IResponse<boolean>>({
            url: `/banner/update`,
            method: 'PUT',
            data: data
        })
    }

    const latestBanner = async () => {
        return await request<BannerModel.BannerVo, ApiModel.IResponse<BannerModel.BannerVo>>({
            url: `/banner/latest`,
            method: 'GET',
        })
    }

    const bannerPage = async (data: BannerModel.BannerQueryDto) => {
        return await request<ApiModel.ApiPage<BannerModel.BannerVo>, ApiModel.IResponse<ApiModel.ApiPage<BannerModel.BannerVo>>>({
            url: `/banner/page`,
            method: 'GET',
            params: data
        })
    }
    return {
        addBanner,
        deleteBanner,
        deleteBannerBatch,
        updateBanner,
        latestBanner,
        bannerPage
    }
}