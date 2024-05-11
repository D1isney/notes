//该文件专门用于创建整个应用的路由器

import VueRouter from "vue-router";
import About from "../pages/About";
import Home from '../pages/Home';
import News from "../pages/News";
import Message from "../pages/Message";
import Detail from "../pages/Detail.vue";

//创建并默认暴露一个路由器
export default new VueRouter({
    routes: [
        {
            path: '/about',
            component: About
        },
        {
            path: '/home',
            component: Home,
            children: [
                {
                    path: 'news',
                    component: News
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
                            props($route){
                                return {id:$route.query.id,title:$route.query.title}
                            }
                        }
                    ]
                }
            ]
        }
    ]
});