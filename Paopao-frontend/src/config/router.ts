import team from '../pages/team.vue';
import index from "../pages/index.vue";
import user from "../pages/user.vue";
import Search from "../pages/Search.vue";
import editPage from "../pages/editPage.vue";
import searchResultPage from "../pages/searchResultPage.vue";
import userLogin from "../pages/userLogin.vue";
import createTeamPage from "../pages/createTeamPage.vue";
//  定义一些路由
const routes = [
    { path: '/', component: index },
    { path: '/team', component: team },
    { path: '/team/teamEdit', component: createTeamPage },
    { path: '/user', component: user },
    { path: '/search', component: Search },
    { path: '/user/edit', component: editPage },
    { path: '/search/searchResult', component: searchResultPage },
    { path: '/login', component: userLogin },

]
export default routes