import defaultSettings from '@/settings'

const { showSettings, fixedHeader, sidebarLogo } = defaultSettings

const state = {
  showSettings: showSettings,
  fixedHeader: fixedHeader,
  sidebarLogo: sidebarLogo,
  plcConnect: false
}

const mutations = {
  CHANGE_SETTING: (state, { key, value }) => {
    // eslint-disable-next-line no-prototype-builtins
    if (state.hasOwnProperty(key)) {
      state[key] = value
    }
  },
  CHANGE_PLC_CONNECT: (state, data) => {
    state.plcConnect = data === 200
  }
}

const actions = {
  changeSetting({ commit }, data) {
    commit('CHANGE_SETTING', data)
  },
  changePLCConnect({ commit }, data) {
    commit('CHANGE_PLC_CONNECT', data)
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

