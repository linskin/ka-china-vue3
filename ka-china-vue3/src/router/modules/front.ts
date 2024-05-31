import {RouteRecordRaw} from "vue-router";

export default {
    path: '/',
    name: 'front',
    component: () => import('@/pages/FrontHome.vue'),
    meta: {canAdmin: false, title: 'KA中国'},
    children: [
        {
            path: '',
            name: 'front.wikipedia',
            component: () => import('@/pages/front/Wikipedia.vue'),
            meta: {canAdmin: false, title: '百科'},
        },
        {
            path: 'details/:id',
            name: 'front.wikipedia.detail',
            component: () => import('@/pages/front/WikipediaDetail.vue'),
            meta: {canAdmin: false, title: '百科详情'},
        },
        {
            path: 'post/editor',
            name: 'front.post.editor',
            component: () => import('@/pages/front/PostEditor.vue'),
            meta: {canAdmin: false, title: '添加百科'},
        },
    ],
} as RouteRecordRaw