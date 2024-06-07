<template>
  <van-cell title="当前用户" :value="user?.username" />
  <van-cell title="我的信息" is-link to="/user/update" />
  <van-cell title="我加入的队伍" is-link to="/user/join" />
  <van-cell title="我创建的队伍" is-link to="/user/create" />
  <van-cell title="登出" @click="loginOut"/>
</template>
<script setup>
import {useRouter} from "vue-router";
import {onMounted,ref} from "vue";
import getCurrentUser from "../config/getCurrentUser.ts";
import myAxios from "../plugins/myAxios.js";
import {showFailToast, showSuccessToast} from "vant";

const router = useRouter()
const route = useRouter()
// let user = ref({
//   username:"李伟亮",
//   userAccount:"李伟亮",
//   avatarUrl: "https://picsum.photos/seed/picsum/200/300",
//   gender:'男',
//   phone:"123",
//   email:"123",
//   userRole:'管理员',
//   planetCode:"1234",
//   tags:["web","女"],
//   createTime:new Date()
// })

const user = ref('')
//todo 头像编辑单独弄一个页面，上传图片，预览，并提交到后台


// 方法
onMounted(async ()=>{
  user.value = await getCurrentUser()
})
const loginOut =async ()=>{
  const res = await myAxios.post("/user/logout")
  if (res.code === 0 && res){
    showSuccessToast("登出成功")
    await router.push({
      path: '/login',
      replace: true
    })
  }else {
    showFailToast("登出失败")
  }
}
</script>
<style scoped>

</style>