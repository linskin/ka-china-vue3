<script setup lang="ts">
import {computed, reactive, ref} from "vue";
import type {Rule} from 'ant-design-vue/es/form';
import useUser from "@/composables/useUser";
import {FormInstance, message, notification} from "ant-design-vue";

const {addUser} = useUser()
const formRef = ref<FormInstance>();
const formState = reactive<UserModel.UserAddDto>({account: "", name: "", role: ""});

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

let validateUserName = async (_rule: Rule, value: string) => {
    if (!value) {
        return Promise.reject('昵称不能为空');
    }
    if (value.length < 2 || value.length > 30) {
        return Promise.reject('昵称长度需在2-30之间');
    } else {
        return Promise.resolve();
    }
};

let validateUserAccount = async (_rule: Rule, value: string) => {
    if (!value) {
        return Promise.reject('手机号不能为空');
    }
    const checkRule = /^(?:(?:\+|00)86)?1\d{10}$/
    if (!checkRule.test(value)) {
        return Promise.reject('您输入的手机号不合法');
    } else {
        return Promise.resolve();
    }
};

// @ts-ignore
const rules: Record<string, Rule[]> = reactive({
    userName: [{required: true, validator: validateUserName, trigger: 'change'}],
    userAccount: [{required: true, validator: validateUserAccount, trigger: 'change'}],
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
        const res = await addUser(formState as UserModel.UserAddDto)
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
    }).catch(error => {
        console.error('error',error)
        message.error('表单输入不完整')
    })
}
const handleResetForm = () => {
    formRef.value?.resetFields();
};
</script>

<template>
    <a-modal v-model:visible="dialogVisible" title="用户创建" @cancel="handleCancel">
        <a-form ref="formRef" :model="formState" :rules="rules" v-bind="layout">
            <a-form-item has-feedback label="姓名" name="userName">
                <a-input v-model:value="formState.name" type="text" autocomplete="off"/>
            </a-form-item>
            <a-form-item has-feedback label="手机号" name="userAccount">
                <a-input v-model:value="formState.account" type="text" autocomplete="off"/>
            </a-form-item>
            <a-form-item has-feedback label="角色" name="role">
                <a-input-password v-model:value="formState.role"/>
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