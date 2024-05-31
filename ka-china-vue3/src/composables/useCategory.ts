import request from '@/utils/request'

export default () => {

    const addCategory = async (data: CategoryModel.CategoryAddDto) => {
        return await request<number, ApiModel.IResponse<number>>({
            url: `/category/add`,
            method: 'POST',
            data: data
        })
    }

    const deleteCategory = async (data: ApiModel.IdRequest) => {
        return await request<boolean, ApiModel.IResponse<boolean>>({
            url: `/category/delete`,
            method: 'DELETE',
            data: data
        })
    }

    const deleteCategoryBatch = async (data: ApiModel.IdsRequest) => {
        return await request<boolean, ApiModel.IResponse<boolean>>({
            url: `/category/delete/batch`,
            method: 'POST',
            data: data
        })
    }

    const updateCategory = async (data: CategoryModel.CategoryUpdateDto) => {
        return await request<boolean, ApiModel.IResponse<boolean>>({
            url: `/category/update`,
            method: 'PUT',
            data: data
        })
    }

    const categoryList = async () => {
        return await request<CategoryModel.CategorySimpleVo[], ApiModel.IResponse<CategoryModel.CategorySimpleVo[]>>({
            url: `/category/list`,
            method: 'GET',
        })
    }

    const categoryPage = async (data: CategoryModel.CategoryQueryDto) => {
        return await request<ApiModel.ApiPage<CategoryModel.CategoryVo>, ApiModel.IResponse<ApiModel.ApiPage<CategoryModel.CategoryVo>>>({
            url: `/category/page`,
            method: 'GET',
            params: data
        })
    }
    return {
        addCategory,
        deleteCategory,
        deleteCategoryBatch,
        updateCategory,
        categoryList,
        categoryPage
    }
}