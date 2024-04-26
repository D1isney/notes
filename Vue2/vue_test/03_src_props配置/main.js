// 引入Vue
import Vue from 'vue'
// 引入App组件。它是所有组件的父组件
import App from './App.vue'

Vue.config.productionTip = false
Vue.config.devtools = true;

// 创建Vue实例vm
new Vue({
    //  将App组件放入容器
    render: h => h(App),
    // render(createElement, hack) {
    //     return createElement('h1','你好呀')
    // }
}).$mount('#app')
