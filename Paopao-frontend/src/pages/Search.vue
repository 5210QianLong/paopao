<template>
  <form action="/">
    <van-search
        v-model="searchTest"
        show-action
        placeholder="请输入要搜索的标签"
        @search="onSearch"
        @cancel="onCancel"
    />
  </form>
  <van-divider content-position="left"
      :style="{ color: '#1989fa', borderColor: '#1989fa', padding: '0 16px',}"
  >
    已选标签
  </van-divider>

  <!-- 设置垂直间距 -->
  <div class="selectBrand" v-if="activeIds.length===0">请选择标签</div>
  <van-row :gutter="[20, 100]" style="padding: 0 15px">
    <van-col v-if="activeIds.length!==0" v-for="tag in activeIds" style="margin-top: 3px">
      <van-tag  closeable size="medium" type="primary" @close="close(tag)" plain>
        {{tag}}
      </van-tag>
    </van-col>
  </van-row>

  <van-divider content-position="left"
      :style="{ color: '#1989fa', borderColor: '#1989fa', padding: '0 16px' }"
  >
    选择标签
  </van-divider>
  <van-tree-select
      v-model:active-id="activeIds"
      v-model:main-active-index="activeIndex"
      :items="tagList"
  />
  <van-button type="primary" style="width: 96%;margin-left: 2%" @click="doSearchResult">搜 索</van-button>
</template>
<script setup>
import { ref } from 'vue';
import {useRouter} from "vue-router";

const router = useRouter()
const searchTest = ref('');
//保存下tagName最开始的值
const oldList =[
  {
    text: '性别',
    children: [
      { text: '男', id: "男" },
      { text: '女', id: "女" },
    ],
  },
  {
    text: '年级',
    children: [
      { text: '大一', id: '大一' },
      { text: '大二', id: '大二' },
      { text: '大三', id: '大三' },
      { text: '大4', id: '大4' },
      { text: '大5', id: '大5' },
      { text: 'java', id: 'java' },
    ],
  },
]
let tagList = ref(oldList);
//右侧选中项的索引
const activeIds = ref([]);
//左侧选中项的索引
const activeIndex = ref(0);






const onSearch = (val) => {
  //flatMap() 方法首先使用映射函数映射每个元素，然后将结果压缩成一个新数组
 // tagList.value =  tagList.value.flatMap(parentTag=>parentTag.children)
 //          .filter(item=>{item.id.includes(searchTest.value)})
  tagList.value = oldList.map(parentTag => {
    const  tempchildren = [...parentTag.children];
    //[...parentTag.children] 将children数组复制一份，es6新语法，虽然我不懂
    const  tempchildtag = {...parentTag};
    tempchildtag.children = tempchildren.filter(item =>item.text.includes(searchTest.value))
  return tempchildtag;
  })

};
const onCancel = () => {
  tagList.value = oldList
  searchTest.value = ""
};
//删除已选中的标签
const close = (tag) => {
  activeIds.value = activeIds.value.filter(item =>{
    return tag !== item;
  })
};

const doSearchResult = ()=>{
  router.push({
    path: "/search/searchResult",
    query: {
      tagsList: activeIds.value
    }
  })
}

</script>
<style scoped>
.selectBrand{
  font-size: 15px;
  font-family: Bahnschrift,serif;
  margin-left: 20px;
}
</style>