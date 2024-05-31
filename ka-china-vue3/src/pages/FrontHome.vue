<script setup lang="ts">
import FrontHeader from "@/components/header/FrontHeader.vue";
import FrontFooter from "@/components/footer/FrontFooter.vue";
</script>

<template>
  <header>
    <FrontHeader />
  </header>
  <main class="ka-front-main">
    <RouterView />
  </main>
  <footer>
    <FrontFooter />
  </footer>
</template>

<style lang="scss" scoped>
.ka-front-main {
  min-height: calc(100vh - 96px);
  padding-top: 70px;
  padding-bottom: 15px;
}
</style>