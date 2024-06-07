<template>
  <van-pull-refresh
      v-model="isLoading"
      success-text="刷新成功"
      @refresh="onRefresh"
  >
    <van-tabs v-model:active="active" @change="onBarChange">
      <van-tab title="公开" name="public"/>
      <van-tab title="加密" name="secret"/>
    </van-tabs>
        <team-cord-list :team-list="recommendTeamList" title="recommend"/>
  </van-pull-refresh>
</template>
<script setup>
import {useRouter} from "vue-router";
import TeamCordList from "./TeamCordList.vue";
import {ref,onMounted} from "vue";
import myAxios from "../plugins/myAxios.js";
let recommendTeamList = ref([])
const loading = ref(false);
const isLoading = ref(true)
const active = ref('public');
//记录页面刷新次数，同时也记录 分页请求页
const count = ref('0');
const router = useRouter()
//方法
const onBarChange = ()=>{

}
const onRefresh = () => {
  setTimeout(() => {
    showToast('刷新成功');
    loading.value = false;
    isLoading.value = false;
    count.value++
  }, 1000);
};

onMounted(async ()=>{
  const res = await myAxios.get("/team/recommend/page",{
    params:{
      pageSize: 10,
      pageNum:1
    }
  })
  if (res?.code===0 && res.date){
    recommendTeamList.value = res.date.records
  }
})

</script>
<style scoped>
</style>
