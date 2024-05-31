declare namespace FilesModel {
    interface FilesVo {
        id: number;
        fileName: string;
        fileType: string;
        fileSize: string;
        fileUuid: string;
        fileMd5: string;
        downloadUrl: string;
        userInfo?: UserModel.UserSimpleVo;
        createTime: string;
        updateTime: string;
    }

    interface FilesQueryDto extends ApiModel.PageRequest {
        fileName?: string;
        fileType?: string;
        fileUuid?: string;
    }
}