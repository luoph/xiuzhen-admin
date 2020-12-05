<template>
    <game-image-component v-bind="configs" v-on="$listeners" />
</template>

<script>
import GameImageComponent from "../GameImageComponent";

export default {
    name: "GameImageSelector",
    components: { GameImageComponent },
    props: ["value"],
    data() {
        return {
            settings: {
                name: "图片",
                displayKey: "name",
                returnKeys: ["id", "imgUrl"],
                listUrl: "game/gameImage/list",
                queryParamText: "图片地址",
                multiple: false,
                columns: [
                    { title: "图片名", dataIndex: "name", align: "center", width: 100 },
                    { title: "图片类型", dataIndex: "type", align: "center", width: 100 },
                    { title: "图片地址", dataIndex: "imgUrl", align: "center", width: 100 }
                ]
            }
        };
    },
    computed: {
        configs() {
            return Object.assign({ value: this.value }, this.settings, this.$attrs);
        }
    }
};
</script>

<style lang="scss" scoped></style>
