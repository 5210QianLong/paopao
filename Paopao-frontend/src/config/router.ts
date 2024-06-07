import team from '../pages/team.vue';
import index from "../pages/index.vue";
import userUpdate from "../pages/userUpdate.vue";
import Search from "../pages/Search.vue";
import editPage from "../pages/editPage.vue";
import searchResultPage from "../pages/searchResultPage.vue";
import userLogin from "../pages/userLogin.vue";
import createTeamPage from "../pages/createTeamPage.vue";
import passwordEditPage from "../pages/passwordEditPage.vue";
import user from "../pages/user.vue";
import userJoinTeam from "../pages/userJoinTeam.vue";
import userCreateTeam from "../pages/userCreateTeam.vue";
//  定义一些路由
const routes = [
    { path: '/', component: index },
    { path: '/team',title:'找队伍', component: team },
    { path: '/team/teamEdit',title:'新建队伍', component: createTeamPage },
    { path: '/team/teamPasswordEdit', component: passwordEditPage },
    { path: '/user/update',title:'更新信息', component: userUpdate },
    { path: '/user',title:'我的信息', component: user },
    { path: '/user/join',title:'已加入队伍', component: userJoinTeam },
    { path: '/user/create',title:'已创建队伍', component: userCreateTeam },
    { path: '/search',title:'搜索', component: Search },
    { path: '/user/edit',title:'编辑', component: editPage },
    { path: '/search/searchResult',title:'搜索结果', component: searchResultPage },
    { path: '/login',title:'登 录', component: userLogin },

]
export default routes