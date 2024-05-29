<template>
  <van-form @submit="onSubmit">
    <van-cell-group inset>
      <van-field
          v-model="editUser.value"
          :name="editUser.name"
          :label="editUser.title"
          placeholder="用户名"
          :rules="[{ required: true, message: '请填写用户名' }]"
      />
    </van-cell-group>
    <div style="margin: 16px;">
      <van-button round block type="primary" native-type="submit">
        提交
      </van-button>
    </div>
  </van-form>

</template>
<script setup>
import {ref, computed, onMounted} from 'vue';
import {useRoute, useRouter} from "vue-router";
import myAxios from "../plugins/myAxios.js";
import {showFailToast, showSuccessToast} from "vant";

const router = useRouter()
const route = useRoute();
let currentUserId = ref();

const editUser = ref({
  name:route.query.name,
  title:route.query.title,
  value:route.query.value
})
// 方法区
// const userValue = computed({
//   get(){
//     if (editUser.value.name === 'gender'){
//       return editUser.value.value === '0' ? "男" : "女"
//     }
//     return editUser.value.value
//   },
//   set(){
//     editUser.value.value = userValue.value
//   }
// })
onMounted(async ()=>{
  const res = await myAxios.get("/current")
  currentUserId.value= res.date.id
})
const onSubmit = async () => {
  //todo 将gender 转换成 0/1
  if ([[editUser.value.name]==='gender']){
    editUser.value.value=editUser.value.value==='男'?0:1
  }
  //todo 将数据提交到后台
  //思路：通过登录获得登录态session，拿到id,知道是哪个人,然后更新某一字段
  const response = await myAxios.post("/user/update",{
    'id':currentUserId.value,
    [editUser.value.name]: editUser.value.value
  })
  console.log(response,"更新请求")
  if (response.code===0 && response.date>0){
    showSuccessToast("修改成功")
    await router.push("/user")
  }else{
    showFailToast("修改失败")
  }

};
</script>
<style scoped>

</style>