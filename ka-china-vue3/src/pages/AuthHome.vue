<script setup lang="ts">
import Login from "@/pages/auth/Login.vue";
import {ref} from "vue";
import Register from "@/pages/auth/Register.vue";
import router from "@/router";
import {useUserStore} from "@/store/modules/useUserStore.ts";
import {storeToRefs} from "pinia";

const {user} = storeToRefs(useUserStore())
const load = () => {
  if (user.value) {
    window.history.state.back === null ? router.push({name: 'front.wikipedia'}) : router.back()
  }
}
load()

const activeKey = ref<string>('login')

const backPage = async () => {
  window.history.state.back === null ? await router.push({name: 'front.wikipedia'}) : router.back()
}

const handleRegisterSuccess = () => {
  activeKey.value = 'login'
}
</script>

<template>
  <div class="fixed flex items-center justify-start text-base cursor-pointer hover:text-theme top-5 left-5"
       @click="backPage">
    <ArrowLeftOutlined/>
    <span class="mx-3">返回</span>
  </div>
  <section class="w-screen h-screen bg-gradient-to-r from-cyan-200 to-blue-200 flex items-center justify-center">
    <img src="@/assets/images/login-img.svg" class="w-[800px] h-96 hidden lg:block" alt="">
    <a-tabs v-model:activeKey="activeKey">
      <a-tab-pane key="login" tab="登录">
        <Login/>
      </a-tab-pane>
      <a-tab-pane key="register" tab="注册" force-render>
        <Register @on-submit="handleRegisterSuccess"/>
      </a-tab-pane>
    </a-tabs>
    <div class="fixed bottom-5">
      <span>@</span>
      <span>2023</span>
      <span class="mx-5">KA中国</span>
      <span>区块链行业应用实验室</span>
    </div>
  </section>
</template>

<style lang="scss" scoped>
:deep(.ant-tabs-nav-wrap) {
  justify-content: center;
}
</style>