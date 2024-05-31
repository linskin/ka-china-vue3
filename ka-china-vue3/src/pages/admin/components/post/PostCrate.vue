<script setup lang="ts">
import {computed, reactive, ref} from "vue";
import type {Rule} from 'ant-design-vue/es/form';
import usePost from "@/composables/usePost";
import useCategory from "@/composables/useCategory";
import {FormInstance, message, notification} from "ant-design-vue";
import ImageUpload from "@/components/upload/ImageUpload.vue";
import ByteMD from '@/plugins/bytemd/ByteMDEditor.vue'

const {addPost} = usePost()
const {categoryList} = useCategory()

const formRef = ref<FormInstance>();
const formState = reactive<PostModel.PostAddDto>({categoryIds: [], content: "", cover: "", description: "", title: ""});
const options = ref<CategoryModel.CategorySimpleVo[]>([])

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

const handleCancel = () => {
  emit('onCancel')
}
const handleOk = () => {
  formRef.value?.validateFields().then(async () => {
    const res = await addPost(formState as PostModel.PostAddDto)
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
    console.error('error', error)
    message.error('表单输入不完整')
  })
}
const handleResetForm = () => {
  formRef.value?.resetFields();
};
</script>

<template>
  <a-modal v-model:visible="dialogVisible" width="100%" wrap-class-name="full-modal" title="帖子创建" @cancel="handleCancel">
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
    </a-form>
    <template #footer>
      <section class="flex justify-end">
        <a-button size="small" @click="handleResetForm">重置</a-button>
        <a-button type="primary" size="small" @click="handleOk">创建</a-button>
      </section>
    </template>
  </a-modal>
</template>


<style lang="less">
.full-modal {
  .ant-modal {
    max-width: 100%;
    top: 0;
    padding-bottom: 0;
    margin: 0;
  }
  .ant-modal-content {
    display: flex;
    flex-direction: column;
    height: calc(100vh);
    overflow: auto;
  }
  .ant-modal-body {
    flex: 1;
    overflow: auto;
  }
}
</style>