<script setup lang="ts">
import {nextTick, onMounted, reactive, ref} from "vue";
import usePost from "@/composables/usePost.ts";
import {Empty, message} from "ant-design-vue";
import {useRoute} from "vue-router";
import moment from 'moment'
import ByteMDViewer from "@/plugins/bytemd/ByteMDViewer.vue";


const {postDetail} = usePost()
const route = useRoute()

const postId = route.params?.id as string

const detail = ref<PostModel.PostVo>({
  categories: [],
  content: "",
  cover: "",
  createTime: "",
  description: "",
  id: 0,
  title: "",
  updateTime: "",
  userInfo: {id: 0, name: '', role: '', avatar: '', account: ''}
})

const loadDetail = async () => {
  const res = await postDetail(postId);
  if (res?.code === 200) {
    detail.value = res.data
    document.title = detail.value?.title + '-KA中国' || '百科详情|KA中国'
  } else {
    res && message.error(res?.message)
  }
}
loadDetail()

interface ICatalogue {
  type: string;
  txt: string;
  offsetTop: number;
  point: string
}

const catalogue = reactive({
  catalogueData: <ICatalogue[]>[],
  catalogueState: '',
})

const getElement = () => {
  let nodeArr: string[];
  nodeArr = ['H1', 'H2', 'H3', 'H4'];
  let nodeInfo: ICatalogue[] = []
  setTimeout(() => {
    const dom: HTMLCollection = document.getElementsByClassName('markdown-body')
    const domChildNodes = dom[0].childNodes
    domChildNodes.forEach((item: any, key: number) => {
      if (nodeArr.includes(item.nodeName)) {
        nodeInfo.push({
          type: item.nodeName,
          txt: item.innerText,
          offsetTop: item.offsetTop,
          point: `heading-${key}`,
        })
        item.setAttribute('id', `heading-${key}`)
      }
    })
    catalogue.catalogueData = nodeInfo
    catalogue.catalogueState = nodeInfo[0]?.txt!
  }, 600)
}

const componentDidMount = () => {
  nextTick(() => {
    getElement()
  })
}
const onScroll = (e: any) => {
  let scrollTop = e.target.documentElement.scrollTop || e.target.body.scrollTop
  let windowHeight = document.documentElement.clientHeight || document.body.clientHeight
  let scrollHeight = document.documentElement.scrollHeight || document.body.scrollHeight
  let currentCatalogue = catalogue.catalogueData[0]?.txt
  for (let item of catalogue.catalogueData) {
    if (scrollTop >= item.offsetTop) {
      currentCatalogue = item?.txt
    } else break
  }
  if (currentCatalogue !== catalogue.catalogueState) {
    catalogue.catalogueState = currentCatalogue!
  }
  if (scrollTop + windowHeight === scrollHeight) {
    catalogue.catalogueState = catalogue.catalogueData[catalogue.catalogueData.length - 1]?.txt!
  }
}

onMounted(() => {
  componentDidMount()
  window.addEventListener('scroll', onScroll, true)
})

const handleSkipItem = (offsetTop: number) => {
  setTimeout(() => {
    document.documentElement.scrollTop = offsetTop
  }, 100)
}

</script>

<template>
  <section class="max-w-[1200px] flex mx-auto">
    <div class="max-w-[1200px] xl:max-w-[880px] flex-1">
      <a-card>
        <template #title>
          <span class="text-xl font-bold">{{ detail.title }}</span>
        </template>
        <div class="flex items-center">
          <div>
            <a-avatar :size="40" :src="detail?.userInfo?.avatar"/>
          </div>
          <div class="flex flex-col ml-1.5">
            <span>{{ detail?.userInfo?.name }}</span>
            <span
                class="text-xs text-gray-400">发布于 {{ moment(detail.createTime).format('YYYY-MM-DD HH:mm:ss') }}</span>
          </div>
        </div>
        <div>
          <ByteMDViewer :value="detail.content"/>
        </div>
      </a-card>
    </div>
    <div>
      <div class="ml-5 hidden xl:block">
        <a-card style="width: 300px">
          <div class="flex items-center justify-between">
            <div class="flex items-center">
              <div>
                <a-avatar :size="50" :src="detail?.userInfo?.avatar"/>
              </div>
              <div class="flex flex-col ml-1.5">
                <span class="font-bold">{{ detail?.userInfo?.name }}</span>
                <span
                    class="text-xs text-gray-400">No BUG</span>
              </div>
            </div>
            <div class="flex">
              <a-button type="primary" size="small" shape="round">关注</a-button>
            </div>
          </div>
          <div class="w-full h-14 flex items-center justify-between mt-8">
            <div class="flex flex-col items-center justify-center">
              <span class="text-theme">3</span>
              <span>帖子</span>
            </div>
            <span>|</span>
            <div class="flex flex-col items-center justify-center">
              <span class="text-theme">3</span>
              <span>关注</span>
            </div>
            <span>|</span>
            <div class="flex flex-col items-center justify-center">
              <span class="text-theme">3</span>
              <span>收藏</span>
            </div>
          </div>
        </a-card>
        <br>
        <a-card style="width: 300px;">
          <template #title>
            文章信息
          </template>
          <a-space>
            <span>类别:</span>
            <a-tag color="#108ee9" v-for="item in detail.categories" :key="item.id">{{item.name}}</a-tag>
          </a-space>
          <div class="mt-3">{{detail?.description}}</div>
        </a-card>
        <br>
        <a-card style="width: 300px;">
          <template #title>
            相关推荐
          </template>
          <a-empty :image="Empty.PRESENTED_IMAGE_SIMPLE" />
        </a-card>
        <br>
        <a-affix :offset-top="70">
          <a-card class="tw-w-80 tw-mt-64" :bodyStyle="{padding: '1rem 0.2rem'}">
            <template #title>
              <span class="text-theme font-bold">目录</span>
            </template>
            <span class="px-3" v-if="catalogue.catalogueData.length === 0">暂无数据</span>
            <section v-else class="flex flex-col">
                    <span v-for="(item, key) in catalogue.catalogueData" @click="handleSkipItem(item.offsetTop)"
                          :key="key"
                          :class="catalogue.catalogueState === item.txt ? 'text-base leading-8 bg-gray-100 text-theme font-bold px-3': 'text-base leading-8 hover:bg-gray-100 hover:text-theme hover:font-bold px-3 cursor-pointer'">
                            {{ item.txt }}
                    </span>
            </section>
          </a-card>
        </a-affix>
      </div>
    </div>

  </section>
</template>

<style lang="scss" scoped>

</style>