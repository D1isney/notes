export const mixin = {
    methods: {
        showName() {
            alert(this.name)
        }
    },
    mounted() {
        console.log("使用mixin")
    }
}
// 相同的元素，组件的优先级更高
//  生命周期钩子函数，不看优先级，都会触发
//  混合会先触发，组件后触发
export const mixin2 = {
    data() {
        return {
            x: 100,
            y: 200,
            name: 'Demo'
        }
    }
}