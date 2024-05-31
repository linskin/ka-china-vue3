<script setup lang="ts">

</script>

<template>
  <a-card :body-style="{padding: '10px', display: 'flex'}">
    <div class="flex-1 px-3 border-r-[0.1rem] border-gray-300">
      <div class="flex items-center justify-between">
        <span class="font-bold">推荐列表</span>
        <span class="text-gray-400 text-xs cursor-pointer hover:text-theme">查看更多</span>
      </div>
      <div>
        <div class="text-xs my-1 px-2 flex items-center" v-for="index in 4" :key="index">
          <div class="w-2 h-2 bg-cyan-500 mr-2"></div>
          <div class="flex-1">KA中国</div>
          <div class="text-theme">666</div>
        </div>
      </div>
    </div>
    <div class="flex-1 px-3 border-l-[0.1rem] border-gray-300">
      <div class="flex items-center justify-between">
        <span class="font-bold">收藏列表</span>
        <span class="text-gray-400 text-xs cursor-pointer hover:text-theme">查看更多</span>
      </div>
      <div>
        <div class="text-xs my-1 px-2 flex items-center" v-for="index in 4" :key="index">
          <div class="w-2 h-2 bg-theme rounded-full mr-2"></div>
          <div class="flex-1">一个专注计算机的百科网站</div>
          <div class="text-theme">111</div>
        </div>
      </div>
    </div>
  </a-card>
</template>

<style scoped>

</style>