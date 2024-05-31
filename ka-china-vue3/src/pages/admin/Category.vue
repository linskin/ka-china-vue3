<script setup lang="ts">
import {computed, reactive, ref} from "vue";
import useCategory from "@/composables/useCategory";
import {message, notification, TableProps} from "ant-design-vue";
import moment from 'moment'
import Crate from "@/pages/admin/components/category/CategoryCrate.vue";
import Update from "@/pages/admin/components/category/CategoryUpdate.vue";

const {categoryPage, deleteCategory, deleteCategoryBatch} = useCategory()

const loadParams = reactive<CategoryModel.CategoryQueryDto>({
  current: 1, pageSize: 10, name: undefined
})

const dataSource = ref<CategoryModel.CategoryVo[]>([])
const total = ref<number>(0)
const createVisible = ref<boolean>(false)
const updateVisible = ref<boolean>(false)
const currentData = ref<CategoryModel.CategoryVo>({
  id: 0,
  name: '',
  userInfo: {id: 0, name: '', role: '', avatar: '', account: ''},
  createTime: '',
  updateTime: ''
})

const loadPage = async (params: CategoryModel.CategoryQueryDto) => {
  const res = await categoryPage({...params});
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
  {title: '名称', dataIndex: 'name', align: 'center', ellipsis: true},
  {
    title: '创建用户', dataIndex: 'userInfo', align: 'center', ellipsis: true,
    customRender: (val: { value: UserModel.UserSimpleVo}) => {
      return val.value?.name
    }
  },
  {
    title: '创建时间', dataIndex: 'createTime', align: 'center', ellipsis: true,
    customRender: (val: { value: moment.Moment }) => {
      return val ? moment(val.value).format('YYYY-MM-DD HH:mm:ss') : ''
    }
  },
  {
    title: '更新时间', dataIndex: 'updateTime', align: 'center', ellipsis: true,
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
  if (loadParams.name === undefined) {
    notification.warning({
      message: 'Warning',
      description: '请输入查询条件后进行搜索'
    });
    return
  }
  loadPage(loadParams)
}

const handleReset = () => {
  if (loadParams.name !== undefined) {
    loadParams.name = undefined;
    loadPage(loadParams)
  }
}

const handleDelete = async (id: number) => {
  const res = await deleteCategory({id: id})
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

const handleUpdate = (value: CategoryModel.CategoryVo) => {
  currentData.value = value;
  updateVisible.value = true;
}

</script>

<template>
  <a-card style="width: 100%">
    <template #title>
      <span class="font-bold">分类管理</span>
    </template>
    <a-space>
      <a-space>
        <span>名称</span>
        <a-input v-model:value="loadParams.name" placeholder="请输入"/>
      </a-space>
    </a-space>
    <div class="flex justify-end mr-4">
      <a-button type="default" size="small" class="mx-1" @click="handleReset">重置</a-button>
      <a-button type="primary" size="small" class="mx-1" @click="handleSearch">搜索</a-button>
    </div>
  </a-card>
  <a-card style="width: 100%; margin-top: 10px">
    <template #extra>
      <a-button type="primary" size="small" class="mx-1" @click="() => {createVisible = true}">创建</a-button>
    </template>
    <a-table
        :columns="columns"
        :row-key="record => record?.id"
        :data-source="dataSource"
        :pagination="pagination"
        @change="handleTableChange"
    >
      <template #bodyCell="{ column, record }">
        <template v-if="column.dataIndex === 'action'">
          <section class="flex justify-center">
            <a-button type="text" class="!text-theme"
                      @click="handleUpdate(record)">更新
            </a-button>
            <a-divider type="vertical"/>
            <a-button type="text" @click="handleDelete(record?.id)" danger>删除</a-button>
          </section>
        </template>
      </template>
    </a-table>
    <Crate :visible="createVisible" @on-cancel="() => {createVisible = false}"
           @on-ok="() => {createVisible = false; loadPage(loadParams)}"/>
    <Update :visible="updateVisible" :data="currentData" @on-cancel="() => {updateVisible = false}"
            @on-ok="() => {updateVisible = false; loadPage(loadParams)}"/>
  </a-card>
</template>


<style lang="scss" scoped>
:deep(.ant-card-body) {
  padding: 10px;
}
</style>