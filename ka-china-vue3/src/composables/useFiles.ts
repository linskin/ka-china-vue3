import request from '@/utils/request'

export default () => {

    const uploadFile = async (data: File) => {
        const formData =  new FormData()
        formData.append('file', data)
        return await request<string, ApiModel.IResponse<string>>({
            url: `/files/upload`,
            transformRequest: [function (data) {
                return data;
            }],
            method: 'POST',
            data: formData
        })
    }

    const filesPage = async (data: FilesModel.FilesQueryDto) => {
        return await request<ApiModel.ApiPage<FilesModel.FilesVo>, ApiModel.IResponse<ApiModel.ApiPage<FilesModel.FilesVo>>>({
            url: `/files/page`,
            method: 'GET',
            params: data
        })
    }
    return {
        uploadFile,
        filesPage
    }
}