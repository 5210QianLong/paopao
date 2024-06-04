<template>
  <van-pull-refresh
      v-model="isLoading"
      success-text="刷新成功"
      @refresh="onRefresh"
  >
        <team-cord-list :team-list="teamList" title="joinTeam"/>
  </van-pull-refresh>
</template>
<script setup>
import {useRouter} from "vue-router";
import TeamCordList from "./TeamCordList.vue";
import {ref,onMounted} from "vue";
import myAxios from "../plugins/myAxios.js";
import getCurrentUser from "../config/getCurrentUser.ts";
let teamList = ref([])
const loading = ref(false);
const isLoading = ref(true)
//记录页面刷新次数，同时也记录 分页请求页
const count = ref('0');
const router = useRouter()
//方法

const onRefresh = () => {
  setTimeout(() => {
    showToast('刷新成功');
    loading.value = false;
    isLoading.value = false;
    count.value++
  }, 1000);
};
onMounted(async ()=>{
  const response = await getCurrentUser()
  const res = await myAxios.get("/team/currentTeams",{
   params:{
     id:response?.id
   }
 })
  if (res?.code===0 && res.date)
    if (res.date.length===1){
      teamList.value = [res]
    }
    teamList.value = res.date
})

</script>
<style scoped>
</style>
