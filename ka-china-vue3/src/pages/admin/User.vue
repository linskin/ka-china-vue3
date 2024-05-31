<script setup lang="ts">
import {computed, reactive, ref} from "vue";
import useUser from "@/composables/useUser";
import {message, notification, TableProps} from "ant-design-vue";
import moment from 'moment'
import Crate from "@/pages/admin/components/user/UserCrate.vue";
import Update from "@/pages/admin/components/user/UserUpdate.vue";

const {userPage, deleteUser, changeRole, resetPassword} = useUser()

const loadParams = reactive<UserModel.UserQueryDto>({
    current: 1, pageSize: 10, name: undefined, account: undefined
})

const dataSource = ref<UserModel.UserVo[]>([])
const total = ref<number>(0)
const createVisible = ref<boolean>(false)
const updateVisible = ref<boolean>(false)
const currentUser = ref<UserModel.UserVo>({
  account: "",
  avatar: "",
  createTime: "",
  id: 0,
  name: "",
  role: "",
  updateTime: ""
})

const loadPage = async (params: UserModel.UserQueryDto) => {
    const res = await userPage({...params});
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
    {title: '姓名', dataIndex: 'name', align: 'center', ellipsis: true},
    {title: '账号', dataIndex: 'account', align: 'center', ellipsis: true},
    {title: '头像', dataIndex: 'avatar', align: 'center', ellipsis: true},
    {title: '用户角色', dataIndex: 'role', align: 'center', ellipsis: true,},
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
    {title: '操作', dataIndex: 'action', align: 'center', width: '390px'},
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
    if (loadParams.account === undefined && loadParams.name === undefined) {
        notification.warning({
            message: 'Warning',
            description: '请输入查询条件后进行搜索'
        });
        return
    }
    loadPage(loadParams)
}

const handleReset = () => {
    if (loadParams.account !== undefined || loadParams.name !== undefined) {
        loadParams.account = undefined;
        loadParams.name = undefined;
        loadPage(loadParams)
    }
}

const handleDelete = async (id: number) => {
    const res = await deleteUser({id: id})
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

const handleUpdate = (value: UserModel.UserVo) => {
  currentUser.value = value;
  updateVisible.value = true;
}

const handleChangeRole = async (id: number) => {
  const res = await changeRole({id})
  if (res?.code === 200) {
    notification.success({
      message: 'Success',
      description: '修改成功'
    });
    await loadPage(loadParams)
  } else {
    res && message.error(res?.message)
  }
}

const handleResetPassword = async (id: number) => {
  const res = await resetPassword({id})
  if (res?.code === 200) {
    notification.success({
      message: 'Success',
      description: '重置成功'
    });
  } else {
    res && message.error(res?.message)
  }
}
</script>

<template>
    <a-card style="width: 100%">
        <template #title>
            <span class="font-bold">用户管理</span>
        </template>
        <a-space>
            <a-space>
                <span>昵称</span>
                <a-input v-model:value="loadParams.name" placeholder="请输入"/>
            </a-space>
            <a-space>
                <span>手机号</span>
                <a-input v-model:value="loadParams.account" placeholder="请输入"/>
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
                <template v-if="column.dataIndex === 'avatar'">
                    <a-avatar :src="record?.avatar"/>
                </template>
              <template v-if="column.dataIndex === 'role'">
                <a-tag v-if="record?.role === 'super_admin'" color="#ff5370">站长</a-tag>
                <a-tag v-else-if="record?.role === 'admin'" color="#108ee9">管理员</a-tag>
                <a-tag v-else color="#87d068">普通用户</a-tag>
              </template>
                <template v-else-if="column.dataIndex === 'action'">
                    <section class="flex justify-center">
                      <a-button type="text" class="!text-green-500" v-if="record?.role !== 'super_admin'"
                                @click="handleChangeRole(record?.id)">{{ record?.role === 'admin' ? '设为用户' : '设为管理员'}}</a-button>
                      <a-divider type="vertical"/>
                      <a-button type="text" class="!text-yellow-500"
                                @click="handleResetPassword(record?.id)">重置密码</a-button>
                      <a-divider type="vertical"/>
                        <a-button type="text" class="!text-theme"
                                  @click="handleUpdate(record)">更新</a-button>
                        <a-divider type="vertical"/>
                        <a-button type="text" @click="handleDelete(record?.id)" danger>删除</a-button>
                    </section>
                </template>
            </template>
        </a-table>
        <Crate :visible="createVisible" @on-cancel="() => {createVisible = false}"
                   @on-ok="() => {createVisible = false; loadPage(loadParams)}"/>
        <Update :visible="updateVisible" :data="currentUser" @on-cancel="() => {updateVisible = false}"
                    @on-ok="() => {updateVisible = false; loadPage(loadParams)}"/>
    </a-card>
</template>


<style lang="scss" scoped>
:deep(.ant-card-body) {
    padding: 10px;
}
</style>