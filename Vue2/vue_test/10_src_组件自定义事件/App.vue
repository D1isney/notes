<template>
  <div>
    <h1>{{ msg }}，学生姓名：{{ StudentName }}</h1>
    <!--    通过父组件给子组件传递函数类型的props事项：子给父传递数据-->
    <School :getSchoolName="getSchoolName"></School>
    <!--    通过父组件给子组件绑定一个自定义事件实现：子给父传递数据(第一种写法，使用@或者v-on)-->
    <Student v-on:disney="getStudentName" @demo="m1"></Student>
    <hr>
    <!--    通过父组件给子组件绑定一个自定义事件实现：子给父传递数据(第一种写法，使用ref)-->
    <!--    @click被当成自定义事件-->
    <!--    @click.native 让vue识别@click识别这是一个原生的@click事件-->
    <Student ref="student" @click.native="show"></Student>
  </div>
</template>
<script>
import Student from "./components/Student.vue";
import School from "./components/School.vue";

export default {
  name: 'App',
  components: {School, Student},
  data() {
    return {
      msg: 'Hello Vue',
      StudentName: '',
    }
  },
  methods: {
    getSchoolName(name) {
      console.log("APP收到了学校名：" + name)
      this.StudentName = name;
    },
    getStudentName(name) {
      console.log("APP收到了学生名：" + name)
    },
    m1() {
      console.log("demo事件被触发了")
    },
    show() {
      alert(123)
    }
  },
  mounted() {
    // 只触发一次
    // this.$refs.student.$once('disney', this.getStudentName);
    // 绑定自定义事件
    // this.$refs.student.$on('disney', this.getStudentName);

    // this.$refs.student.$once('disney', function (){
    //   console.log(this) //  vc
    // });
    // 使用箭头函数拿到的就是vm
    this.$refs.student.$on('disney', (name) => {
      console.log(this) //  vm
      this.StudentName = name
    });
  }
}
</script>
<style>

</style>