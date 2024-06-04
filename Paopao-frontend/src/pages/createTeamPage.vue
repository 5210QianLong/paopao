<template>
  <van-form @submit="onSubmit">
    <van-cell-group inset>
      <van-field
          v-model="pageTeamTable.teamName"
          name="teamName"
          label="队伍名"
          placeholder="请输入队伍名"
          :rules="[{ required: true, message: '请输入队伍名' }]"
      />
      <van-field
          v-model="pageTeamTable.description"
          rows="3"
          autosize
          label="队伍描述"
          type="textarea"
          maxlength="50"
          placeholder="请输入队伍描述"
          show-word-limit
      />
      <van-field
          v-model="pageTeamTable.expireTime"
          is-link
          readonly
          name="expireTime"
          label="队伍过期时间"
          placeholder="请选择队伍过期时间"
          @click="showPicker = true"
      />
      <van-popup v-model:show="showPicker" position="bottom">
        <van-date-picker
            @confirm="onConfirm"
            :min-date="minDate"
            type="datetime"
            @cancel="showPicker = false"
        />
      </van-popup>
      <van-field name="maxNum" label="最大人数">
        <template #input>
          <van-stepper v-model="pageTeamTable.maxNum" max="10"/>
        </template>
      </van-field>
      <van-field name="status" label="队伍状态">
        <template #input>
          <van-radio-group v-model="pageTeamTable.status" direction="horizontal">
            <van-radio name="0">公开</van-radio>
            <van-radio name="1">私有</van-radio>
            <van-radio name="2">加密</van-radio>
          </van-radio-group>
        </template>
      </van-field>
      <van-field
          v-if="pageTeamTable.status==='2'"
          v-model="pageTeamTable.password"
          type="password"
          name="password"
          label="密码"
          placeholder="请输入密码"
          :rules="[{ required: true, message: '请填写密码' }]"
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
  import {ref} from "vue";
  import myAxios from "../plugins/myAxios.js";
  import {showFailToast, showSuccessToast} from "vant";
  import {useRouter} from "vue-router";
  const router = useRouter()
  let minDate =new Date()
  const teamTable ={
      teamName: "",
      description: "",
      maxNum: 1,
      expireTime: minDate,
      status: 0,
      password: ""
  }
  //加了... 不会修改原值
  let pageTeamTable = ref({...teamTable})
  const showPicker = ref(false);

  //方法
  const onSubmit = async()=>{
    const postDate ={
      ...pageTeamTable.value,
      status: Number(pageTeamTable.value.status)
    }
    //todo 日期不能为空

    const res = await myAxios.post("/team/add",postDate)
    if (res.code===0 && res.data){
      await showSuccessToast("添加成功")
      router.push({
        path:"/team",
        replace:true
      })
    }else {
      showFailToast("添加失败")
    }
  }
  const onConfirm = ({ selectedValues }) => {
    pageTeamTable.value.expireTime = selectedValues.join('-');
    showPicker.value = false;
  };
</script>
<style scoped>

</style>