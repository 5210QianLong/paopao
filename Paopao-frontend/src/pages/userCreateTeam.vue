<template>
  <van-pull-refresh
      v-model="isLoading"
      success-text="刷新成功"
      @refresh="onRefresh"
  >
    <van-tabs  type="line" line-height="0px">
      <van-tab title="我创建的队伍" >
        <team-cord-list :team-list="recommendTeamList" title="createTeam" v-if="recommendTeamList.length!==0"/>
      </van-tab >
      <van-empty description="暂无队伍" v-if="recommendTeamList.length===0"/>
    </van-tabs>

  </van-pull-refresh>
  <div id="addTeamButton">
    <van-button type="primary" block @click="addTeam">点 我 创 建 队 伍</van-button>
  </div>
</template>
<script setup>
import {useRouter} from "vue-router";
import TeamCordList from "./TeamCordList.vue";
import {ref,onMounted} from "vue";
import myAxios from "../plugins/myAxios.js";
import getCurrentUser from "../config/getCurrentUser.ts";
let recommendTeamList = ref([])
const loading = ref(false);
const isLoading = ref(true)
//记录页面刷新次数，同时也记录 分页请求页
const count = ref('0')
//方法

const onRefresh = () => {
  setTimeout(() => {
    showToast('刷新成功');
    loading.value = false;
    isLoading.value = false;
    count.value++
  }, 1000);
};
// const onClickTab = async ()=>{
//   const res = await myAxios.get("/team/recommend/page",{
//     params:{
//       pageSize: 10,
//       pageNum:1
//     }
//   })
//   if (res?.code===0 && res.date){
//       recommendTeamList.value = res.date.records
//   }
// }
onMounted(async ()=>{
  const response = await getCurrentUser()
  console.log(response.id)
  const res = await myAxios.get("/team/getTeam",{
   params:{
     id:response?.id
   }
 })
  if (res?.code===0 && res.date)
    if (res.date.length===1){
      recommendTeamList.value = [res]
    }
    recommendTeamList.value = res.date
})
const router = useRouter()
  const addTeam = ()=>{
  router.push({
    path:"/user"
  })
  }
</script>
<style scoped>
  #addTeamButton{
    width: 96%;
    margin-left: 2%;
  }
</style>
