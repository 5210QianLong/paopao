<template>
  <van-form @submit="onSubmit">
    <van-cell-group inset>
      <van-field
          v-model="password"
          name="password"
          label="密码"
          placeholder="请输入密码"
          :rules="[{ required: true, message: '请输入密码' }]"
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
import {ref } from 'vue';
import {useRouter} from "vue-router";
const router = useRouter()
const password = ref()
// 方法区

const onSubmit = () => {
  router.push({
    path:'/team',
    replace:true,
    query:{
      password:password.value
    }
  })

};
</script>
<style scoped>

</style>