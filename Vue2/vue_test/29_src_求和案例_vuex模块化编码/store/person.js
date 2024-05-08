


// 人员管理相关的配置
const personOptions = {
    // 添加明明空间、下面的store模块就不会被认识
    namespaced: true,
    actions: {
        addPersonWang(context, val) {
            if (val.name.indexOf('王') === 0) {
                context.commit("ADD_PERSON", val)
            }else{
                alert("添加人必须姓王！")
            }
        }
    },
    mutations: {
        ADD_PERSON(state, val) {
            state.personList.unshift(val)
        }
    },
    state: {
        personList: [
            {id: '001', name: '张三'}
        ],
    },
    getters: {
        firstPersonName(state) {
            return state.personList[0].name;
        }
    }
}
export default personOptions