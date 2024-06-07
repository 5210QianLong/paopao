<template>
  <van-nav-bar
      :title="topTitle"
      @click-left="onClickLeft"
      @click-right="onClickRight"

  >
    <template #left >
      <van-icon name="arrow-left" size="20"/>
    </template>
    <template #right >
      <van-icon name="search" size="20"/>
    </template>
  </van-nav-bar>
  <div id="contend">
    <router-view></router-view>
  </div>

  <van-tabbar route v-if="showBar">
    <van-tabbar-item to="/" icon="home-o">推荐</van-tabbar-item>
    <van-tabbar-item to="/team" icon="friends-o" >队伍</van-tabbar-item>
    <van-tabbar-item to="/user" icon="user-o" >个人</van-tabbar-item>
  </van-tabbar>


</template>
<script setup>
import {useRoute, useRouter} from "vue-router";
import {ref} from 'vue'
import routes from "../config/router.ts";
const router = useRouter();
const route = useRoute()
//这里留着 登录页 的 标签栏的显示
const showBar = ref(true)
const DEFAULT_TITLE = '伙伴匹配'
const topTitle = ref(DEFAULT_TITLE)
router.beforeEach((to)=>{
  const toPath = to.path
  const matchRoute = routes.find((route)=>{
    return toPath === route.path;
  })
  topTitle.value = matchRoute?.title ?? DEFAULT_TITLE;
})
const onClickLeft = () => router.back();
const onClickRight = () => {
  router.push('/search')
}
</script>
<style scoped>
#contend{
  padding-bottom: 50px;
}
</style>