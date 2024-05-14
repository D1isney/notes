//该文件专门用于创建整个应用的路由器

import VueRouter from "vue-router";
import About from "../pages/About";
import Home from '../pages/Home';
import News from "../pages/News";
import Message from "../pages/Message";
import Detail from "../pages/Detail.vue";

//创建并默认暴露一个路由器
const router = new VueRouter({
    routes: [
        {
            path: '/about',
            component: About,
            meta: {
                title: 'about',
            }
        },
        {
            path: '/home',
            component: Home,
            meta: {
                title: 'home',
            },
            children: [
                {
                    path: 'news',
                    component: News,
                    meta: {
                        isAuth: true,
                        title: 'news',
                    }
                },
                {
                    path: 'message',
                    component: Message,
                    children: [
                        {
                            name: 'detail',
                            // path: 'detail/:id/:title',
                            path: 'detail',
                            component: Detail,
                            // props的第一种写法、传给Detail组件
                            // props: {
                            //     a: 1,
                            //     b: 'hello'
                            // }

                            //     props的第二种写法，值为布尔值，若布尔值微针，就会把该组件收到的所有的params参数，以props的形式传给Detail组件
                            // props: true

                            //     props的第三种写法，
                            props($route) {
                                return {id: $route.query.id, title: $route.query.title}
                            }
                        }
                    ],
                    meta: {
                        title: 'message',
                        isAuth: true
                    }
                }
            ]
        }
    ]
});

// 全局前置路由守卫 -- 初始化的时候被调用、每次路由切换之前被调用
router.beforeEach((to, from, next) => {
    // console.log(to,from)
    //     title名
    //     document.title = to.meta.title
    // document.title = to.meta["title"]

//     next 放行
//     判断是否需要鉴权
    if (to.meta["isAuth"]) {
        if (localStorage.getItem("name") === 'Disney') {
            // document.title = to.meta["title"]
            next()
        } else {
            alert('权限不对')
        }
    } else {
        // document.title = to.meta["title"]
        next()
    }
})

// 全局后置路由守卫
router.afterEach((to, from) => {
    document.title = to.meta["title"]
    console.log("后置路由：", to, from)
})
export default router