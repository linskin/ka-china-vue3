<script setup lang="ts">
import {ref, watch} from "vue";
import {UploadOutlined} from '@ant-design/icons-vue';
import {message} from 'ant-design-vue';
import type {UploadChangeParam} from 'ant-design-vue';
import {serviceIp} from "@/utils/request"


const uploadUrl = serviceIp + '/files/upload'

interface IProps {
    value: string;
    accept: string;
}
const fileList = ref<string[]>([]);
const loading = ref<boolean>(false);
const fileUrl = ref<string>('')
const accept = ref<string>()

// @ts-ignore
const props = withDefaults(defineProps<IProps>(), {
    value: '',
})
const emit = defineEmits(['update:value'])

watch(() => props.value, (val) => {
    fileList.value = []
    fileUrl.value = ''
    if (val.length !== 0) {
        fileList.value.push(val)
        fileUrl.value = val
    }
}, {immediate: true})

watch(() => props.accept, (val) => {
    if (val.length !== 0) {
        accept.value = val
    }
}, {immediate: true})

const handleChange = (info: UploadChangeParam) => {
    if (info.file.status === 'uploading') {
        loading.value = true;
        return;
    }
    if (info.file.status === 'done') {
        emit('update:value', info.file.response?.data)
        loading.value = false;
    }
    if (info.file.status === 'error') {
        loading.value = false;
        message.error('图片上传失败');
    }
};
</script>

<template>
    <div class="flex flex-col">
        <a-upload
            v-model:file-list="fileList"
            name="file"
            :accept="accept"
            :action="uploadUrl"
            :with-credentials="true"
            :show-upload-list="false"
            :maxCount="1"
            @change="handleChange"
        >
            <a-button>
                <UploadOutlined/>
                <span v-if="fileUrl.length === 0">点击上传附件</span>
                <span class="text-red-500" v-else>点击更换附件</span>
            </a-button>
        </a-upload>
        <a-typography-text
            v-if="fileUrl.length > 0"
            :style="{ width: '100%' }"
            :ellipsis="{ tooltip: fileUrl }"
            :content="fileUrl"
        />
    </div>
</template>


<style lang="scss" scoped>

</style>