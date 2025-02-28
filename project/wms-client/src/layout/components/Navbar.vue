<template>
  <div class="navbar">
    <hamburger :is-active="sidebar.opened" class="hamburger-container" @toggleClick="toggleSideBar"/>
    <breadcrumb class="breadcrumb-container"/>
    <div class="right-menu">
      <el-tag :type="getPlcStatus()" class="system-drawer-tag">PLC连接状态</el-tag>
      <el-button icon="el-icon-user-solid" class="system-button" @click="openMySelf">{{ name }}</el-button>
      <el-button icon="el-icon-s-tools" class="system-button" @click="systemDrawer = true">系统配置</el-button>
      <el-button icon="el-icon-caret-right" class="system-button" @click="logout()">登出</el-button>
    </div>

    <el-drawer
      title="系统配置"
      :visible.sync="systemDrawer"
      :with-header="true"
    >
      <el-row :gutter="20">
        <el-col :span="6" :push="2" class="system-drawer-el-col">
          <el-tag class="system-drawer-tag">用户：</el-tag>
        </el-col>
        <el-col :span="10">
          <el-button type="info">
            {{ name }}
          </el-button>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="6" :push="2" class="system-drawer-el-col">
          <el-tag class="system-drawer-tag">首页：</el-tag>
        </el-col>
        <el-col :span="10">
          <router-link to="/">
            <el-button type="primary" @click="systemDrawer = false">
              Home
            </el-button>
          </router-link>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="6" :push="2" class="system-drawer-el-col">
          <el-tag class="system-drawer-tag">设置：</el-tag>
        </el-col>
        <el-col :span="10">
          <el-button type="primary" @click="openSetting">Setting</el-button>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="6" :push="1" class="system-drawer-el-col">
          <el-tag :type="getPlcStatus()" class="system-drawer-tag">PLC连接状态：</el-tag>
        </el-col>
        <el-col :span="10">
          <el-button type="primary" @click="openPlc">开启连接</el-button>
          <el-button type="primary" @click="closePlc">关闭连接</el-button>
        </el-col>
      </el-row>
    </el-drawer>

    <el-drawer
      title="PLC参数设置"
      :visible.sync="setting"
      :with-header="true"
    >
      <el-form label-position="right" label-width="80px" :model="settingList">
        <el-row :gutter="10">
          <el-col :span="13">
            <el-form-item label="IP：">
              <el-input v-model="settingList.ip" placeholder="127.0.0.1"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="13">
            <el-form-item label="端口：">
              <el-input v-model="settingList.port" placeholder="502"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="13">
            <el-form-item label="slaveId：">
              <el-input v-model="settingList.slaveId" placeholder="1"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item>
          <el-button type="primary" @click="saveOrUpdateSetting">修改</el-button>
          <el-button @click="setting = false">取消</el-button>
        </el-form-item>
      </el-form>
    </el-drawer>

    <el-dialog
      :visible.sync="dialogVisibleUpdateMember"
      width="80%"
      title="用户信息"
    >
      <el-descriptions title="基本信息" :column="3">
        <template slot="extra">
          <el-button type="warning" size="small" @click="openOperatingButton">更改密码</el-button>
        </template>
        <el-descriptions-item label="账号">
          <el-tag size="small" type="primary">
            {{ updateMember.username }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="密码">
          <el-tag size="small" type="info">
            {{ updateMember.password }}
          </el-tag>
        </el-descriptions-item>

        <el-descriptions-item label="姓名">
          <el-tag size="small" type="info">
            {{ updateMember.name }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="性别">
          <el-tag size="small">
            {{ sexOptions[updateMember.sex].label }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="邮箱地址">
          <el-tag size="small" type="info">
            {{ updateMember.email }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="电话号码">
          <el-tag size="small" type="info">
            {{ updateMember.phone }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="出生日期">
          <el-tag size="small" type="info">
            {{ updateMember.birthday }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">
          <el-tag size="small" type="info">
            {{ updateMember.createTime }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="更新时间">
          <el-tag size="small" type="info">
            {{ updateMember.updateTime }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="登录状态">
          <el-tag
            :type="onlineTag(1)"
            effect="dark"
            size="small"
          >
            {{ onlineOptions[1].label }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="账号状态">
          <el-tag size="small"
                  :type="statusTag(updateMember.status)"
          >
            {{ (updateMember.status || updateMember.status === 0) && statusOptions[updateMember.status].label }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="加密盐">
          <el-tag size="small" type="danger">
            {{ updateMember.salt }}
          </el-tag>
        </el-descriptions-item>
      </el-descriptions>

      <el-progress :percentage="percentageStatus" v-show="percentageShow" :color="customColorMethod"></el-progress>
      <div class="passwordBox" v-show="operatingButton">
        <div class="operatingBox">
          <el-form ref="updateMemberPasswordForm" :model="updateMemberPasswordForm" label-width="80px">
            <el-row :gutter="20">
              <el-col :span="24">
                <el-form-item label="账号">
                  <el-input v-model="updateMemberPasswordForm.username" disabled></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="24">
                <el-form-item label="旧密码">
                  <el-input v-model="updateMemberPasswordForm.oldPassword" type="password" show-password clearable :disabled="enterOldOrNewPassword"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="24">
                <el-form-item label="新密码">
                  <el-input v-model="updateMemberPasswordForm.newPassword" type="password" show-password clearable :disabled="!enterOldOrNewPassword"
                  ></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
          <el-steps :active="active" finish-status="success">
            <el-step title="验证密码"></el-step>
            <el-step title="新密码"></el-step>
            <el-step title="修改完成"></el-step>
          </el-steps>
          <div style="display: flex;justify-content: flex-end">
            <el-popover
              placement="bottom"
              title="警告"
              width="200"
              trigger="manual"
              content="密码不能为空且密码长度必须大于等于6！"
              v-model="commitVisible"
            >
              <el-button @click="next" slot="reference">{{ verificationPasswordTitle }}</el-button>
            </el-popover>
            <el-button @click="operatingButton= false">取消修改</el-button>
            <el-button type="primary" @click="dialogVisibleUpdateMember = false">取 消</el-button>
          </div>
        </div>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { mapGetters, mapState, mapActions } from 'vuex'
import Breadcrumb from '@/components/Breadcrumb'
import Hamburger from '@/components/Hamburger'
import {
  closePlcConnect,
  executePendingTasks,
  getSettingAPI,
  openPlcConnect,
  saveOrUpdateSettingAPI
} from '@/api/system/systemAPI'
import { Message } from 'element-ui'
import { MemberConst, verificationPassword, confirmTheChange } from '@/api/member/member'

export default {
  components: {
    Breadcrumb,
    Hamburger
  },
  computed: {
    ...mapGetters(['sidebar', 'name']),
    ...mapState('webSocket', ['socketData']),
    ...mapState('user', ['member']),
    statusOptions: () => Object.keys(MemberConst.status).map(key => MemberConst.status[key]),
    onlineOptions: () => Object.keys(MemberConst.online).map(key => MemberConst.online[key]),
    sexOptions: () => Object.keys(MemberConst.sex).map(key => MemberConst.sex[key])
  },
  created() {
    this.getSettingList()
    this.openSocket()
  },
  data() {
    return {
      systemDrawer: false,
      setting: false,
      color: false,
      settingList: {
        ip: '',
        port: '',
        slaveId: ''
      },
      plcStatus: 500,
      notifications: [], // 存储所有通知的ID
      maxNotifications: 2, // 最大通知数量限制
      retransmission: {
        flag: false,
        type: 0
      },
      updateMember: {
        sex: 0,
        online: 1,
        status: 1
      },
      dialogVisibleUpdateMember: false,
      operatingButton: false,
      active: 0,
      updateMemberPasswordForm: {
        oldPassword: '',
        newPassword: '',
        username: ''
      },
      enterOldOrNewPassword: false,
      percentageStatus: 0,
      percentageShow: false,
      verificationPasswordTitle: '验证密码',
      commitVisible: false,

      statusNum: 5,
      hasEmpty: false,
      hasNotEmpty: false
    }
  },
  methods: {
    customColorMethod(percentageStatus) {
      if (percentageStatus < 30) {
        return '#909399'
      } else if (percentageStatus < 70) {
        return '#e6a23c'
      } else {
        return '#67c23a'
      }
    },
    openOperatingButton() {
      this.percentageStatus = 0
      this.operatingButton = false
      this.percentageShow = true
      this.verificationPasswordTitle = '验证密码'
      this.active = 0
      this.enterOldOrNewPassword = false
      this.updateMemberPasswordForm = {
        username: this.updateMember.username,
        oldPassword: '',
        newPassword: ''
      }
      let time
      time = setInterval(() => {
        //  达到100时清空定时器
        if (this.percentageStatus >= 100) {
          clearInterval(time)
          //  调用展开盒子
          setTimeout(() => {
            this.openOperatingBox()
            this.percentageShow = false
          }, 500)
        } else if (this.percentageStatus < 100) {
          this.percentageStatus += this.statusNum
          if (this.percentageStatus >= 100) this.percentageStatus === 100
        }
      }, 100)
    },
    openOperatingBox() {
      this.operatingButton = true
    },
    next() {
      this.commitVisible = false
      //  验证密码
      if (this.active === 0) {
        if (this.updateMemberPasswordForm.oldPassword === '') {
          this.$message.error('请输入旧密码验证')
          return
        }
        //  校验
        this.commitVerificationPassword()
        this.updateMemberPasswordForm.newPassword = ''
      } else if (this.active === 1) {
        //  修改
        if (this.updateMemberPasswordForm.newPassword === '' || this.updateMemberPasswordForm.newPassword.length < 6) {
          this.commitVisible = true
          return
        }
      } else if (this.active === 2) {
        //  修改
        if (this.updateMemberPasswordForm.newPassword === '' || this.updateMemberPasswordForm.newPassword.length < 6) {
          this.commitVisible = true
          return
        }
        this.commitConfirmTheChange()
      }
    },
    async commitVerificationPassword() {
      await verificationPassword(this.updateMemberPasswordForm).then(res => {
      })
    },
    async commitConfirmTheChange() {
      await confirmTheChange(this.updateMemberPasswordForm).then(res => {
      })
    },

    statusTag(status) {
      if (status === 1) {
        return 'success'
      } else if (status === 0) {
        return 'danger'
      } else {
        return 'warning'
      }
    },
    onlineTag(online) {
      if (online > 0) {
        return 'success'
      } else {
        return 'info'
      }
    },
    openMySelf() {
      this.updateMember = this.member
      this.dialogVisibleUpdateMember = true
      this.operatingButton = false
      this.updateMemberPasswordForm.username = this.updateMember.username
      this.active = 0
    },
    ...mapActions('webSocket', ['openSocket']),
    toggleSideBar() {
      this.$store.dispatch('app/toggleSideBar')
    },
    //  登出账号
    async logout() {
      this.$confirm('此操作会将此账号登出, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$store.dispatch('user/logout')
        this.$message.success('即将登录账号！')
        setTimeout(() => {
          this.$router.push(`/login?redirect=${this.$route.fullPath}`)
        }, 2000)
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消登出'
        })
      })

    },
    openSetting() {
      this.systemDrawer = false
      this.setting = true
      this.getSettingList()
    },
    openColor() {
      this.systemDrawer = false
      this.color = true
    },
    async getSettingList() {
      await getSettingAPI().then(res => {
        this.settingList = res.data
      })
    },
    async saveOrUpdateSetting() {
      saveOrUpdateSettingAPI(this.settingList).then(res => {
        this.getSettingList()
        this.setting = false
        this.getPlcStatus()
        // this.closePlc();
      })
    },
    getPlcStatus() {
      if (this.$store.state.settings.plcConnect === true) {
        return 'success'
      } else {
        return 'danger'
      }
    },
    openPlc() {
      openPlcConnect().then(res => {
        this.$store.dispatch('settings/changePLCConnect', res.code)
        this.$message.success(res.message)
      })
    },
    closePlc() {
      closePlcConnect().then(res => {
        if (res.code === 200) {
          this.$store.dispatch('settings/changePLCConnect', 500)
          this.$message.success(res.message)
        } else {
          this.$store.dispatch('settings/changePLCConnect', 200)
          this.$message.danger(res.message)
        }
      })
    },
    async getExecutePendingTasks(query) {
      await executePendingTasks(query).then(res => {
          console.log(res)
        }
      )
    }

  },
  watch: {
    socketData(val) {
      if (val.type === 'PlcConnectError') {
        this.$notify({
          title: '失败',
          message: val.message,
          type: 'error'
        })
      }
      if (val.type === 'TaskMessageIssued') {
        if (this.notifications.length >= this.maxNotifications) {
          Notification.close(this.notifications[0])
          this.notifications.shift()
        }
        const id = this.$notify({
          title: val.message,
          message: '任务名称：' + val.data.name + '，任务编码：' + val.data.code,
          type: 'success',
          onClose: () => {
            this.notifications = this.notifications.filter(n => n !== id)
          }
        })
        this.notifications.push(id)
      }

      if (val.type === 'TaskMessageSuccess') {
        if (this.notifications.length >= this.maxNotifications) {
          Notification.close(this.notifications[0])
          this.notifications.shift()
        }
        const id = Notification({
          title: val.message,
          message: '任务名称：' + val.data.name + '，任务编码：' + val.data.code,
          type: 'success',
          onClose: () => {
            this.notifications = this.notifications.filter(n => n !== id)
          }
        })
        this.notifications.push(id)
      }

      if (val.type === 'continueWarehousingTask') {
        this.$confirm(val.message, '提示', {
          confirmButtonText: '下发',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.retransmission = {
            flag: true,
            type: val.data
          }
          this.getExecutePendingTasks(this.retransmission)
        }).catch(() => {
          this.retransmission = {
            flag: false,
            type: val.data
          }
          this.getExecutePendingTasks(this.retransmission)
        })
      }
      if (val.type === 'continueIssueTask') {
        this.$confirm(val.message, '提示', {
          confirmButtonText: '下发',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.retransmission = {
            flag: true,
            type: val.data
          }
          this.getExecutePendingTasks(this.retransmission)
        }).catch(() => {
          this.retransmission = {
            flag: false,
            type: val.data
          }
          this.getExecutePendingTasks(this.retransmission)
        })
      }

      if (val.type === 'passwordVerificationSuccessful') {
        this.active += 1
        this.verificationPasswordTitle = val.message
        this.enterOldOrNewPassword = true
        this.$message.success('验证成功，请输入新密码！')
      }
      if (val.type === 'passwordVerificationFailed') {
        this.$message.error(val.message)
      }
      if (val.type === 'passwordSaveSuccessful') {
        this.verificationPasswordTitle = val.message
        this.active += 1
        this.$store.dispatch('user/logout')
        this.$message.success('修改成功，即将跳转登录页面！')
        setTimeout(() => {
          this.$router.push(`/login?redirect=${this.$route.fullPath}`)
        }, 2000)
      }
      if (val.type === 'passwordSaveFailed') {
        this.$message.error(val.message)
      }
    },
    'updateMemberPasswordForm.newPassword'(newVal) {
      if (newVal === '' && !this.hasEmpty) {
        this.hasEmpty = true
        this.hasNotEmpty = false
        this.active -= 1
      } else if (newVal !== '' && !this.hasNotEmpty) {
        this.hasNotEmpty = true
        this.hasEmpty = false
        this.active += 1
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, .08);

  .hamburger-container {
    line-height: 46px;
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background .3s;
    -webkit-tap-highlight-color: transparent;

    &:hover {
      background: rgba(0, 0, 0, .025)
    }
  }

  .breadcrumb-container {
    float: left;
  }

  .right-menu {
    float: right;
    height: 100%;
    line-height: 50px;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 18px;
      color: #5a5e66;
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background .3s;

        &:hover {
          background: rgba(0, 0, 0, .025)
        }
      }
    }

    .avatar-container {
      margin-right: 30px;

      .avatar-wrapper {
        margin-top: 5px;
        position: relative;

        .user-avatar {
          cursor: pointer;
          width: 40px;
          height: 40px;
          border-radius: 10px;
        }

        .el-icon-caret-bottom {
          cursor: pointer;
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
        }
      }
    }
  }

  .menu-buttons {
    border: 0;

  }

  .system-button {
    border: 0;
    background-color: transparent;
  }

  .system-drawer-el-col {
    margin-bottom: 20px;
  }

  .system-drawer-tag {
    margin-right: 50px;
  }
}

.passwordBox {
  width: 100%;
  display: flex;
  flex-direction: column;
  //align-items: center;
  justify-content: center;
  align-items: center;
  margin-top: 3%;
}

.operatingBox {
  width: 50%;
  //display: flex;
}
</style>
