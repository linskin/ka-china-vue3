<script setup lang="ts">
import {useSystemStore} from "@/store/modules/useSystemStore";
import {useUserStore} from "@/store/modules/useUserStore.ts";
import {storeToRefs} from "pinia";

const {isLoading} = storeToRefs(useSystemStore())
const {getCurrentUserInfo} = useUserStore()
getCurrentUserInfo()
</script>

<template>
  <a-spin :spinning="isLoading" tip="Loading...">
    <RouterView/>
  </a-spin>
</template>

<style lang="less" scoped>

</style>
