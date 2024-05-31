<script setup lang="ts">
import router from "@/router";
import {useUserStore} from "@/store/modules/useUserStore";
import {useSystemStore} from "@/store/modules/useSystemStore";
import {storeToRefs} from "pinia";

const {user} = storeToRefs(useUserStore())
const {logout} = useUserStore()
const {collapsed} = storeToRefs(useSystemStore())
const {changeCollapsed} = useSystemStore()
</script>

<template>
  <div class="header-left">
    <img class="logo" src="@/assets/images/logo.png" alt="" draggable="false">
    <div class="title" @click="() => { router.push({name: 'admin.welcome'}) }">KA中国管理后台</div>
    <div class="menu-switch">
      <MenuUnfoldOutlined @click="changeCollapsed" v-if="collapsed"/>
      <MenuFoldOutlined @click="changeCollapsed" v-else/>
    </div>
  </div>
  <a-dropdown v-if="user">
    <a-space class="flex items-center cursor-pointer">
      <a-avatar :size="36" :src="user?.avatar"/>
      <span>{{ user?.name }}</span>
      <setting-outlined class="!flex !items-center"/>
    </a-space>
    <template #overlay>
      <a-menu class="!mt-3">
        <a-menu-item>
          <span @click="router.push({name: 'admin.center'})">个人中心</span>
        </a-menu-item>
        <a-menu-item>
          <span @click="router.push({name: 'admin.password'})">修改密码</span>
        </a-menu-item>
        <div class="h-[0.01rem] bg-gray-300 my-1"></div>
        <a-menu-item>
          <span @click="logout">退出登录</span>
        </a-menu-item>
      </a-menu>
    </template>
  </a-dropdown>
</template>

<style lang="scss" scoped>
.header-left {
  @apply flex items-center;
  .logo {
    @apply h-12;
  }

  .title {
    @apply font-bold text-xl m-3 select-none cursor-pointer;
  }

  .menu-switch {
    @apply flex items-center text-xl cursor-pointer hover:text-blue-500 relative top-0.5;
  }
}
</style>