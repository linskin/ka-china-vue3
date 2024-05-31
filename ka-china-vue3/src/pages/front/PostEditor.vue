<script setup lang="ts">
import ByteMD from "@/plugins/bytemd/ByteMDEditor.vue";
import ImageUpload from "@/components/upload/ImageUpload.vue";
import {FormInstance, message, notification} from "ant-design-vue";
import {Rule} from "ant-design-vue/es/form";
import {reactive, ref} from "vue";
import usePost from "@/composables/usePost";
import useCategory from "@/composables/useCategory";
import router from "@/router";

const formRef = ref<FormInstance>();
const formState = reactive<PostModel.PostAddDto>({categoryIds: [], content: "", cover: "", description: "", title: ""});
const options = ref<CategoryModel.CategorySimpleVo[]>([])
const {addPost} = usePost()
const {categoryList} = useCategory()
const loadOptions = async () => {
  const res = await categoryList()
  if (res?.code === 200) {
    options.value = res.data
  } else {
    res && message.error(res?.message)
  }
}
loadOptions()

// @ts-ignore
const rules: Record<string, Rule[]> = reactive({
  title: [{required: true, message: '请输入标题', trigger: 'change'}],
  categoryIds: [{required: true, message: '请选择分类', trigger: 'change'}],
  description: [{required: true, message: '请输入描述', trigger: 'change'}],
  content: [{required: true, message: '请输入内容', trigger: 'change'}],
});

const layout = {
  labelCol: {span: 4},
  wrapperCol: {span: 18},
};
const handleOk = () => {
  formRef.value?.validateFields().then(async () => {
    const res = await addPost(formState as PostModel.PostAddDto)
    if (res.code === 200) {
      await router.replace({name: 'front.wikipedia'})
      formRef.value?.resetFields();
      notification.success({
        message: 'Success',
        description: '创建成功'
      });
    } else {
      message.error(res.message || '创建失败')
    }
  }).catch(error => {
    console.error('error', error)
    message.error('表单输入不完整')
  })
}
const handleResetForm = () => {
  formRef.value?.resetFields();
};
</script>

<template>
    <section class="bg-white p-6 mx-5">
      <a-form ref="formRef" :model="formState" :rules="rules" v-bind="layout">
        <a-form-item has-feedback label="标题" name="title">
          <a-input v-model:value="formState.title" type="text" autocomplete="off"/>
        </a-form-item>
        <a-form-item has-feedback label="分类" name="categoryIds">
          <a-select
              v-model:value="formState.categoryIds"
              mode="multiple"
              style="width: 100%"
              placeholder="请选择类别"
              :field-names="{ label: 'name', value: 'id' }"
              :options="options"
          />
        </a-form-item>
        <a-form-item has-feedback label="封面" name="cover">
          <ImageUpload v-model:value="formState.cover as string"/>
        </a-form-item>
        <a-form-item has-feedback label="描述" name="description">
          <a-textarea v-model:value="formState.description" auto-size type="text" autocomplete="off"/>
        </a-form-item>
        <a-form-item has-feedback label="内容" name="content">
          <ByteMD v-model:value="formState.content"/>
        </a-form-item>
        <div class="flex justify-center">
          <a-button class="mx-3" type="primary" @click="handleOk">提交</a-button>
          <a-button class="mx-3" type="ghost" @click="handleResetForm">重置</a-button>
        </div>
      </a-form>
    </section>
</template>

<style scoped lang="scss">

</style>