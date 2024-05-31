<script lang="ts" setup>
import gfm from '@bytemd/plugin-gfm'
import frontMatter from '@bytemd/plugin-frontmatter';
import gemoji from '@bytemd/plugin-gemoji';
import highlight from '@bytemd/plugin-highlight';
import mediumZoom from '@bytemd/plugin-medium-zoom';
import mermaid from "@bytemd/plugin-mermaid";
import math from "@bytemd/plugin-math";
import {Editor} from "@bytemd/vue-next";
import zhHans from 'bytemd/locales/zh_Hans.json'
import gfmLocale from '@bytemd/plugin-gfm/locales/zh_Hans.json';
import mermaidLocale from '@bytemd/plugin-mermaid/locales/zh_Hans.json';
import mathLocale from '@bytemd/plugin-math/locales/zh_Hans.json'
import 'bytemd/dist/index.css'
import 'juejin-markdown-themes/dist/juejin.min.css'
import 'highlight.js/styles/vs.css'
import {ref, watch} from "vue";
import {handleImageUpload} from "bytemd/dist/editor";

import useFiles from "@/composables/useFiles";

const {uploadFile} = useFiles()

const plugins = [
    gfm({locale: gfmLocale}),
    frontMatter(),
    gemoji(),
    highlight(),
    mediumZoom(),
    mermaid({locale: mermaidLocale}),
    math({locale: mathLocale})
    // Add more plugins here
]

const value = ref<string>()
interface Props {
    value?: string,
}

// @ts-ignore
const props = withDefaults(defineProps<Props>(), {
    value: '',
});
const emit = defineEmits(['update:value'])

watch(() => props.value, (val) => {
    value.value = val || '';
}, {immediate: true})

const handleChange = (v: string): void => {
    value.value = v
    emit('update:value', value.value)
    value.value = ''
}

const handleImageUpload = async (files: File[]) => {
    const res = await uploadFile(files[0])
    if (res?.code === 200) {
        return [
            {
                title: files.map((item) => item.name),
                url: res?.data
            }
        ]
    } else {
        return
    }
}
</script>

<template>
    <div>
        <Editor :value="value" :plugins="plugins" :uploadImages="handleImageUpload" :locale="zhHans" @change="handleChange"/>
    </div>
</template>

<style lang="less" scoped>
:deep(.bytemd) {
    height: calc(100vh - 516px);
}
:deep(.bytemd-tippy-right:last-child) {
    display: none;
}

:deep(.katex-html) {
  display: none;
}
</style>
