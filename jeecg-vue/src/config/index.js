/** init domain config */
import Vue from 'vue'
// 设置全局API_BASE_URL
Vue.prototype.API_BASE_URL = window._CONFIG.VUE_APP_API_BASE_URL ? window._CONFIG.VUE_APP_API_BASE_URL : process.env.VUE_APP_API_BASE_URL
console.debug(`process.env.BASE_URL: ${process.env.BASE_URL}`)
console.debug(`Vue.prototype.API_BASE_URL: ${Vue.prototype.API_BASE_URL}`)

window._CONFIG['domianURL'] = Vue.prototype.API_BASE_URL
// 单点登录地址
window._CONFIG['casPrefixUrl'] = window._CONFIG.VUE_APP_CAS_BASE_URL ? window._CONFIG.VUE_APP_CAS_BASE_URL : process.env.VUE_APP_CAS_BASE_URL
window._CONFIG['onlinePreviewDomainURL'] = window._CONFIG.VUE_APP_ONLINE_BASE_URL ? window._CONFIG.VUE_APP_ONLINE_BASE_URL : process.env.VUE_APP_ONLINE_BASE_URL
window._CONFIG['staticDomainURL'] = Vue.prototype.API_BASE_URL + '/sys/common/static'
window._CONFIG['downloadUrl'] = Vue.prototype.API_BASE_URL + '/sys/common/download';
window._CONFIG['pdfDomainURL'] = Vue.prototype.API_BASE_URL + '/sys/common/pdf/pdfPreviewIframe'