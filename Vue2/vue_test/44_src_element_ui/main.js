//引入Vue
import Vue from "vue";
//引入App
import App from './App';
// 完整引入
// import ElementUI from 'element-ui';
// import 'element-ui/lib/theme-chalk/index.css';
// 按需引入
import { Button, Select ,Row,DatePicker} from 'element-ui';
Vue.component(Button.name, Button);
Vue.component(Select.name, Select);
Vue.component(Row.name, Row);
Vue.component(DatePicker.name, DatePicker);

/* 或写为
 * Vue.use(Button)
 * Vue.use(Select)
 */

//引入vue-router
import VueRouter from "vue-router";
import router from './router';



//关闭Vue的生产提示
Vue.config.productionTip = false;

//应用vue-router插件
Vue.use(VueRouter);
// 注册所有的组件
// Vue.use(ElementUI);
new Vue({
    el: '#app',
    render: h => h(App),
    router
});