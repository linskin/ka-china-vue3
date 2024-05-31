<script setup lang="ts">
import {reactive} from "vue";
import router from "@/router";
import useUser from "@/composables/useUser";
import {message, notification} from "ant-design-vue";
import {useUserStore} from "@/store/modules/useUserStore.ts";

const {userLogin} = useUser()
const {setUser} = useUserStore()

const formState = reactive<UserModel.UserLoginDto>({
  account: "", password: ""
});

const onFinish = async (values: any) => {
  const res = await userLogin(formState)
  if (res?.code === 200) {
    setUser(res.data!)
    window.history.state.back === null ? await router.push({name: 'front.wikipedia'}) : router.back()
    notification.success({
      message: 'Success',
      description: '登录成功'
    });
  } else {
    res && message.error(res.message || '登录失败')
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
        :wrapper-col="{ span: 24 }"
        autocomplete="off"
        @finish="onFinish"
        @finishFailed="onFinishFailed"
    >
      <a-form-item
          name="account"
          :rules="[{ required: true, message: '请输入账号' }]"
      >
        <a-input v-model:value="formState.account" size="large" placeholder="请输入账号">
          <template #prefix>
            <UserOutlined/>
          </template>
        </a-input>
      </a-form-item>

      <a-form-item
          name="password"
          :rules="[{ required: true, message: '请输入密码' }]"
      >
        <a-input-password v-model:value="formState.password" size="large" placeholder="请输入密码">
          <template #prefix>
            <LockOutlined/>
          </template>
        </a-input-password>
      </a-form-item>
      <div class="flex justify-end">
        <a-button type="link">忘记密码？</a-button>
      </div>
      <a-form-item>
        <a-button type="primary" html-type="submit" size="large" block>登录</a-button>
      </a-form-item>
      <a-form-item>
        <a-divider><span class="text-gray-400">其他登录方式</span></a-divider>
        <div class="flex text-2xl items-center justify-center">
          <div class="text-[#50c8fd] mx-3 cursor-pointer">
            <QqOutlined/>
          </div>
          <div class="text-[#07c160] mx-3 cursor-pointer">
            <WechatOutlined/>
          </div>
          <div class="mx-3 cursor-pointer">
            <GithubOutlined/>
          </div>
        </div>
      </a-form-item>
    </a-form>
  </div>
</template>

<style scoped>

</style>