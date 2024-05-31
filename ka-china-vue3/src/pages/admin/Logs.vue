<script setup lang="ts">
import {computed, reactive, ref} from "vue";
import {message, notification, TableProps} from "ant-design-vue";
import moment from "moment/moment";
import useLogs from "@/composables/useLogs";
import LogShow from "@/pages/admin/components/log/LogShow.vue";

const {logPage, deleteLog, deleteLogAll, deleteLogBatch} = useLogs()

const loadParams = reactive<LogsModel.LogQueryDto>({
  current: 1, pageSize: 10, title: undefined, requestMethod: undefined
})

const dataSource = ref<LogsModel.LogVo[]>([])
const total = ref<number>(0)
const showVisible = ref<boolean>(false)
const ids = ref<number[]>([])
const currentData = ref<LogsModel.LogVo>({
  id: 0,
  costTime: '',
  operationTime: '',
  operationIp: '',
  operationParam: '',
  operationUrl: '',
  requestId: '',
  requestMethod: '',
  result: '',
  title: '',
  userInfo: {id: 0, name: '', account: '', avatar: '', role: ''}
})

const loadPage = async (params: LogsModel.LogQueryDto) => {
  const res = await logPage({...params});
  if (res?.code === 200) {
    dataSource.value = res?.data?.records || []
    total.value = res?.data?.total || 0
  } else {
    res && message.error(res?.message)
  }
}
loadPage(loadParams)

const columns = [
  {title: 'ID', dataIndex: 'id', align: 'center', ellipsis: true},
  {title: '请求ID', dataIndex: 'requestId', align: 'center', ellipsis: true},
  {title: '模块标题', dataIndex: 'title', align: 'center', ellipsis: true,},
  {title: '请求方式', dataIndex: 'requestMethod', align: 'center', ellipsis: true},
  {title: '请求URL', dataIndex: 'operationUrl', align: 'center', ellipsis: true},
  {
    title: '创建用户', dataIndex: 'userInfo', align: 'center', ellipsis: true,
    customRender: (val: { value: UserModel.UserSimpleVo }) => {
      return val ? val.value.name : ''
    }
  },
  {title: '主机地址', dataIndex: 'operationIp', align: 'center', ellipsis: true},
  {title: '请求参数', dataIndex: 'operationParam', align: 'center', ellipsis: true},
  {title: '返回参数', dataIndex: 'result', align: 'center', ellipsis: true},
  {title: '消耗时间', dataIndex: 'costTime', align: 'center', ellipsis: true},
  {
    title: '操作时间', dataIndex: 'operationTime', align: 'center', ellipsis: true,
    customRender: (val: { value: moment.Moment }) => {
      return val ? moment(val.value).format('YYYY-MM-DD HH:mm:ss') : ''
    }
  },
  {title: '操作', dataIndex: 'action', align: 'center', width: '170px'},
];

const pagination = computed(() => ({
  total: total.value,
  current: loadParams.current,
  pageSize: loadParams.pageSize,
}));

const handleTableChange: TableProps['onChange'] = (e) => {
  loadParams.current = e.current
  loadParams.pageSize = e.pageSize
  loadPage(loadParams)
}

const handleSearch = () => {
  if (loadParams.title === undefined && loadParams.requestMethod === undefined) {
    notification.warning({
      message: 'Warning',
      description: '请输入查询条件后进行搜索'
    });
    return
  }
  loadPage(loadParams)
}

const handleReset = () => {
  if (loadParams.title !== undefined || loadParams.requestMethod !== undefined) {
    loadParams.title = undefined;
    loadParams.requestMethod = undefined;
    loadPage(loadParams)
  }
}

const handleRowSelection: TableProps['rowSelection'] = {
  onChange: (selectedRowKeys) => {
    ids.value = selectedRowKeys as number[]
  }
}

const handleDeleteBatch = async () => {
  if (ids.value.length === 0) {
    message.error('请选择后进行删除')
    return
  }
  const res = await deleteLogBatch({ids: ids.value})
  if (res?.code === 200) {
    notification.success({
      message: 'Success',
      description: '批量删除成功'
    });
    ids.value = []
    await loadPage(loadParams)
  } else {
    res && message.error(res?.message)
  }
}

const handleDelete = async (id: number) => {
  const res = await deleteLog({id: id})
  if (res?.code === 200) {
    notification.success({
      message: 'Success',
      description: '删除成功'
    });
    await loadPage(loadParams)
  } else {
    res && message.error(res?.message)
  }
}

const handleDeleteAll = async () => {
  const res = await deleteLogAll()
  if (res?.code === 200) {
    notification.success({
      message: 'Success',
      description: '已删除全部'
    });
    await loadPage(loadParams)
  } else {
    res && message.error(res?.message)
  }
}

const handleLogShow = (val: LogsModel.LogVo) => {
  currentData.value = val
  showVisible.value = true
}
</script>

<template>
  <a-card style="width: 100%">
    <template #title>
      <span class="font-bold">操作日志</span>
    </template>
    <a-space>
      <a-space>
        <span>业务标题</span>
        <a-input v-model:value="loadParams.title" placeholder="请输入"/>
      </a-space>
      <a-space>
        <span>请求方式</span>
        <a-input v-model:value="loadParams.requestMethod" placeholder="请输入"/>
      </a-space>
    </a-space>
    <section class="flex justify-end">
      <a-button type="default" size="small" class="mx-1" @click="handleReset">重置</a-button>
      <a-button type="primary" size="small" class="mx-1" @click="handleSearch">搜索</a-button>
    </section>
  </a-card>
  <a-card style="width: 100%; margin-top: 10px">
    <template #extra>
      <section class="flex">
        <a-button type="ghost" size="small" class="mx-1" @click="handleDeleteBatch" danger>批量删除</a-button>
        <a-button type="primary" size="small" class="mx-1" @click="handleDeleteAll" danger>全部删除</a-button>
      </section>
    </template>
    <a-table
        :row-selection="handleRowSelection"
        :columns="columns"
        :row-key="record => record.id"
        :data-source="dataSource"
        :pagination="pagination"
        @change="handleTableChange"
    >
      <template #bodyCell="{ column, record }">
        <template v-if="column.dataIndex === 'action'">
          <section class="flex">
            <a-button type="link" @click="handleLogShow(record)">查看</a-button>
            <a-divider type="vertical"/>
            <a-button type="text" @click="handleDelete(record?.id)" danger>删除</a-button>
          </section>
        </template>
      </template>
    </a-table>
    <LogShow :visible="showVisible" :data="currentData" @on-close="() => {showVisible = false}" />
  </a-card>
</template>


<style lang="scss" scoped>
:deep(.ant-card-body) {
  padding: 10px;
}
</style>