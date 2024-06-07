<template>
  <van-cell center title="相似伙伴">
    <template #right-icon >
      <van-switch v-model="isLoveMode"/>
    </template>
  </van-cell>
<!--  <van-loading vertical v-if="isLoading" text-color="#0094ff" text-size="20px" style="margin-top: 40px">-->
<!--    <template #icon>-->
<!--      <van-icon name="star-o" size="50" color="#0094ff" />-->
<!--    </template>-->
<!--    <p>加 载 中 ...</p>-->
<!--  </van-loading>-->
  <user-cord-list :user-list="userList"  :loading="isLoading"/>
</template>
<script setup>
import { ref,watchEffect} from 'vue'
import myAxios from "../plugins/myAxios.js";

import UserCordList from "./userCordList.vue";
let userList = ref([])
const isLoveMode = ref(false)
const isLoading = ref(true)
//方法
watchEffect(async ()=>{
  let userListDate;
  isLoading.value=true
  if (isLoveMode.value){
    //相似用户
    userListDate = await myAxios.get('/user/match', {
      params: {
        num:10
      },
    })
        .then(function (response) {
          console.log("/match succeed",response);
          return response.date;
        })
        .catch(function (error) {
          console.log("/match failed",error);
        })
  }else {
    //不是相似用户
    userListDate = await myAxios.get('/user/recommend', {
      params: {
        pageSize:8,
        pageNum:1
      },
    })
        .then(function (response) {
          console.log("/recommend succeed",response);
          return response.date.records;
        })
        .catch(function (error) {
          console.log("/recommend failed",error);
        })
  }
  if (userListDate){
    userListDate.forEach(user =>{
      // 后端发回来的tags是一个字符串，将它解析为数组
      if (user.tags){
        user.tags = JSON.parse(user.tags)
      }
    })
    userList.value = userListDate
  }
  isLoading.value=false
})
</script>
<style scoped>

</style>