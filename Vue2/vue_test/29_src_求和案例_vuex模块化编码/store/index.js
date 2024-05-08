//  创建Store,Vuex

// 引入Vuex
import Vuex from 'vuex'
// 引入vue，如果卸载main.js，就会先扫描了所有的import才去vue.use，就会报错
import Vue from "vue";


import countOptions from "./count";
import personOptions from "./person";

Vue.use(Vuex)
const store = new Vuex.Store({
    modules: {
        countAbout: countOptions,
        personAbout: personOptions
    }
})
export default store