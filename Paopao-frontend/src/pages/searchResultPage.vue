<template>
  <user-cord-list :user-list="userList"/>
  <!-- 搜索提示 -->
  <van-empty v-if="!userList || userList.length < 1" image="search" description="搜索结果为空" />
</template>
<script setup>
import {useRoute} from "vue-router";
import { ref,onMounted} from 'vue'
import myAxios from "../plugins/myAxios.js";
import qs from 'qs'
import UserCordList from "./userCordList.vue";
const route = useRoute()
const { tagsList } = route.query
let userList = ref([])

onMounted(async ()=>{
  const userListDate = await myAxios.get('/user/search/tags', {
    params: {
      tagNameList: tagsList
    },
    // 使用qs库的stringfy方法来序列化数组参数，序列化为url参数形式
    // indices:false 不要在参数后面加中括号，即不要使用 ids[]=1&ids[]=2 的形式
    //下面 params 不要改固定参数
    paramsSerializer:params => {
      return qs.stringify(params,{ indices:false })
    }
  })
      .then(function (response) {
        console.log("/search/tags succeed",response);
        return response.date;
      })
      .catch(function (error) {
        console.log("/search/tags failed",error);
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