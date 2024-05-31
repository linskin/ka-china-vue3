<script setup lang="ts">
import {computed, reactive, ref} from "vue";
import type {Rule} from 'ant-design-vue/es/form';
import useCategory from "@/composables/useCategory";
import {FormInstance, message, notification} from "ant-design-vue";

const {addCategory} = useCategory()
const formRef = ref<FormInstance>();
const formState = reactive<CategoryModel.CategoryAddDto>({name: ""});

interface IProps {
    visible: boolean
}

const props = withDefaults(defineProps<IProps>(), {
    visible: false,
})

const emit = defineEmits(['onCancel', 'onOk'])

const dialogVisible = computed({
    get: () => props.visible,
    set: () => {
        emit('onCancel')
    }
})

// @ts-ignore
const rules: Record<string, Rule[]> = reactive({
    name: [{required: true, message: '请输入名称', trigger: 'change'}],
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
        const res = await addCategory(formState as CategoryModel.CategoryAddDto)
        if (res.code === 200) {
            formRef.value?.resetFields();
            notification.success({
                message: 'Success',
                description: '创建成功'
            });
            emit('onOk')
        } else {
            message.error(res.message || '创建失败')
        }
    }).catch(_ => {
        message.error('表单输入不完整')
    })
}
const handleResetForm = () => {
    formRef.value?.resetFields();
};
</script>

<template>
    <a-modal v-model:visible="dialogVisible" title="分类创建" @cancel="handleCancel">
        <a-form ref="formRef" :model="formState" :rules="rules" v-bind="layout">
            <a-form-item has-feedback label="名称" name="name">
                <a-input v-model:value="formState.name" type="text" autocomplete="off"/>
            </a-form-item>
        </a-form>
        <template #footer>
            <section class="flex justify-end">
                <a-button size="small" @click="handleResetForm">重置</a-button>
                <a-button type="primary" size="small" @click="handleOk">创建</a-button>
            </section>
        </template>
    </a-modal>
</template>


<style lang="scss" scoped>

</style>