<script setup lang="ts">
import {computed, reactive, ref, watch} from "vue";
import type {Rule} from 'ant-design-vue/es/form';
import useBanner from "@/composables/useBanner";
import {FormInstance, message, notification} from "ant-design-vue";
import ImageUpload from "@/components/upload/ImageUpload.vue";

const {updateBanner} = useBanner()
const formRef = ref<FormInstance>();
const formState = reactive<BannerModel.BannerUpdateDto>({cover: "", id: 0, name: ""});

interface IProps {
    visible: boolean;
    data: BannerModel.BannerVo;
}

// @ts-ignore
const props = withDefaults(defineProps<IProps>(), {
    visible: false,
})

const emit = defineEmits(['onCancel', 'onOk'])
watch(() => props.data, (val) => {
    formState.id = val.id || 0;
    formState.name = val.name || '';
    formState.cover = val.cover || '';
}, {immediate: true})
const dialogVisible = computed({
    get: () => props.visible,
    set: () => {
        emit('onCancel')
    }
})

// @ts-ignore
const rules: Record<string, Rule[]> = reactive({
  name: [{required: true, message: '请输入名称', trigger: 'change'}],
  cover: [{required: true, message: '请选择封面', trigger: 'change'}],
});

const layout = {
    labelCol: {span: 4},
    wrapperCol: {span: 18},
};

const handleCancel = () => {
    emit('onCancel')
}
const handleOk = () => {
    formRef.value?.validateFields().then(async () => {
        const res = await updateBanner(formState as BannerModel.BannerUpdateDto)
        if (res.code === 200) {
            formRef.value?.resetFields();
            notification.success({
                message: 'Success',
                description: '更新成功'
            });
            emit('onOk')
        } else {
            message.error(res.message || '更新失败')
        }
    }).catch(error => {
        console.error('error', error)
        message.error('表单输入不完整')
    })
}
</script>

<template>
    <a-modal v-model:visible="dialogVisible" title="banner更新" @cancel="handleCancel">
        <a-form ref="formRef" :model="formState" :rules="rules" v-bind="layout">
          <a-form-item has-feedback label="名称" name="name">
            <a-input v-model:value="formState.name" type="text" autocomplete="off"/>
          </a-form-item>
          <a-form-item has-feedback label="封面" name="cover">
            <ImageUpload v-model:value="formState.cover as string"/>
          </a-form-item>
        </a-form>
        <template #footer>
            <section class="flex justify-end">
                <a-button size="small" @click="() => {dialogVisible=false; handleCancel()}">取消</a-button>
                <a-button type="primary" size="small" @click="handleOk">更新</a-button>
            </section>
        </template>
    </a-modal>
</template>


<style lang="scss" scoped>

</style>