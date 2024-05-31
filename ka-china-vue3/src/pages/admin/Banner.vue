<script setup lang="ts">
import {computed, reactive, ref} from "vue";
import useBanner from "@/composables/useBanner";
import {message, notification, TableProps} from "ant-design-vue";
import moment from 'moment'
import Crate from "@/pages/admin/components/banner/BannerCrate.vue";
import Update from "@/pages/admin/components/banner/BannerUpdate.vue";

const {bannerPage, deleteBanner, deleteBannerBatch} = useBanner()

const loadParams = reactive<BannerModel.BannerQueryDto>({
  current: 1, pageSize: 10, name: undefined
})

const dataSource = ref<BannerModel.BannerVo[]>([])
const total = ref<number>(0)
const createVisible = ref<boolean>(false)
const updateVisible = ref<boolean>(false)
const currentData = ref<BannerModel.BannerVo>({
  id: 0,
  cover: '',
  name: '',
  userInfo: {id: 0, name: '', role: '', avatar: '', account: ''},
  createTime: '',
  updateTime: ''
})

const loadPage = async (params: BannerModel.BannerQueryDto) => {
  const res = await bannerPage({...params});
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
  {title: '封面', dataIndex: 'cover', align: 'center', ellipsis: true},
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
  const res = await deleteBanner({id: id})
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

const handleUpdate = (value: BannerModel.BannerVo) => {
  currentData.value = value;
  updateVisible.value = true;
}

</script>

<template>
  <a-card style="width: 100%">
    <template #title>
      <span class="font-bold">Banner管理</span>
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
        <template v-if="column.dataIndex === 'cover'">
          <a-image
              :width="100"
              :src="record?.cover"
              fallback="data:image/png;base64,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"
          />
        </template>
        <template v-else-if="column.dataIndex === 'action'">
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