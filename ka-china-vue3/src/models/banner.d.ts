declare namespace BannerModel {
    interface BannerAddDto {
        name: string;
        cover: string;
    }

    interface BannerUpdateDto {
        id: number;
        name: string;
        cover: string;
    }

    interface BannerQueryDto extends ApiModel.PageRequest{
        name?: string;
    }

    interface BannerVo {
        id: number;
        name: string;
        cover: string;
        userInfo: UserModel.UserSimpleVo;
        createTime: string;
        updateTime: string;
    }
}