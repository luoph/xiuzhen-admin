<template>
    <div>
        <a-form-item label="玩法类型" :label-col="{ span: 12 }" :wrapper-col="{ span: 16 }">
            <a-select placeholder="请选择玩法类型" v-model="playMethodsType" :initialValue="playMethodsType" @change="onSelectPlayMethodsType">
                <a-select-option v-for="playMethodsType in playMethodsTypeList" :key="playMethodsType.name" :value="playMethodsType.type">
                    {{ playMethodsType.type + "-" + playMethodsType.name }}
                </a-select-option>
            </a-select>
        </a-form-item>
    </div>
</template>

<script>
import { getAction } from "@/api/manage";

export default {
    name: "PlayMethodsType",
    components: {
        getAction
    },
    // 编译成html后加载初始化页面的数据
    created() {
        this.getList();
    },
    // 数据加载 return方式 局限于本页面内 防止污染其他页面的数据
    data() {
        return {
            playMethodsType: undefined,
            // 玩法类型数据
            playMethodsTypeList: [],
            url: {
                playMethodsTypeUrl: "game/commonModule/playMethodsTypeShowList"
            }
        };
    },
    methods: {
        getList() {
            getAction(this.url.playMethodsTypeUrl, { playMethodsType: this.playMethodsType }).then((res) => {
                this.playMethodsTypeList = res.result.record;
            });
        },
        onSelectPlayMethodsType(value) {
            for (var j = 0; j < this.playMethodsTypeList.length; j++) {
                if (this.playMethodsTypeList[j].type == value) {
                    // 触发父容器的方法
                    this.$emit("onSelectPlayMethodsType", this.playMethodsTypeList[j]);
                }
            }
        }
    },
    watch: {}
};
</script>
<style lang="less" scoped></style>