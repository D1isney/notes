// 引入Vue
import Vue from 'vue'
import App from './App.vue'
Vue.config.productionTip = false
Vue.config.devtools = true;
// 引用插件
import plugins from './plugins';
// 应用插件
Vue.use(plugins)

// 创建Vue实例vm
new Vue({
    //  将App组件放入容器
    render: h => h(App),
    // render(createElement, hack) {
    //     return createElement('h1','你好呀')
    // }
}).$mount('#app')
