<template>
  <div id="card">
    <van-card
        v-for="team in props.teamList" :key="team.id"
        :desc="team.description"
        :title="team.teamName"
        :thumb="youXi"
    >
      <template #tags>
        <van-tag plain type="primary" style="margin-right: 8px;margin-top: 8px">
          {{ teamStatusEnum[team.status] }}
        </van-tag>
      </template>
      <template #price-top>

      </template>
      <template #bottom>
        最大人数:{{team.maxNum}}
      </template>
      <template #footer>
        <template v-if="props.title==='recommend'">
          <van-button size="small" @click="matchStatus(team.id,team.status)"
                      plain hairline type="primary">加入队伍</van-button>
        </template>
        <template v-if="props.title==='joinTeam'">
          <van-button size="small" plain hairline type="primary">查看队伍</van-button>
          <van-button size="small" @click="doQuitTeam(team.id)" plain hairline type="primary">退出队伍</van-button>
        </template>
        <template v-if="props.title==='createTeam'">
          <van-button size="small" plain hairline type="primary">查看队伍</van-button>
          <van-button size="small"  plain hairline type="primary">更新队伍</van-button>
          <van-button size="small" @click="doDeleteTeam(team.id)" plain hairline type="primary"
                      color="#FE5933">解散队伍</van-button>
        </template>
      </template>
    </van-card>
    <van-dialog v-model:show="showPasswordDialog" title="请输入密码" show-cancel-button @confirm="enterTeam" @cancel="doJoinCancel">
      <van-field v-model="password" placeholder="请输入密码" />
    </van-dialog>
  </div>

  <!-- 搜索提示 -->
<!--  <van-empty v-if="!teamList || teamList.length < 1" image="search" description="搜索结果为空" />-->
</template>

<script setup lang="ts">
import {TeamType} from "../models/team";
import {teamStatusEnum} from "../constants/TeamStatusEnum.ts";
import youXi from "../assets/游戏图标.png"
import myAxios from "../plugins/myAxios.js";
import {showFailToast, showSuccessToast} from "vant";
import {onMounted,ref} from "vue";
import getCurrentUser from "../config/getCurrentUser.ts";
const showPasswordDialog = ref(false)
const password = ref()
const clickTeamId = ref()
const currentUser = ref()
//方法
onMounted(async ()=>{
   currentUser.value= await getCurrentUser();
})
const props =withDefaults(defineProps<{
  teamList:TeamType[],
  title:string
}>(),{
  //若不加ts类型限定  defineProps(['team-list'])直接接收数据即可
  //@ts-ignore
  teamList: ()=> [] as TeamType[]});
const enterTeam = async ()=>{
  if(!clickTeamId.value){
    return
  }
  const res = await myAxios.post("/team/join",{
    teamId:clickTeamId.value,
    password:password.value
  })
  if (res?.code===0){
    showSuccessToast("加入队伍成功！")
    doJoinCancel()
  }else {
    showFailToast("加入队伍失败"+(res.description!=''?","+res.description:''))
    doJoinCancel()
  }
}
const matchStatus = (id:number,status:number)=>{
  {
    clickTeamId.value = id
    if(status===0){
      enterTeam()
    }else{
      showPasswordDialog.value = true
    }
  }
}
const doJoinCancel = ()=>{
  clickTeamId.value = 0
      password.value=''
}
/**
 * 解散队伍
 * @param id
 */
const doQuitTeam = async (id:number)=>{
  const res = await myAxios.post("/team/quit",{
    teamId:id,
  })
  if (res?.code===0){
    showSuccessToast("退出队伍成功！")
  }else {
    showFailToast("退出队伍失败"+(res.description!=''?","+res.description:''))
  }
}
/**
 * 退出队伍
 * @param id
 */
const doDeleteTeam = async (id:number)=>{
  const res = await myAxios.post("/team/delete",{
    id
  })
  if (res?.code===0){
    showSuccessToast("删除队伍成功！")
  }else {
    showFailToast("删除队伍失败"+(res.description!=''?","+res.description:''))
  }
}
</script>



<style scoped>
  #card{
    height: 100%;
  }
</style>