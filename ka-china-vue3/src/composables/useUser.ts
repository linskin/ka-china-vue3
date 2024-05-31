import request from '@/utils/request'

export default () => {

    const userLogin = async (data: UserModel.UserLoginDto) => {
        return await request<UserModel.UserVo, ApiModel.IResponse<UserModel.UserVo>>({
            url: `/user/login`,
            method: 'POST',
            data: data
        })
    }

    const userRegister = async (data: UserModel.UserRegisterDto) => {
        return await request<number, ApiModel.IResponse<number>>({
            url: `/user/register`,
            method: 'POST',
            data: data
        })
    }

    const getCurrentUser = async () => {
        return await request<UserModel.UserVo, ApiModel.IResponse<UserModel.UserVo>>({
            url: `/user/current`,
            method: 'GET'
        })
    }

    const userLogout = async () => {
        return await request<boolean, ApiModel.IResponse<boolean>>({
            url: `/user/logout`,
            method: 'POST'
        })
    }

    const addUser = async (data: UserModel.UserAddDto) => {
        return await request<number, ApiModel.IResponse<number>>({
            url: `/user/add`,
            method: 'POST',
            data: data
        })
    }

    const deleteUser = async (data: ApiModel.IdRequest) => {
        return await request<boolean, ApiModel.IResponse<boolean>>({
            url: `/user/delete`,
            method: 'DELETE',
            data: data
        })
    }

    const updateUser = async (data: UserModel.UserUpdateDto) => {
        return await request<boolean, ApiModel.IResponse<boolean>>({
            url: `/user/update`,
            method: 'PUT',
            data: data
        })
    }

    const changeRole = async (data: ApiModel.IdRequest) => {
        return await request<boolean, ApiModel.IResponse<boolean>>({
            url: `/user/role/change`,
            method: 'POST',
            data: data
        })
    }

    const changePassword = async (data: UserModel.UserChangePasswordDto) => {
        return await request<boolean, ApiModel.IResponse<boolean>>({
            url: `/user/password/change`,
            method: 'POST',
            data: data
        })
    }

    const resetPassword = async (data: ApiModel.IdRequest) => {
        return await request<boolean, ApiModel.IResponse<boolean>>({
            url: `/user/password/reset`,
            method: 'POST',
            data: data
        })
    }

    const userPage = async (data: UserModel.UserQueryDto) => {
        return await request<ApiModel.ApiPage<UserModel.UserVo>, ApiModel.IResponse<ApiModel.ApiPage<UserModel.UserVo>>>({
            url: `/user/page`,
            method: 'GET',
            params: data
        })
    }
    return {
        userLogin,
        userRegister,
        getCurrentUser,
        userLogout,
        addUser,
        deleteUser,
        updateUser,
        changeRole,
        changePassword,
        resetPassword,
        userPage
    }
}