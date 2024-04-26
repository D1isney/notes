//  引入vue
import Vue from 'vue'
//  引入app
import App from './App.vue'
//  关闭Vue的生产提示
Vue.config.productionTip = false
Vue.config.devtools = true;
//  创建vm
new Vue({
    el:'#app',
    render: h => h(App)
})