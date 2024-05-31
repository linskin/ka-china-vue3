declare namespace CategoryModel {
    interface CategoryAddDto {
        name: string;
    }

    interface CategoryUpdateDto {
        id: number;
        name: string;
    }

    interface CategoryQueryDto extends ApiModel.PageRequest{
        name?: string;
    }

    interface CategorySimpleVo {
        id: number;
        name: string;
    }

    interface CategoryVo {
        id: number;
        name: string;
        userInfo: UserModel.UserSimpleVo;
        createTime: string;
        updateTime: string;
    }
}