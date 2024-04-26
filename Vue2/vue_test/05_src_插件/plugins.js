export default {
    install(Vue) {
        //     全局过滤器
        Vue.filter('mySlice', function (val) {
            return val.slice(0, 4)
        })

        //     定义全局指令
        Vue.directive('fbind', {
            bind(element, binding) {
                element.value = binding.value
            },
            inserted(element, binding) {
                element.focus()
            },
            update(element, binding) {
                element.value = binding.value
            }
        })

        //     定义混入
        Vue.mixin({
            data() {
                return {
                    x: 100,
                    y: 200
                }
            }
        })

        //     给Vue原型上添加一个方法（vm与vc都能用）
        Vue.prototype.hello = ()=>{alert("你好呀！")}
    }
}