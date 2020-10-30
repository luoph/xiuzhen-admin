<template>
    <div>
        <a-row>
            <a-col :span="4">
                登录天数
            </a-col>
            <a-col :span="8">
                <a-input-number v-model="model.loginDay" placeholder="请输入登录天数" />
            </a-col>
        </a-row>
        <a-row>
            <a-col :span="4">
                描述
            </a-col>
            <a-col :span="8">
                <a-input v-model="model.description" placeholder="请输入描述" />
            </a-col>
        </a-row>
        <a-row>
            <a-col :span="4">
                奖励列表
            </a-col>
            <a-col :span="8">
                <a-input v-model="model.reward" placeholder="请输入奖励列表" />
            </a-col>
        </a-row>
    </div>
</template>

<script>
export default {
    name: "GameCampaignLoginModule",
    props: {
        loginDay: {
            type: String,
            required: true
        },
        description: {
            type: String,
            required: true
        },
        reward: {
            type: String,
            required: true
        }
    },
    watch: {
        value: {
            immediate: true,
            handler: function() {
                this.initVal();
            }
        }
    },
    model: {
        prop: "value",
        event: "change"
    },
    data() {
        return {
            inputVal: ""
        };
    },
    methods: {
        initVal() {},
        backValue(e) {}
    }
};
</script>

<style scoped></style>
