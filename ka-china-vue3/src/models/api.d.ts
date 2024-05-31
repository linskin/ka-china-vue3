declare namespace ApiModel {
    interface IdRequest {
        id: number;
    }
    interface IdsRequest {
        ids: number[];
    }

    interface PageRequest {
        current?: number;
        pageSize?: number;
    }

    interface ApiPage<T> {
        current: number;
        size: number;
        total: number;
        records: T[];
    }

    interface IResponse<T> {
        code: number;
        data: T;
        message: string;
    }
}