let BASEURL
if (Object.is("development",process.env.NODE_ENV)){
  BASEURL = 'http://127.0.0.1:1217'
} else {
  BASEURL = './'
}

module.exports = {

  title: 'WMS',

  //  是否固定顶部导航栏
  fixedHeader: false,
  //  是否显示侧边导航Log
  sidebarLogo: false,

  showSettings: true,

  BASEURL
}
