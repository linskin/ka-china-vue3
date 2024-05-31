import request from '@/utils/request'

export default () => {
    const addPost = async (data: PostModel.PostAddDto) => {
        return await request<number, ApiModel.IResponse<number>>({
            url: `/post/add`,
            method: 'POST',
            data: data
        })
    }
    const deletePost = async (data: ApiModel.IdRequest) => {
        return await request<boolean, ApiModel.IResponse<boolean>>({
            url: `/post/delete`,
            method: 'DELETE',
            data: data
        })
    }
    const deletePostBatch = async (data: ApiModel.IdsRequest) => {
        return await request<boolean, ApiModel.IResponse<boolean>>({
            url: `/post/delete/batch`,
            method: 'POST',
            data: data
        })
    }
    const updatePost = async (data: PostModel.PostUpdateDto) => {
        return await request<boolean, ApiModel.IResponse<boolean>>({
            url: `/post/update`,
            method: 'PUT',
            data: data
        })
    }
    const postDetail = async (postId: string) => {
        return await request<PostModel.PostVo, ApiModel.IResponse<PostModel.PostVo>>({
            url: `/post/detail/${postId}`,
            method: 'GET'
        })
    }
    const postFrontPage = async (data: PostModel.PostQueryDto) => {
        return await request<ApiModel.ApiPage<PostModel.PostSimpleVo>, ApiModel.IResponse<ApiModel.ApiPage<PostModel.PostSimpleVo>>>({
            url: `/post/page/front`,
            method: 'GET',
            params: data
        })
    }
    const postPage = async (data: PostModel.PostQueryDto) => {
        return await request<ApiModel.ApiPage<PostModel.PostVo>, ApiModel.IResponse<ApiModel.ApiPage<PostModel.PostVo>>>({
            url: `/post/page`,
            method: 'GET',
            params: data
        })
    }
    return {
        addPost,
        deletePost,
        deletePostBatch,
        updatePost,
        postDetail,
        postFrontPage,
        postPage
    }
}