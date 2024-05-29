import {onBeforeUnmount, onMounted, reactive} from "vue";

export default function () {
    let point = reactive({
        x: 0,
        y: 0
    })

    function savePoint(event) {
        point.x = event.x
        point.y = event.y
    }

    //  周期钩子
    onMounted(() => {
        //  组件不在还会存在这个监听时间
        window.addEventListener("click", (event) => {
            savePoint(event)
        })
    })
    onBeforeUnmount(() => {
        window.removeEventListener('click', savePoint)
    })
    return point
}
