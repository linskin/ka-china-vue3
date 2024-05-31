import {defineStore} from 'pinia';
import {ref} from 'vue';

export const useSystemStore = defineStore(
    'system',
    () => {
        const collapsed = ref<boolean>(false);
        const isLoading = ref<boolean>(false);

        const changeCollapsed = (): void => {
            collapsed.value = !collapsed.value;
        };

        const changeIsLoading = (val: boolean): void => {
            isLoading.value = val;
        };
        return {collapsed, changeCollapsed, isLoading, changeIsLoading};
    },
    {
        persist: true,
    },
);
