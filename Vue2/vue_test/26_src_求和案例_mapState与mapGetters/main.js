// 引入Vue
import Vue from 'vue'
import App from './App.vue'

// 引入插件
import VueResource from "vue-resource";
import vueResourceEsm from "vue-resource";
// 引入store vuex
// import store from "./store/index";
import store from "./store";

// 引入Vuex
import Vuex from 'vuex'

Vue.config.productionTip = false
Vue.config.devtools = true;

Vue.use(vueResourceEsm)

// 创建Vue实例vm
const vm = new Vue({
    //  将App组件放入容器
    render: h => h(App),
    // render(createElement, hack) {
    //     return createElement('h1','你好呀')
    // }
    store,
    beforeCreate() {
        // 全局总线
        Vue.prototype.$bus = this
    }
}).$mount('#app')
