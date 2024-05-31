declare namespace UserModel {
    interface UserLoginDto {
        account: string;
        password: string;
    }

    interface UserRegisterDto {
        name: string;
        account: string;
        password: string;
        rePassword: string;
    }

    interface UserAddDto {
        name: string;
        account: string;
        role: string;
    }

    interface UserUpdateDto {
        id: number;
        name: string;
        avatar: string;
    }

    interface UserQueryDto extends ApiModel.PageRequest{
        name?: string;
        account?: string;
    }

    interface UserChangePasswordDto {
        account: string;
        oldPassword: string;
        newPassword: string;
        reNewPassword: string;
    }

    interface UserSimpleVo {
        id: number;
        name: string;
        account: string;
        avatar: string;
        role: string;
    }

    interface UserVo {
        id: number;
        name: string;
        account: string;
        avatar: string;
        role: string;
        createTime: string;
        updateTime: string;
    }
}