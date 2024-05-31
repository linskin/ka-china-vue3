<script setup lang="ts">
import {
  HomeOutlined,
  UsergroupAddOutlined,
  FileOutlined,
  BranchesOutlined,
  FileImageOutlined,
  VideoCameraOutlined,
  BookOutlined,
  TableOutlined,
  VideoCameraAddOutlined,
  FormOutlined
} from "@ant-design/icons-vue";
import {MenuProps} from "ant-design-vue";
import {ref, watch} from "vue";
import router from "@/router";
import {useRoute} from "vue-router";

const route = useRoute()

const selectedKeys = ref<string[]>([route.name as string]);
const handleClick: MenuProps['onClick'] = e => {
  router.push({name: e.key as string})
};

watch(() => route.name, (val) => {
  selectedKeys.value = [route.name as string]
}, {immediate: true})
</script>

<template>
  <a-menu
      v-model:selectedKeys="selectedKeys"
      mode="inline"
      @click="handleClick"
  >
    <a-menu-item key="admin.welcome">
      <template #icon>
        <HomeOutlined/>
      </template>
      <span>主页</span>
    </a-menu-item>
    <a-menu-item key="admin.logs">
      <template #icon>
        <BranchesOutlined/>
      </template>
      <span>操作日志</span>
    </a-menu-item>
    <a-menu-item key="admin.banner">
      <template #icon>
        <UsergroupAddOutlined/>
      </template>
      <span>Banner管理</span>
    </a-menu-item>
    <a-menu-item key="admin.user">
      <template #icon>
        <UsergroupAddOutlined/>
      </template>
      <span>用户管理</span>
    </a-menu-item>
    <a-menu-item key="admin.files">
      <template #icon>
        <FileOutlined/>
      </template>
      <span>文件管理</span>
    </a-menu-item>
    <a-menu-item key="admin.category">
      <template #icon>
        <FileImageOutlined/>
      </template>
      <span>分类管理</span>
    </a-menu-item>
    <a-menu-item key="admin.post">
      <template #icon>
        <VideoCameraOutlined/>
      </template>
      <span>百科管理</span>
    </a-menu-item>
  </a-menu>
</template>


<style lang="less" scoped>
:deep(.ant-menu-title-content) {
  display: flex;
  align-items: center;
}
</style>