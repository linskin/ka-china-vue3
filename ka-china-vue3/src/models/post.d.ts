declare namespace PostModel {
    interface PostAddDto {
        title: string;
        cover: string;
        description: string;
        content: string;
        categoryIds: number[];
    }

    interface PostUpdateDto {
        id: number;
        title: string;
        cover: string;
        description: string;
        content: string;
        categoryIds: number[];
    }

    interface PostQueryDto extends ApiModel.PageRequest{
        title?: string;
    }

    interface PostVo {
        id: number;
        title: string;
        cover: string;
        description: string;
        content: string;
        categories: CategoryModel.CategorySimpleVo[];
        userInfo?: UserModel.UserSimpleVo;
        createTime: string;
        updateTime: string;
    }
    interface PostSimpleVo {
        id: number;
        title: string;
        cover: string;
        description: string;
        categories: CategoryModel.CategorySimpleVo[];
        userInfo?: UserModel.UserSimpleVo;
        createTime: string;
        updateTime: string;
    }
}