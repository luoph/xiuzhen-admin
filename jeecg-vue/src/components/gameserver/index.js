/**自定义全局组件 */
import ServerSelect from './ServerSelect.vue'
import MultipleServerSelect from './MultipleServerSelect.vue'

const ServerSelectList = {
    install: function (Vue) {
        Vue.component('ServerSelect', ServerSelect);
    }
};

const MultipleServerSelectList = {
    install:function(Vue){
        Vue.component('MultipleSeverSelect', MultipleServerSelect);
    }
};

export default ()=> {
   return {
       ServerSelectList, 
       MultipleServerSelectList,
    };
};