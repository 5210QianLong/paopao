import { createApp } from 'vue'
// import './style.css'
// 可能会报错找不到vue,新版的 vite 没有进行 env 配置
import App from './App.vue'
// import { Button ,NavBar,ConfigProvider,Tabbar, TabbarItem} from 'vant';
import * as VueRouter from 'vue-router';
import routes from "./config/router.ts";
import Vant from 'vant'
import 'vant/lib/index.css';


const app = createApp(App);

// app.use(Button);
// app.use(NavBar)
// app.use(ConfigProvider)
// app.use(Tabbar);
// app.use(TabbarItem);

const router = VueRouter.createRouter({
    //内部提供了 history 模式的实现。为了简单起见，我们在这里使用 hash 模式。
    history: VueRouter.createWebHashHistory(),
    routes, // `routes: routes` 的缩写
})
app.use(Vant)
app.use(router)
app.mount('#app')

//const route = useRoute() 查看当前路由消息  可以 route.query
//const router = useRouter() 实现路由跳转 可以 router.push()实现编程跳转





