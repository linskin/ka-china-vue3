import {NavigationGuardNext, RouteLocationNormalized, Router} from 'vue-router'
import NProgress from 'nprogress';
import '@/assets/styles/nprogress.scss';
import {useUserStore} from "@/store/modules/useUserStore";
import {storeToRefs} from "pinia";

function beforeEach(to: RouteLocationNormalized, _: RouteLocationNormalized, next: NavigationGuardNext) {
    const {user} = storeToRefs(useUserStore())
    NProgress.start();
    if (to.meta.title) {
        document.title = to.meta.title as string + ' | ' + 'KA中国'
    }
    if ((to.meta.canAdmin && !user) || to.meta.canAdmin && !(user.value?.role === 'admin' || user.value?.role === 'super_admin')) {
        next('/auth')
    } else {
        next()
    }
}

function afterEach() {
    NProgress.done();
}

export default (router: Router) => {
    router.beforeEach(beforeEach);
    router.afterEach(afterEach);
};