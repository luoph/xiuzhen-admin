<template>
    <a-drawer :title="title" :width="width" placement="right" :closable="false" @close="close" :visible="visible">
        <div class="table-page-search-wrapper">
            <a-form layout="inline">
                <a-row :gutter="100">
                    <a-col :md="10" :sm="8">
                        <a-form-item label="服务器：" :labelCol="labelCol" :wrapperCol="wrapperCol">
                            <server-select @select="change"></server-select>
                        </a-form-item>
                    </a-col>
                    <a-col :md="10" :sm="8">
                        <a-form-item label="玩家ID">
                            <a-input placeholder="请输入玩家ID" v-model="queryParam.playerId"></a-input>
                        </a-form-item>
                    </a-col>
                    <a-col :md="10" :sm="8">
                        <a-form-item label="同步时间：" :labelCol="labelCol" :wrapperCol="wrapperCol">
                            <a-range-picker v-model="queryParam.syncTimeRange" format="YYYY-MM-DD" :placeholder="['开始时间', '结束时间']" @change="onDateChange" />
                        </a-form-item>
                    </a-col>
                </a-row>
            </a-form>
        </div>

        <div>
            <a-button style="margin-right: 50px;" type="primary" @click="handleOkSyncLog">同步</a-button>
            <a-button type="primary" @click="handleCancel">取消</a-button>
        </div>
    </a-drawer>
</template>

<script>
import JDate from "@/components/jeecg/JDate.vue";
import { getAction } from "@/api/manage";
import { filterObj } from "@/utils/util";
export default {
    name: "PlayerItemLogModal",
    components: {
        JDate
    },
    data() {
        return {
            form: this.$form.createForm(this),
            title: "同步",
            width: 800,
            visible: false,
            labelCol: {
                xs: { span: 50 },
                sm: { span: 5 }
            },
            wrapperCol: {
                xs: { span: 24 },
                sm: { span: 16 }
            },
            queryParam: {
                serverId: null,
                syncTimeBegin: null,
                syncTimeEnd: null,
                playerId: null
            },
            url: {
                syncLog: "player/playerItemLog/sync"
            },
            dictOptions: {}
        };
    },
    computed: {},
    created() {},
    methods: {
        initDictConfig() {},
        getQueryParams() {
            let param = Object.assign({}, this.queryParam, this.isorter);
            // 范围参数不传递后台
            delete param.syncTimeRange;
            return filterObj(param);
        },
        onDateChange: function (value, dateString) {
            this.queryParam.syncTimeBegin = dateString[0];
            this.queryParam.syncTimeEnd = dateString[1];
        },
        change(serverId) {
            this.queryParam.serverId = serverId;
        },
        handleOkSyncLog: function () {
            const that = this;
            if (this.queryParam.serverId == null || this.queryParam.serverId <= 0) {
                this.$message.error("请选择服务器");
                return;
            // } else if (this.queryParam.playerId == null || this.queryParam.playerId == "" || this.queryParam.playerId <= 0) {
            //     this.$message.error("请输入玩家ID");
            //     return;
            }else if (this.queryParam.syncTimeBegin == null || this.queryParam.syncTimeEnd == null) {
                this.$message.error("请选择同步的游戏日期");
                return;
            }
            that.confirmLoading = true;
            getAction(this.url.syncLog, this.getQueryParams())
                .then((res) => {
                    if (res.success) {
                        that.$message.success("日志同步成功!");
                        that.$emit("ok");
                    } else {
                        that.$message.error(res.message);
                    }
                })
                .finally(() => {
                    that.confirmLoading = false;
                    that.close();
                });
        },
        close() {
            this.$emit("close");
            this.visible = false;
        },
        handleCancel() {
            this.close();
        }
    }
};
</script>