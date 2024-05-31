<script lang="ts" setup>
import {Viewer} from "@bytemd/vue-next";
import 'bytemd/dist/index.css'
import 'juejin-markdown-themes/dist/juejin.min.css'
import 'highlight.js/styles/vs.css'
import gfm from "@bytemd/plugin-gfm";
import frontMatter from "@bytemd/plugin-frontmatter";
import gemoji from "@bytemd/plugin-gemoji";
import highlight from "@bytemd/plugin-highlight";
import mediumZoom from "@bytemd/plugin-medium-zoom";
import mermaid from "@bytemd/plugin-mermaid";
import math from "@bytemd/plugin-math";
import gfmLocale from '@bytemd/plugin-gfm/locales/zh_Hans.json';
import mermaidLocale from '@bytemd/plugin-mermaid/locales/zh_Hans.json';

interface Props {
    value?: string,
}

const {value} = defineProps<Props>();

const plugins = [
    gfm({locale: gfmLocale}),
    frontMatter(),
    gemoji(),
    highlight(),
    mediumZoom(),
    mermaid({locale: mermaidLocale}),
    math(),
    // mathSsr()
    // Add more plugins here
]
</script>

<template>
    <div>
        <Viewer :plugins="plugins" :value="value"/>
    </div>
</template>

<style scoped>
:deep(.katex-html) {
  display: none;
}
</style>
