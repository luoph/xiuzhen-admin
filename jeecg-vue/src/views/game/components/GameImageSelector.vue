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
                valueKey: "imgUrl",
                returnKeys: ["id", "imgUrl"],
                multiple: false
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
