<template>
    <van-cell title="昵称" is-link :value="user.username"  @click="toEdit('nickname','昵称',user.username)"/>
    <van-cell title="账户"  :value="user.userAccount" />
    <van-cell title="头像" is-link @click="toEdit('avatarUrl','头像',user.avatarUrl)">
      <img style="height:48px" :src="user.avatarUrl"  alt=""/>
    </van-cell>
    <van-cell title="性别" is-link :value="user.gender" @click="toEdit('gender','性别',user.gender)"/>
    <van-cell title="电话" is-link :value="user.phone" @click="toEdit('phone','电话',user.phone)"/>
    <van-cell title="邮件" is-link :value="user.email" @click="toEdit('email','邮件',user.email)"/>
    <van-cell title="权限"  :value="user.userRole"/>
    <van-cell title="星球编号"  :value="user.planetCode"/>
    <van-cell title="标签" is-link :value="user.tags.toString()" @click="toEdit('tags','标签',user.tags.toString())"/>
    <van-cell title="创建时间"  :value="user.createTime.toISOString()" />
</template>
<script setup lang="ts">
import {useRouter} from "vue-router";

const router = useRouter()
const user={
  username:"李伟亮",
  userAccount:"李伟亮",
  avatarUrl: "https://picsum.photos/seed/picsum/200/300",
  gender:'男',
  phone:"123",
  email:"123",
  userRole:'管理员',
  planetCode:"1234",
  tags:["web","女"],
  createTime:new Date()
}

//todo 头像编辑单独弄一个页面，上传图片，预览，并提交到后台

// 方法
const toEdit =(editKey:string,editTitle:string,currentValue:string)=>{
    router.push({
      path:'/user/edit',
      query:{
        name:editKey,
        title:editTitle,
        value:currentValue
      }
    })
}
</script>
<style scoped>

</style>