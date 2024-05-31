import { createRouter, createWebHistory } from 'vue-router'
import routes from '@/router/modules/index'
import guard from './guard'

const router = createRouter({
    history: createWebHistory(),
    routes,
})

guard(router)

export default router