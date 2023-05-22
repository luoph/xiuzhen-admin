/** 自定义全局组件 */
import ServerSelect from "./ServerSelect.vue";
import MultipleServerSelect from "./MultipleServerSelect.vue";
import GameChannelServer from "./GameChannelServer.vue";
import ASelectReadJson from "./ASelectReadJson.vue";
import JsonSelector from "./JsonSelector.vue";

const ServerSelectList = {
    install: function (Vue) {
        Vue.component("ServerSelect", ServerSelect);
    }
};

const MultipleServerSelectList = {
    install: function (Vue) {
        Vue.component("MultipleSeverSelect", MultipleServerSelect);
    }
};

const GameChannelServerList = {
    install: function (Vue) {
        Vue.component("GameChannelServer", GameChannelServer);
    }
};

const ASelectReadJsonList = {
    install: function (Vue) {
        Vue.component("ASelectReadJson", ASelectReadJson);
    }
};

const JsonSelectorList = {
    install: function (Vue) {
        Vue.component("JsonSelector", JsonSelector);
    }
};

export default () => {
    return {
        ServerSelectList,
        MultipleServerSelectList,
        GameChannelServerList,
        ASelectReadJsonList,
        JsonSelectorList
    };
};
