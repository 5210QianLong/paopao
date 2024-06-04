<template>
  <van-nav-bar
      :title="topTitle"
      left-arrow
      @click-left="onClickLeft"
      @click-right="onClickRight"

  >
    <template #right>
      <van-icon name="search" size="18"/>
    </template>
  </van-nav-bar>
  <div id="contend">
    <router-view></router-view>
  </div>

  <van-tabbar route  @change="onChange" >
    <van-tabbar-item to="/" icon="home-o" name="推荐" >推荐</van-tabbar-item>
    <van-tabbar-item to="/team" icon="friends-o" name="队伍">队伍</van-tabbar-item>
    <van-tabbar-item to="/user" icon="user-o" name="个人">个人</van-tabbar-item>
  </van-tabbar>


</template>
<script setup>
import {useRouter} from "vue-router";
import {onMounted, ref} from 'vue'

const topTitle = ref("推荐")
const router = useRouter();
const onClickLeft = () => history.back();
const onClickRight = () => {
  router.push('/search')
};
const onChange = (index) =>{topTitle.value=index};
onMounted(()=>{
  router.replace("/login")
})
</script>
<style scoped>
#contend{
  padding-bottom: 50px;
}
</style>