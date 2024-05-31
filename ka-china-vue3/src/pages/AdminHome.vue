<script setup lang="ts">
import router from "@/router";
import {useSystemStore} from "@/store/modules/useSystemStore";
import {useUserStore} from "@/store/modules/useUserStore.ts";
import {storeToRefs} from "pinia";
import AdminHeader from "@/components/header/AdminHeader.vue";
import AdminAside from "@/components/menu/AdminAside.vue";
import {watch} from "vue";

const {collapsed} = storeToRefs(useSystemStore())
const {user} = storeToRefs(useUserStore())

const toLogin = (val: UserModel.UserVo | undefined) => {
  if (!val) {
    router.push({name: 'auth'})
  }
}

watch(() => user.value, (val) => {
  toLogin(val)
}, {immediate: true})
</script>

<template>
  <div class="bg-white w-screen h-screen">
    <a-layout-header style="background-color: #FFFFFF; padding: 1.1rem; "
                     class="h-14 border-b-2 border-gray-300 flex items-center justify-between overflow-hidden">
      <AdminHeader/>
    </a-layout-header>
    <div class="flex w-full h-full" style="height: calc(100% - 64px);">
      <a-layout-sider v-model:collapsed="collapsed" :trigger="null" collapsible style="background-color: #FFFFFF"
                      class="h-full overflow-x-hidden overflow-y-auto border-r-2 border-gray-300">
        <AdminAside/>
        <div class="px-3 py-6 flex items-center justify-center">
          <a-button type="primary" shape="circle" v-if="collapsed"
                    @click="() => {router.push({name: 'front.wikipedia'})}">
            <template #icon>
              <SwapOutlined/>
            </template>
          </a-button>
          <a-button type="primary" v-else plain round class="w-full"
                    @click="() => {router.push({name: 'front.wikipedia'})}">返回前台
          </a-button>
        </div>
      </a-layout-sider>
      <a-layout-content class="p-2 overflow-auto">
        <RouterView/>
      </a-layout-content>
    </div>
  </div>
</template>

<style lang="scss" scoped>

</style>