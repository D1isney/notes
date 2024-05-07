//  创建Store,Vuex

// 引入Vuex
import Vuex from 'vuex'
// 引入vue，如果卸载main.js，就会先扫描了所有的import才去vue.use，就会报错
import Vue from "vue";
// 准备action，用于相应组件中的动作
const actions = {}
// 准备mutations 用于操作数据
const mutations = {}

//  准备state 用于存储数据
const state = {}

Vue.use(Vuex)
// 创建store
const store = new Vuex.Store({
    actions: actions,
    mutations: mutations,
    // 简写
    state
})
export default state