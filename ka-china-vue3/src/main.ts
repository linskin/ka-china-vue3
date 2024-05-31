import { createApp } from 'vue'
import '@/assets/styles/app.scss'
import App from '@/App.vue'
import {setupStore} from "@/store";
import Antd from 'ant-design-vue';
import * as AntdIconsVue from "@ant-design/icons-vue";
import 'ant-design-vue/dist/antd.less';
import '@/assets/styles/ant-rewrite.less'
import router from "@/router";

const app = createApp(App);
setupStore(app)
app.use(Antd)
app.use(router)
app.mount('#app')


for (const [key, component] of Object.entries(AntdIconsVue)) {
    app.component(key, component)
}

