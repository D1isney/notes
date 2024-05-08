//  创建Store,Vuex

// 引入Vuex
import Vuex from 'vuex'
// 引入vue，如果卸载main.js，就会先扫描了所有的import才去vue.use，就会报错
import Vue from "vue";
// 准备action，用于相应组件中的动作
const actions = {
    jia: function (context, val) {
        // console.log("执行jia")
        // console.log(context)
        // console.log(val)
        context.commit('JIA', val)
    },
    // 没有业务逻辑可以跳过、      this.$store.commit("JIAN", this.n);
    // jian(context, val) {
    //     context.commit('JIAN', val)
    // },
    jiaOdd(context, val) {
        if (context.state.sum % 2) {
            context.commit('JIA', val)
        }
    },
    jiaWait(context, val) {
        setTimeout(() => {
            context.commit('JIA', val)
        }, 500)
    }
}
// 准备mutations 用于操作数据
const mutations = {
    JIA(state, val) {
        // console.log("mutations被调用",state,val)
        state.sum += val
    },
    JIAN(state, val) {
        state.sum -= val
    },

    ADD_PERSON(state, val) {
        state.personList.unshift(val)
    }
}

//  准备state 用于存储数据
const state = {
    sum: 0, //  当前的和
    school: 'Vue',
    personList: [
        {id: '001', name: '张三'}
    ],
}

// 准备getters 用于将state中的数据进行加工
const getters = {
    bigSum(state) {
        return state.sum * 10;
    }
}

Vue.use(Vuex)
// 创建store
const store = new Vuex.Store({
    actions: actions,
    mutations: mutations,
    // 简写
    state,
    getters
})
export default store