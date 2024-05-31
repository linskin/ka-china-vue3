<script setup lang="ts">
import {ref} from "vue";
import {useRoute} from "vue-router";
import router from "@/router";

const route = useRoute()

const currentRouteName = ref<string>(route.name as string)

const menuList = ref<SystemModel.IMenu[]>([
  {id: 1, name: '百科', key: 'front.wikipedia', icon: ''},
  {id: 2, name: '课程', key: '', icon: ''},
  {id: 3, name: '活动', key: '', icon: ''},
  {id: 4, name: '资讯', key: '', icon: ''},

])

console.log(currentRouteName.value)
</script>

<template>
  <nav class="flex mx-10">
    <div v-for="item in menuList" :key="item.id"
         @click="router.push({name: item.key})"
         :class=" currentRouteName === item.key ? 'mx-3 text-lg font-bold text-theme' : 'mx-3 text-lg cursor-pointer hover:text-theme hover:font-bold'">
      {{ item.name }}
    </div>
  </nav>
</template>

<style scoped>

</style>