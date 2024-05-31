<script setup lang="ts">
import {reactive, ref} from "vue";
import useUser from "@/composables/useUser.ts";
import {FormInstance, message, notification} from "ant-design-vue";

const formRef = ref<FormInstance>();

const {userRegister} = useUser()
const emit = defineEmits(['onSubmit'])
const formState = reactive<UserModel.UserRegisterDto>({
  account: "", name: "", password: "", rePassword: ""
});

const onFinish = async (values: any) => {
  const res = await userRegister(formState)
  if (res?.code === 200) {
    notification.success({
      message: 'Success',
      description: '注册成功'
    });
    emit('onSubmit')
    formRef.value?.resetFields();
  } else {
    res && message.error(res.message || '注册失败')
  }
};

const onFinishFailed = (errorInfo: any) => {
  console.log('Failed:', errorInfo);
};
</script>

<template>
  <div class="w-[300px] h-96">
    <a-form
        class="w-full"
        :model="formState"
        ref="formRef"
        :wrapper-col="{ span: 24 }"
        autocomplete="off"
        @finish="onFinish"
        @finishFailed="onFinishFailed"
    >
      <a-form-item
          name="reg-name"
          :rules="[{ required: true, message: '请输入姓名' }]"
      >
        <a-input v-model:value="formState.name" size="large" placeholder="请输入姓名">
          <template #prefix>
            <UserOutlined/>
          </template>
        </a-input>
      </a-form-item>
      <a-form-item
          name="reg-account"
          :rules="[{ required: true, message: '请输入账号' }]"
      >
        <a-input v-model:value="formState.account" size="large" placeholder="请输入账号">
          <template #prefix>
            <UserOutlined/>
          </template>
        </a-input>
      </a-form-item>

      <a-form-item
          name="reg-password"
          :rules="[{ required: true, message: '请输入密码' }]"
      >
        <a-input-password v-model:value="formState.password" size="large" placeholder="请输入密码">
          <template #prefix>
            <LockOutlined/>
          </template>
        </a-input-password>
      </a-form-item>
      <a-form-item
          name="reg-rePassword"
          :rules="[{ required: true, message: '请输入密码' }]"
      >
        <a-input-password v-model:value="formState.rePassword" size="large" placeholder="请再次输入密码">
          <template #prefix>
            <LockOutlined/>
          </template>
        </a-input-password>
      </a-form-item>
      <a-form-item>
        <a-button type="primary" size="large" html-type="submit" block>立即注册</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>

<style scoped>

</style>