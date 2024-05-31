import request from '@/utils/request'

export default () => {
    const deleteLog = async (data: ApiModel.IdRequest) => {
        return await request<boolean, ApiModel.IResponse<boolean>>({
            url: `/log/delete`,
            method: 'POST',
            data: data
        })
    }

    const deleteLogAll = async () => {
        return await request<boolean, ApiModel.IResponse<boolean>>({
            url: `/log/delete/all`,
            method: 'DELETE',
        })
    }

    const deleteLogBatch = async (data: ApiModel.IdsRequest) => {
        return await request<boolean, ApiModel.IResponse<boolean>>({
            url: `/log/delete/batch`,
            method: 'POST',
            data: data
        })
    }

    const logTail = async () => {
        return await request<LogsModel.LogVo[], ApiModel.IResponse<LogsModel.LogVo[]>>({
            url: `/log/tail`,
            method: 'GET',
        })
    }
    const logPage = async (data: LogsModel.LogQueryDto) => {
        return await request<ApiModel.ApiPage<LogsModel.LogVo>, ApiModel.IResponse<ApiModel.ApiPage<LogsModel.LogVo>>>({
            url: `/log/page`,
            method: 'GET',
            params: data
        })
    }
    return { deleteLog, deleteLogAll, deleteLogBatch, logTail, logPage }
}