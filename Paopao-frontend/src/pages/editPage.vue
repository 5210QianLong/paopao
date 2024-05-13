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
import { ref } from 'vue';
import {useRoute} from "vue-router";
const route = useRoute();


// 方法区
const editUser = ref({
  name:route.query.name,
  title:route.query.title,
  value:route.query.value
})
const onSubmit = (values) => {
  //todo 将数据提交到后台
  console.log(values)
};
</script>
<style scoped>

</style>