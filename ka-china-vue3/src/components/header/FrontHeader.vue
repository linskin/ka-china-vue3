<script setup lang="ts">
import FrontMenu from "@/components/menu/FrontMenu.vue";
import FrontSearch from "@/components/search/FrontSearch.vue";
import router from "@/router";
import {useUserStore} from "@/store/modules/useUserStore";
import {storeToRefs} from "pinia";

const {user} = storeToRefs(useUserStore())
</script>

<template>
  <section class="w-full h-14 bg-white px-5 border-b-2 border-b-gray-500 fixed z-50">
    <div class="max-w-[1400px] h-full mx-auto flex items-center justify-between">
      <div class="flex items-center">
        <img src="@/assets/images/logo.png" class="w-14 h-14" alt="">
        <div class="font-bold text-2xl">KA中国</div>
        <FrontMenu/>
      </div>
      <div>
        <FrontSearch />
      </div>
      <div v-if="user">
        <a-avatar :size="44" :src="user?.avatar" />
      </div>
      <div v-else class="flex items-center">
        <a-button type="primary" @click="router.push({name: 'auth'})">
          <span>登录</span>
          <span class="mx-2 relative top-[-1px]">|</span>
          <span>注册</span>
        </a-button>
      </div>
    </div>
  </section>

  <section id="components-back-top-demo-custom">
    <a-back-top>
      <div class="ant-back-top-inner">
        <VerticalAlignTopOutlined />
      </div>
    </a-back-top>
  </section>
</template>

<style lang="scss" scoped>
#components-back-top-demo-custom .ant-back-top {
  @apply bottom-28;
}

#components-back-top-demo-custom .ant-back-top-inner {
  @apply h-12 w-12 text-3xl rounded-3xl bg-theme text-white flex items-center justify-center;
}
</style>