import {RouteRecordRaw} from "vue-router";

export default {
    path: '/auth',
    name: 'auth',
    component: () => import('@/pages/AuthHome.vue'),
    meta: {canAdmin: false, title: '登录'},
} as RouteRecordRaw