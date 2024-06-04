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
    { path: '/team', component: team },
    { path: '/team/teamEdit', component: createTeamPage },
    { path: '/team/teamPasswordEdit', component: passwordEditPage },
    { path: '/user/update', component: userUpdate },
    { path: '/user', component: user },
    { path: '/user/join', component: userJoinTeam },
    { path: '/user/create', component: userCreateTeam },
    { path: '/search', component: Search },
    { path: '/user/edit', component: editPage },
    { path: '/search/searchResult', component: searchResultPage },
    { path: '/login', component: userLogin },

]
export default routes