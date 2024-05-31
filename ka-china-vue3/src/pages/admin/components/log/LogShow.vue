<script setup lang="ts">
import {computed, reactive, watch} from "vue";
import moment from "moment/moment";
import dayjs from "dayjs";

interface IProps {
  visible: boolean,
  data: LogsModel.LogVo
}

// @ts-ignore
const props = withDefaults(defineProps<IProps>(), {
  visible: false
})
const emit = defineEmits(['onClose'])
const fromData = reactive<LogsModel.LogVo>({
  costTime: "",
  id: 0,
  operationIp: "",
  operationParam: "",
  operationTime: "",
  operationUrl: "",
  requestId: "",
  requestMethod: "",
  result: "",
  title: "",
})
watch(() => props.data, (val) => {
  fromData.id = val.id || 0;
  fromData.costTime = val.costTime || '';
  fromData.operationIp = val.operationIp || '';
  fromData.operationParam = val.operationParam || '';
  fromData.operationTime = val.operationTime || '';
  fromData.operationUrl = val.operationUrl || '';
  fromData.requestId = val.requestId || '';
  fromData.requestMethod = val.requestMethod || '';
  fromData.result = val.result || '';
  fromData.title = val.title || '';
  fromData.userInfo = val?.userInfo || {id: 0, account: '', avatar: '', role: '', name: ''};
}, {immediate: true})

const dialogVisible = computed({
  get: () => props.visible,
  set: () => {
    emit('onClose')
  }
})
const handleCancel = () => {
  emit('onClose')
}
</script>

<template>
  <a-modal width="100%" v-model:visible="dialogVisible" title="操作日志详情"
           @cancel="handleCancel">
    <a-descriptions bordered>
      <a-descriptions-item label="请求ID">{{fromData?.requestId}}</a-descriptions-item>
      <a-descriptions-item label="模块标题">{{fromData?.title}}</a-descriptions-item>
      <a-descriptions-item label="请求方式">{{fromData?.requestMethod}}</a-descriptions-item>
      <a-descriptions-item label="请求URL" :span="2">{{fromData?.operationUrl}}</a-descriptions-item>
      <a-descriptions-item label="主机地址" :span="1">{{fromData?.operationIp}}</a-descriptions-item>
      <a-descriptions-item label="请求参数" :span="3">{{fromData?.operationParam}}</a-descriptions-item>
      <a-descriptions-item label="返回参数" :span="3">{{fromData?.result}}</a-descriptions-item>
      <a-descriptions-item label="操作时间" :span="2">
        {{moment(fromData?.operationTime as moment.Moment).format('YYYY-MM-DD HH:mm:ss')}}
      </a-descriptions-item>
      <a-descriptions-item label="消耗时间" :span="1">{{fromData?.costTime}}</a-descriptions-item>
      <a-descriptions-item label="创建用户" :span="3">
        <div>
          ID - {{ fromData?.userInfo?.id }}
          <br>
          姓名 - {{ fromData?.userInfo?.name }}
          <br>
          账号 - {{ fromData?.userInfo?.account }}
          <br>
          角色 - {{ fromData?.userInfo?.role }}
        </div>
      </a-descriptions-item>
    </a-descriptions>
    <template #footer>
      <section class="flex justify-end">
        <a-button type="primary" size="small" @click="() => {dialogVisible=false; handleCancel()}">关闭</a-button>
      </section>
    </template>
  </a-modal>
</template>

<style scoped lang="less">
</style>