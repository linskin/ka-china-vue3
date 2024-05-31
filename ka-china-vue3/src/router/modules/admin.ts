import {RouteRecordRaw} from "vue-router";

export default {
    path: '/admin',
    component: () => import('@/pages/AdminHome.vue'),
    meta: {canAdmin: true, title: 'KA中国管理后台'},
    redirect: '',
    children: [
        {
            path: '',
            name: 'admin.welcome',
            component: () => import('@/pages/admin/Welcome.vue'),
            meta: {canAdmin: true, title: '欢迎'},
        },
        {
            path: 'logs',
            name: 'admin.logs',
            component: () => import('@/pages/admin/Logs.vue'),
            meta: {canAdmin: true, title: '操作日志管理'},
        },
        {
            path: 'banner',
            name: 'admin.banner',
            component: () => import('@/pages/admin/Banner.vue'),
            meta: {canAdmin: true, title: 'Banner管理'},
        },
        {
            path: 'user',
            name: 'admin.user',
            component: () => import('@/pages/admin/User.vue'),
            meta: {canAdmin: true, title: '用户管理'},
        },
        {
            path: 'files',
            name: 'admin.files',
            component: () => import('@/pages/admin/Files.vue'),
            meta: {canAdmin: true, title: '文件管理'},
        },
        {
            path: 'category',
            name: 'admin.category',
            component: () => import('@/pages/admin/Category.vue'),
            meta: {canAdmin: true, title: '分类管理'},
        },
        {
            path: 'post',
            name: 'admin.post',
            component: () => import('@/pages/admin/Post.vue'),
            meta: {canAdmin: true, title: '百科管理'},
        },
    ],
} as RouteRecordRaw