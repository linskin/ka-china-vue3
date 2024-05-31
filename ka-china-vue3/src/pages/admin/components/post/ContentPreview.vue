<script setup lang="ts">
import {computed, nextTick, onMounted, reactive, ref, watch} from "vue";
import ByteMDViewer from "@/plugins/bytemd/ByteMDViewer.vue";

const content = ref<string>('');

interface IProps {
  visible: boolean;
  content: string;
}

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
    if (!dom[0]) {
      return
    }
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
    catalogue.catalogueState = nodeInfo[0]?.point as string
  }, 600)
}

const componentDidMount = () => {
  nextTick(() => {
    getElement()
  })
}

onMounted(() => {
  componentDidMount()
})

const handleSkipItem = (data: ICatalogue) => {
  catalogue.catalogueState = data.point
  setTimeout(() => {
    document.querySelector('#' + data.point)?.scrollIntoView({behavior: "smooth"})
  }, 100)
}

// @ts-ignore
const props = withDefaults(defineProps<IProps>(), {
  visible: false,
})

const emit = defineEmits(['onCancel'])
watch(() => props.content, (val) => {
  content.value = val || '';
  componentDidMount()
}, {immediate: true})
const dialogVisible = computed({
  get: () => props.visible,
  set: () => {
    emit('onCancel')
  }
})

const handleCancel = () => {
  emit('onCancel')
}
</script>

<template>
  <a-modal v-model:visible="dialogVisible" width="100%" wrap-class-name="full-modal" title="内容详情"
           @cancel="handleCancel">
    <section class="w-full h-full flex">
      <div class="fixed z-40 hidden lg:block">
        <a-card style="width: 230px" :bodyStyle="{padding: '1rem 0.2rem'}">
          <template #title>
            <span class="text-theme font-bold">目录</span>
          </template>
          <span class="px-3" v-if="catalogue.catalogueData.length === 0">暂无数据</span>
          <section v-else class="flex flex-col">
            <span v-for="(item, key) in catalogue.catalogueData" @click="handleSkipItem(item)"
                  :key="key"
                  :class="catalogue.catalogueState === item.point ? 'text-base leading-8 bg-gray-100 text-theme font-bold px-3': 'text-base leading-8 hover:bg-gray-100 hover:text-theme hover:font-bold px-3 cursor-pointer'">
                            {{ item.txt }}
            </span>
          </section>
        </a-card>
      </div>
      <div class="flex-1 ml-0 lg:ml-60">
        <ByteMDViewer :value="content"/>
      </div>
    </section>
    <template #footer>
      <section class="flex justify-end">
        <a-button type="primary" size="small" @click="() => {dialogVisible=false; handleCancel()}">关闭</a-button>
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