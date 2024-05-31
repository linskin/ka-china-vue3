<script setup lang="ts">
import {ref} from "vue";

const searchValue = ref<string>('');

const onSearch = (value: string) => {
  console.log('use value', searchValue.value);
};

</script>

<template>
  <section>
    <a-input-search
        v-model:value="searchValue"
        placeholder="关键词"
        enter-button
        @search="onSearch"
    />
  </section>
</template>

<style scoped>

</style>