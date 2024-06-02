module.exports = {
    pages: {
        index: {
            entry: '21_src_Suspense/main.js',
            template: 'public/index.html',
            filename: 'index.html',
            title: 'Index Page',
            chunks: ['chunk-vendors', 'chunk-common', 'index']
        },
        subpage: '21_src_Suspense/main.js'
    },
    lintOnSave: false /*关闭语法检查*/
    /*
    3.4 ships a number of improvements to hydration mismatch error messages:
      Improved clarity of the wording (rendered by server vs. expected on client).
      The message now includes the DOM node in question so you can quickly locate it on the page or in the elements panel.
      Hydration mismatch checks now also apply to class, style, and other dynamically bound attributes.
     */
    , chainWebpack: (config) => {
        config.plugin('define').tap((definitions) => {
            Object.assign(definitions[0], {
                __VUE_OPTIONS_API__: 'true',
                __VUE_PROD_DEVTOOLS__: 'false',
                __VUE_PROD_HYDRATION_MISMATCH_DETAILS__: 'false'
            })
            return definitions
        })
    }
}