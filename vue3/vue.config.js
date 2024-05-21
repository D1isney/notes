module.exports = {
  pages: {
    index: {
      entry: '05_src_Vue3的响应式原理/main.js',
      template: 'public/index.html',
      filename: 'index.html',
      title: 'Index Page',
      chunks: ['chunk-vendors', 'chunk-common', 'index']
    },
    subpage: '05_src_Vue3的响应式原理/main.js'
  },
  lintOnSave:false /*关闭语法检查*/
}