<template>
  <div class="navbar">
    <hamburger :is-active="sidebar.opened" class="hamburger-container" @toggleClick="toggleSideBar"/>
    <breadcrumb class="breadcrumb-container"/>
    <div class="right-menu">
      <el-button icon="el-icon-s-tools" class="system-button" @click="systemDrawer = true">系统配置</el-button>
    </div>

    <el-drawer
      title="系统配置"
      :visible.sync="systemDrawer"
      :with-header="true"
    >
      <el-row :gutter="10">
        <el-col :span="23" :push="1" class="system-drawer-el-col">
          <el-tag class="system-drawer-tag">首页：</el-tag>
          <router-link to="/">
            <el-button type="primary" @click="systemDrawer = false">
              Home
            </el-button>
          </router-link>
        </el-col>
        <el-col :span="23" :push="1" class="system-drawer-el-col">
          <el-tag class="system-drawer-tag">主题：</el-tag>
          <el-button type="primary" @click="openColor">Color</el-button>
        </el-col>
        <el-col :span="23" :push="1" class="system-drawer-el-col">
          <el-tag class="system-drawer-tag">设置：</el-tag>
          <el-button type="primary" @click="openSetting">Setting</el-button>
        </el-col>
        <el-col :span="23" :push="1" class="system-drawer-el-col">
          <el-tag :type="getPlcStatus()" class="system-drawer-tag">PLC连接状态：</el-tag>
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
              <el-input placeholder="127.0.0.1" v-model="settingList.ip"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="13">
            <el-form-item label="端口：">
              <el-input placeholder="502" v-model="settingList.port"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="13">
            <el-form-item label="slaveId：">
              <el-input placeholder="1" v-model="settingList.slaveId"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item>
          <el-button type="primary" @click="saveOrUpdateSetting">修改</el-button>
          <el-button @click="setting = false">取消</el-button>
        </el-form-item>

      </el-form>
    </el-drawer>

    <el-drawer
      title="配置主题"
      :visible.sync="color"
      :with-header="true"
    >

    </el-drawer>

  </div>
</template>

<script>
import Vuex from 'vuex'
import { mapGetters } from 'vuex'
import Breadcrumb from '@/components/Breadcrumb'
import Hamburger from '@/components/Hamburger'
import fa from 'element-ui/src/locale/lang/fa'
import { closePlcConnect, getSettingAPI, openPlcConnect, saveOrUpdateSettingAPI } from '@/api/system/systemAPI'

export default {
  components: {
    Breadcrumb,
    Hamburger
  },
  computed: {
    ...mapGetters([
      'sidebar',
      'avatar'
    ])
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
      plcStatus: 500
    }
  },
  methods: {
    toggleSideBar() {
      this.$store.dispatch('app/toggleSideBar')
    },
    async logout() {
      await this.$store.dispatch('user/logout')
      this.$router.push(`/login?redirect=${this.$route.fullPath}`)
    },
    openSetting() {
      this.systemDrawer = false
      this.setting = true
      this.getSettingList()
    },
    openColor(){
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
        this.setting = false;
        this.getPlcStatus()
        this.closePlc();
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
        }else{
          this.$store.dispatch('settings/changePLCConnect', 200)
          this.$message.danger(res.message)
        }
      })
    }
  },
  created() {
    this.getSettingList()
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
</style>
