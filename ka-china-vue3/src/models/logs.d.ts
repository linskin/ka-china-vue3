declare namespace LogsModel {
    interface LogVo {
        id?: number;
        requestId?: string;
        title?: string;
        requestMethod?: string;
        operationUrl?: string;
        userInfo?: UserModel.UserSimpleVo;
        operationIp?: string;
        operationParam?: string;
        result?: string;
        operationTime?: string;
        costTime?: string;
    }


    interface LogQueryDto extends ApiModel.PageRequest {
        title?: string;
        requestMethod?: string;
    }
}