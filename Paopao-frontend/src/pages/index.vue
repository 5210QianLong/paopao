<template>
  <user-cord-list :user-list="userList"/>
</template>
<script setup>
import { ref,onMounted} from 'vue'
import myAxios from "../plugins/myAxios.js";

import UserCordList from "./userCordList.vue";
let userList = ref([])

onMounted(async ()=>{
  const userListDate = await myAxios.get('/user/recommend', {
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
  if (userListDate){
    userListDate.forEach(user =>{
      // 后端发回来的tags是一个字符串，将它解析为数组
      if (user.tags){
        user.tags = JSON.parse(user.tags)
      }
    })
    userList.value = userListDate
  }
})


//方法

</script>
<style scoped>

</style>