module.exports = {
  pages: {
    index: {
      entry: '32_src_路由的query参数/main.js',
      template: 'public/index.html',
      filename: 'index.html',
      title: 'Index Page',
      chunks: ['chunk-vendors', 'chunk-common', 'index']
    },
    subpage: '32_src_路由的query参数/main.js'
  },

  // 开启代理服务器
  // 解决跨域的问题

  // 方式1
  // 后端8090、不能配置多个代理
  // devServer: {
  //   proxy: 'http://localhost:8090'
  // },

  // 方式2
  // 可以多个代理服务器
  devServer: {
    proxy: {
      // 请求前缀
      '/api': {
        target: 'http://localhost:8090',
        // 配置/api开头的 意思就是去掉这个api
        pathRewrite:{'^/api':''},
        // 用于支持webSocket、默认true
        ws: true,
        //  骗过服务器，冒充自己是8090、默认true
        // 用于控制请求头的host值
        changeOrigin: true
      },
      // 简写
      // '/foo': {
      //   target: '<other_url>'
      // }
      // 请求前缀
      '/demo': {
        target: 'http://localhost:8090',
        // 配置/api开头的 意思就是去掉这个api
        pathRewrite:{'^/demo':''},
        // 用于支持webSocket、默认true
        ws: true,
        //  骗过服务器，冒充自己是8090、默认true
        // 用于控制请求头的host值
        changeOrigin: true
      },
    }
  },

  lintOnSave:false /*关闭语法检查*/
}