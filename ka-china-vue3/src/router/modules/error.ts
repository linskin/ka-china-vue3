import {RouteRecordRaw} from "vue-router";

export default {
    path: '/error',
    name: 'error',
    redirect: '/error/404',
    meta: {canAdmin: false, title: 'error'},
    children: [
        {
            path: '404',
            name: 'error.404',
            component: () => import('@/pages/error/404.vue'),
            meta: {canAdmin: false, title: '未找到资源'},
        },
        {
            path: '/:any(.*)',
            name: 'error.404',
            component: () => import('@/pages/error/404.vue'),
        },
    ],
} as RouteRecordRaw