<template>
  <van-pull-refresh
      v-model="isLoading"
      success-text="刷新成功"
      @refresh="onRefresh"
  >

    <team-cord-list :team-list="myTeam" title="createTeam" v-if="myTeam.length!==0"/>
    <van-empty description="暂无队伍" v-if="myTeam.length===0"/>

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
let myTeam = ref([])
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
//       myTeam.value = res.date.records
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
      myTeam.value = [res]
    }
    myTeam.value = res.date
})
const router = useRouter()
  const addTeam = ()=>{
  router.push({
    path:"/team/teamEdit"
  })
  }
</script>
<style scoped>
  #addTeamButton{
    width: 96%;
    margin-left: 2%;
    position: fixed;
    bottom: 14vw;
  }
</style>
