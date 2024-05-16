<template>
  <van-card
      v-for="user in props.userList" :key="user.id"
      :desc="user.profile"
      :title="`${user.username}(${user.planetCode})`"
      :thumb="user.avatarUrl"
  >
    <template #tags>
      <van-tag plain type="primary" v-for="tag in user.tags" style="margin-right: 8px;margin-top: 8px">
        {{tag}}
      </van-tag>
    </template>
    <template #footer>
      <van-button size="mini">私信</van-button>
      <van-button size="mini">关注</van-button>
    </template>
  </van-card>
  <!-- 搜索提示 -->
<!--  <van-empty v-if="!userList || userList.length < 1" image="search" description="搜索结果为空" />-->
</template>

<script setup lang="ts">
import {UserType} from "../models/user";

interface UserCordListProps{
  userList:UserType[]
}
const props =withDefaults(defineProps<UserCordListProps>(),{
  //若不加ts类型限定  defineProps(['user-list'])直接接收数据即可
  //@ts-ignore
  userList: [] as UserType[],
});
</script>



<style scoped>

</style>