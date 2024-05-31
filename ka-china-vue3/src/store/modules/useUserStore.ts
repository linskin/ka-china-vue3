import {defineStore} from 'pinia';
import {ref} from 'vue';
import useUser from "@/composables/useUser";
import router from "@/router";
import {message} from "ant-design-vue";

const {getCurrentUser, userLogout} = useUser()
export const useUserStore = defineStore(
    'user',
    () => {
        const user = ref<UserModel.UserVo>();

        const setUser = (userVo: UserModel.UserVo | undefined): void => {
            user.value = userVo
        };

        const getCurrentUserInfo = async (): Promise<void> => {
            const res = await getCurrentUser();
            if (res?.code === 200) {
                user.value = res?.data
            } else {
                user.value = undefined
            }
        }

        const logout = async (): Promise<void> => {
            const res = await userLogout()
            if (res?.code === 200) {
                user.value = undefined
                await router.push({name: 'auth'})
            } else {
                message.error(res.message)
            }
        }
        return {user, setUser, getCurrentUserInfo, logout};
    },
    {
        persist: true,
    },
);
