<template>
  <div id="addTeamButton">
    <van-button type="primary" @click="addTeam">加入队伍</van-button>
  </div>
</template>
<script setup>
import {useRouter} from "vue-router";

const router = useRouter()
  const addTeam = ()=>{
  router.push({
    path:"/team/teamEdit"
  })
  }
</script>
<style scoped>
  #addTeamButton{
    margin-left: 20%;
  }
</style>
