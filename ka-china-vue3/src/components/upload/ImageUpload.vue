<script setup lang="ts">
import {ref, watch} from "vue";
import {PlusOutlined, LoadingOutlined} from '@ant-design/icons-vue';
import {message} from 'ant-design-vue';
import type {UploadChangeParam} from 'ant-design-vue';
import {serviceIp} from "@/utils/request"

const uploadUrl = serviceIp + '/files/upload'

interface IProps {
    value: string
}

const imageUrl = ref<string>('');
const fileList = ref<string[]>([]);
const loading = ref<boolean>(false);

// @ts-ignore
const props = withDefaults(defineProps<IProps>(), {
    value: ''
})
const emit = defineEmits(['update:value'])

watch(() => props.value, (val: string) => {
    fileList.value = []
    imageUrl.value = ''
    if (val.length !== 0) {
        fileList.value.push(val)
        imageUrl.value = val
    }
}, {immediate: true})

const handleChange = (info: UploadChangeParam) => {
    if (info.file.status === 'uploading') {
        loading.value = true;
        return;
    }
    if (info.file.status === 'done') {
        imageUrl.value = info.file.response?.data
        emit('update:value', imageUrl.value)
        loading.value = false;
    }
    if (info.file.status === 'error') {
        loading.value = false;
        message.error('图片上传失败');
    }
};
</script>

<template>
    <div>
        <a-upload
                v-model:file-list="fileList"
                name="file"
                accept=".png, .jpg, .jpeg"
                list-type="picture-card"
                class="avatar-uploader"
                :show-upload-list="false"
                :withCredentials="true"
                :action="uploadUrl"
                @change="handleChange"
        >
            <img v-if="imageUrl" :src="imageUrl" alt="avatar"/>
            <div v-else>
                <loading-outlined v-if="loading"></loading-outlined>
                <plus-outlined v-else></plus-outlined>
                <div class="ant-upload-text">Upload</div>
            </div>
        </a-upload>
    </div>
</template>


<style lang="scss" scoped>
.avatar-uploader > .ant-upload {
  width: 128px;
  height: 128px;
}

.ant-upload-select-picture-card i {
  font-size: 32px;
  color: #999;
}

.ant-upload-select-picture-card .ant-upload-text {
  margin-top: 8px;
  color: #666;
}
</style>