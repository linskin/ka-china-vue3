<script setup lang="ts">
import {useUserStore} from "@/store/modules/useUserStore.ts";
import {storeToRefs} from "pinia";
import {ref} from "vue";
import useCategory from "@/composables/useCategory.ts";
import {message} from "ant-design-vue";
import router from "@/router";
// 人工智能、大数据、区块链、云计算、软件开发
const {categoryList} = useCategory()
const {user} = storeToRefs(useUserStore())

const categories = ref<CategoryModel.CategorySimpleVo[]>([])

const loadCategoryList = async () => {
  const res = await categoryList()
  if (res?.code === 200) {
    categories.value = res?.data!
  } else {
    message.error(res.message)
  }
}
loadCategoryList()
</script>

<template>
  <div class="ml-5 hidden xl:block">
    <a-card style="width: 300px">
      <div class="flex items-center justify-between">
        <div class="font-bold">
          <span>欢迎~</span>
          <span v-if="!user" class="text-theme cursor-pointer">请登录</span>
          <span v-else class="text-theme">{{ user?.name }}</span>
        </div>
        <div class="flex">
          <a-button type="primary" size="small" shape="round" :disabled="!user" @click="router.push({name: 'front.post.editor'})">去创作</a-button>
          <a-button type="primary" size="small" shape="round" v-if="user?.role === 'super_admin' || user?.role === 'admin'" @click="router.push({name: 'admin.welcome'})" danger class="ml-1">后台管理</a-button>
        </div>
      </div>
      <div class="w-full h-14 flex items-center justify-between mt-8" v-if="user">
        <div class="flex flex-col items-center justify-center">
          <span class="text-theme">3</span>
          <span>帖子</span>
        </div>
        <span>|</span>
        <div class="flex flex-col items-center justify-center">
          <span class="text-theme">3</span>
          <span>关注</span>
        </div>
        <span>|</span>
        <div class="flex flex-col items-center justify-center">
          <span class="text-theme">3</span>
          <span>收藏</span>
        </div>
      </div>
    </a-card>
    <br>
    <a-card style="width: 300px" :body-style="{padding: '10px'}">
      <div v-for="(item, index) in categories" :key="item.id"
           class="h-10 w-full flex items-center justify-between text-sm cursor-pointer px-3 hover:bg-blue-300 hover:rounded hover:font-bold hover:text-white">
        <span>{{ item.name }}</span>
        <span class="w-5 bg-theme flex items-center justify-center rounded-full text-white">{{ index + 1 }}</span>
      </div>
    </a-card>
    <a-carousel arrows>
      <template #prevArrow>
        <div class="custom-slick-arrow" style="left: 10px; z-index: 1">
          <left-circle-outlined/>
        </div>
      </template>
      <template #nextArrow>
        <div class="custom-slick-arrow" style="right: 10px">
          <right-circle-outlined/>
        </div>
      </template>
      <div v-for="index in 4" :key="index"
           class="w-[300px] h-36 bg-theme !flex !items-center !justify-center text-white cursor-pointer">
        广告
      </div>
    </a-carousel>

  </div>
</template>

<style lang="scss" scoped>
.ant-carousel {
  width: 300px;
  height: 144px;
  margin: 20px 0;
}

.ant-carousel :deep(.slick-arrow.custom-slick-arrow) {
  width: 25px;
  height: 25px;
  font-size: 25px;
  color: #fff;
  background-color: rgba(31, 45, 61, 0.11);
  opacity: 0.3;
  z-index: 1;
}

.ant-carousel :deep(.custom-slick-arrow:before) {
  display: none;
}

.ant-carousel :deep(.custom-slick-arrow:hover) {
  opacity: 0.5;
}
</style>