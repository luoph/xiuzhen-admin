/** init domain config */
import Vue from 'vue'
// 设置全局API_BASE_URL
Vue.prototype.API_BASE_URL = window._CONFIG.VUE_APP_API_BASE_URL ? window._CONFIG.VUE_APP_API_BASE_URL : process.env.VUE_APP_API_BASE_URL
console.debug(`process.env.BASE_URL: ${process.env.BASE_URL}`)
console.debug(`Vue.prototype.API_BASE_URL: ${Vue.prototype.API_BASE_URL}`)

if (document.location.hostname === 'localhost') {
  window._CONFIG['domainURL'] = Vue.prototype.API_BASE_URL
} else {
  window._CONFIG['domainURL'] = window.location.protocol + '//' + document.location.host + '/jeecg-boot';
}
console.debug(`domainURL: ${window._CONFIG['domainURL']}`)

// 单点登录地址
window._CONFIG['casPrefixUrl'] = window._CONFIG.VUE_APP_CAS_BASE_URL ? window._CONFIG.VUE_APP_CAS_BASE_URL : process.env.VUE_APP_CAS_BASE_URL
window._CONFIG['onlinePreviewDomainURL'] = window._CONFIG.VUE_APP_ONLINE_BASE_URL ? window._CONFIG.VUE_APP_ONLINE_BASE_URL : process.env.VUE_APP_ONLINE_BASE_URL
window._CONFIG['staticDomainURL'] = window._CONFIG['domainURL'] + '/sys/common/static'
window._CONFIG['downloadUrl'] = window._CONFIG['domainURL'] + '/sys/common/download';
window._CONFIG['pdfDomainURL'] = window._CONFIG['domainURL'] + '/sys/common/pdf/pdfPreviewIframe'