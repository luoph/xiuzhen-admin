import ServerSelect from './ServerSelect.vue'

const ServerSelectList = {
    install: function (Vue) {
        Vue.component('ServerSelect', ServerSelect);
    }
};

export default ServerSelectList;