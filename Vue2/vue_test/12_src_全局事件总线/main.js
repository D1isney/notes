// 引入Vue
import Vue from 'vue'
import App from './App.vue'

Vue.config.productionTip = false
Vue.config.devtools = true;

// window.x = {a: 1, b: 2}
// 写在vc上，但是每一个新的组件就是一个新的vc
// 放在vm的原型对象上，vm能拿 vc也能拿
// Vue.prototype.x = {a: 1, b: 2}

// console.log(Vue.prototype)

// const demo = Vue.extend({})
// // vc
// const d = new demo();
// Vue.prototype.x = d

// 创建Vue实例vm
new Vue({
    //  将App组件放入容器
    render: h => h(App),
    // render(createElement, hack) {
    //     return createElement('h1','你好呀')
    // }

    //  安装全局事件总线
    beforeCreate() {
        Vue.prototype.$bus = this
    }
}).$mount('#app')
