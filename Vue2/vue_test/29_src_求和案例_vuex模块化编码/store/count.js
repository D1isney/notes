// 求和相关的配置
const countOptions = {
    // 添加明明空间
    namespaced: true,
    actions: {
        jia: function (context, val) {
            context.commit('JIA', val)
        },
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
    },
    mutations: {
        JIA(state, val) {
            state.sum += val
        },
        JIAN(state, val) {
            state.sum -= val
        },
    },
    state: {
        sum: 0, //  当前的和
        school: 'Vue',
    },
    getters: {
        bigSum(state) {
            return state.sum * 10;
        }
    }
}
export default countOptions