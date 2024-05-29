<template>
    <template v-if="user">
      <van-cell title="昵称" is-link :value="user.username"  @click="toEdit('username','昵称',user.username)"/>
      <van-cell title="账户"  :value="user.userAccount" />
      <van-cell title="头像" is-link @click="toEdit('avatarUrl','头像',user.avatarUrl)">
        <img style="height:48px" :src="user.avatarUrl"  alt=""/>
      </van-cell>
      <van-cell title="性别" is-link :value="user.gender" @click="toEdit('gender','性别',user.gender)"/>
      <van-cell title="电话" is-link :value="user.phone" @click="toEdit('phone','电话',user.phone)"/>
      <van-cell title="邮件" is-link :value="user.email" @click="toEdit('email','邮件',user.email)"/>
      <van-cell title="权限"  :value="user.userRole"/>
      <van-cell title="星球编号"  :value="user.planetCode"/>
      <van-cell title="标签" is-link :value="user.tags.toString()" @click="toEdit('tags','标签',user.tags)"/>
      <van-cell title="创建时间"  :value="user.createTime" />
    </template>
    <template v-if="!user">
      <van-empty
          image="https://fastly.jsdelivr.net/npm/@vant/assets/custom-empty-image.png"
          image-size="100"
          description="无用户信息,请登录"
      />
    </template>
</template>
<script setup>
import {useRouter} from "vue-router";
import {onMounted,ref} from "vue";
import myAxios from "../plugins/myAxios";

const router = useRouter()
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
  const res = await myAxios.get("/user/current")
  if (res.code === 0 ){
    res.date.gender = res.date.gender === 0?"男":"女"
    res.date.tags = JSON.parse(res.date.tags)
    user.value = res.date
  }
})
const toEdit =(editKey,editTitle,currentValue)=>{
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