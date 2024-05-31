import axios from "axios";
import {message as AMessage} from "ant-design-vue";
import {useSystemStore} from "@/store/modules/useSystemStore";
import {useUserStore} from "@/store/modules/useUserStore";

// const baseURL = 'http://localhost:8100/api'
const baseURL = 'http://127.0.0.1:8080/'
const service = axios.create({
    baseURL: baseURL,
    timeout: 20000
})
service.defaults.withCredentials = true

service.interceptors.request.use(
    (config: any) => {
        const {changeIsLoading} = useSystemStore()
        changeIsLoading(true)
        config.headers['Content-Type'] = 'application/json;charset=utf-8';
        return config
    },
    error => {
        return Promise.reject(error)
    });

service.interceptors.response.use(
    response => {
        const {changeIsLoading} = useSystemStore()
        const {setUser} = useUserStore()
        changeIsLoading(false)
        let res = response;
        if (res.data.code === 401) {
            setUser(undefined)
            return
        } else if (res.data.code === 403) {
            // ElMessage.error(res.data.message)
            // router.push('/error/403')
            AMessage.error(res?.data?.message);
            return
        }
        return res.data;
    },
    error => {
        const {changeIsLoading} = useSystemStore()
        changeIsLoading(false)
        console.log('err' + error)
        let {message} = error;
        if (message == "Network Error") {
            message = "后端接口连接异常";
        } else if (message.includes("timeout")) {
            message = "系统接口请求超时";
        } else if (message.includes("Request failed with status code")) {
            message = "系统接口" + message.substr(message.length - 3) + "异常";
        }
        AMessage.error(message);
        return Promise.reject(error)
    })

export default service
export const serviceIp = baseURL
