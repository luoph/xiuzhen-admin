/**
 * 新增修改完成调用 modalFormOk方法 编辑弹框组件ref定义为modalForm
 * 高级查询按钮调用 superQuery方法  高级查询组件ref定义为superQueryModal
 * data中url定义 list为查询列表  delete为删除单条记录  deleteBatch为批量删除
 */
import { deleteAction, downFile, getAction } from "@/api/manage";
import { ACCESS_TOKEN } from "@/store/mutation-types";
import { filterObj } from "@/utils/util";
import Vue from "vue";

export const JeecgListMixin = {
    data() {
        return {
            // token header
            tokenHeader: {
                "X-Access-Token": Vue.ls.get(ACCESS_TOKEN)
            },
            /* 查询条件-请不要在queryParam中声明非字符串值的属性 */
            queryParam: {},
            /* 数据源 */
            dataSource: [],
            /* 是否需要选择serverId，默认不需要 */
            serverIdOption: false,
            /* 分页参数 */
            ipagination: {
                current: 1,
                pageSize: 50,
                pageSizeOptions: ["20", "50", "100"],
                showTotal: (total, range) => {
                    return range[0] + "-" + range[1] + " 共" + total + "条";
                },
                showQuickJumper: true,
                showSizeChanger: true,
                total: 0
            },
            /* 排序参数 */
            isorter: {
                column: "createTime",
                order: "desc"
            },
            /* 筛选参数 */
            filters: {},
            /* table加载状态 */
            loading: false,
            /* table选中keys*/
            selectedRowKeys: [],
            /* table选中records*/
            selectionRows: [],
            /* 查询折叠 */
            toggleSearchStatus: false,
            /* 高级查询条件生效状态 */
            superQueryFlag: false,
            /* 高级查询条件 */
            superQueryParams: ""
        };
    },
    created() {
        // 服务器id
        if (this.serverIdOption) {
            this.queryServerList();
        }
        if (!this.disableMixinCreated) {
            this.loadData();
            // 初始化字典配置 在自己页面定义
            this.initDictConfig();
        }
    },
    methods: {
        loadData(arg) {
            if (!this.url.list) {
                this.$message.error("请设置url.list属性!");
                return;
            }
            // 加载数据 若传入参数1则加载第一页的内容
            if (arg === 1) {
                this.ipagination.current = 1;
            }
            // 查询条件
            var params = this.getQueryParams();
            if (this.serverIdOption && !params.serverId || params.serverId < 0) {
                this.$message.error("请选择服务器id");
                return;
            }
            this.loading = true;
            getAction(this.url.list, params).then(res => {
                if (res.success) {
                    this.dataSource = res.result.records;
                    this.ipagination.total = res.result.total;
                }
                if (res.code === 510) {
                    this.$message.warning(res.message);
                }
                this.loading = false;
            });
        },
        initDictConfig() {},
        handleSuperQuery(arg) {
            // 高级查询方法
            if (!arg) {
                this.superQueryParams = "";
                this.superQueryFlag = false;
            } else {
                this.superQueryFlag = true;
                this.superQueryParams = JSON.stringify(arg);
            }
            this.loadData();
        },
        getQueryParams() {
            // 获取查询条件
            const sqp = {};
            if (this.superQueryParams) {
                sqp["superQueryParams"] = encodeURI(this.superQueryParams);
            }
            var param = Object.assign(sqp, this.queryParam, this.isorter, this.filters);
            param.field = this.getQueryField();
            param.pageNo = this.ipagination.current;
            param.pageSize = this.ipagination.pageSize;
            param.serverId = this.queryParam.serverId;
            return filterObj(param);
        },
        getQueryField() {
            // TODO 字段权限控制
            var str = "id,";
            this.columns.forEach(function(value) {
                str += "," + value.dataIndex;
            });
            return str;
        },

        onSelectChange(selectedRowKeys, selectionRows) {
            this.selectedRowKeys = selectedRowKeys;
            this.selectionRows = selectionRows;
        },
        onClearSelected() {
            this.selectedRowKeys = [];
            this.selectionRows = [];
        },
        searchQuery() {
            this.loadData(1);
        },
        superQuery() {
            this.$refs.superQueryModal.show();
        },
        searchReset() {
            this.queryParam = {};
            this.loadData(1);
        },
        batchDel: function() {
            if (!this.url.deleteBatch) {
                this.$message.error("请设置url.deleteBatch属性!");
                return;
            }
            if (this.selectedRowKeys.length <= 0) {
                this.$message.warning("请选择一条记录！");
                return;
            } else {
                var ids = "";
                for (var a = 0; a < this.selectedRowKeys.length; a++) {
                    ids += this.selectedRowKeys[a] + ",";
                }
                var that = this;
                this.$confirm({
                    title: "确认删除",
                    content: "是否删除选中数据?",
                    onOk: function() {
                        that.loading = true;
                        deleteAction(that.url.deleteBatch, {
                            ids: ids
                        })
                            .then(res => {
                                if (res.success) {
                                    that.$message.success(res.message);
                                    that.loadData();
                                    that.onClearSelected();
                                } else {
                                    that.$message.warning(res.message);
                                }
                            })
                            .finally(() => {
                                that.loading = false;
                            });
                    }
                });
            }
        },
        handleDelete: function(id) {
            if (!this.url.delete) {
                this.$message.error("请设置url.delete属性!");
                return;
            }
            var that = this;
            deleteAction(that.url.delete, {
                id: id
            }).then(res => {
                if (res.success) {
                    that.$message.success(res.message);
                    that.loadData();
                } else {
                    that.$message.warning(res.message);
                }
            });
        },
        handleEdit: function(record) {
            this.$refs.modalForm.edit(record);
            this.$refs.modalForm.title = "编辑";
            this.$refs.modalForm.disableSubmit = false;
        },
        handleAdd: function() {
            this.$refs.modalForm.add();
            this.$refs.modalForm.title = "新增";
            this.$refs.modalForm.disableSubmit = false;
        },
        handleTableChange(pagination, filters, sorter) {
            // 分页、排序、筛选变化时触发
            // TODO 筛选
            if (Object.keys(sorter).length > 0) {
                this.isorter.column = sorter.field;
                this.isorter.order = "ascend" == sorter.order ? "asc" : "desc";
            }
            this.ipagination = pagination;
            this.loadData();
        },
        handleToggleSearch() {
            this.toggleSearchStatus = !this.toggleSearchStatus;
        },
        modalFormOk() {
            // 新增/修改 成功时，重载列表
            this.loadData();
        },
        handleDetail: function(record) {
            this.$refs.modalForm.edit(record);
            this.$refs.modalForm.title = "详情";
            this.$refs.modalForm.disableSubmit = true;
        },
        /* 导出 */
        handleExportXls2() {
            const paramsStr = encodeURI(JSON.stringify(this.getQueryParams()));
            const url = `${window._CONFIG["domainURL"]}/${this.url.exportXlsUrl}?paramsStr=${paramsStr}`;
            window.location.href = url;
        },
        handleExportXls(fileName) {
            if (!fileName || typeof fileName != "string") {
                fileName = "导出文件";
            }
            const param = {
                ...this.queryParam
            };
            if (this.selectedRowKeys && this.selectedRowKeys.length > 0) {
                param["selections"] = this.selectedRowKeys.join(",");
            }
            console.log("导出参数", param);
            downFile(this.url.exportXlsUrl, param).then(data => {
                if (!data) {
                    this.$message.warning("文件下载失败");
                    return;
                }
                if (typeof window.navigator.msSaveBlob !== "undefined") {
                    window.navigator.msSaveBlob(new Blob([data]), fileName + ".xls");
                } else {
                    const url = window.URL.createObjectURL(new Blob([data]));
                    const link = document.createElement("a");
                    link.style.display = "none";
                    link.href = url;
                    link.setAttribute("download", fileName + ".xls");
                    document.body.appendChild(link);
                    link.click();
                    document.body.removeChild(link); // 下载完成移除元素
                    window.URL.revokeObjectURL(url); // 释放掉blob对象
                }
            });
        },
        /* 导入 */
        handleImportExcel(info) {
            if (info.file.status !== "uploading") {
                console.log(info.file, info.fileList);
            }
            if (info.file.status === "done") {
                if (info.file.response.success) {
                    // this.$message.success(`${info.file.name} 文件上传成功`);
                    if (info.file.response.code === 201) {
                        const {
                            message,
                            result: { msg, fileUrl, fileName }
                        } = info.file.response;
                        const href = window._CONFIG["domainURL"] + fileUrl;
                        this.$warning({
                            title: message,
                            content: (
                                <div>
                                    <span> {msg} </span>
                                    <br />
                                    <span>
                                        {" "}
                                        具体详情请{" "}
                                        <a href={href} target="_blank" download={fileName}>
                                            {" "}
                                            点击下载{" "}
                                        </a>{" "}
                                    </span>
                                </div>
                            )
                        });
                    } else {
                        this.$message.success(info.file.response.message || `${info.file.name} 文件上传成功`);
                    }
                    this.loadData();
                } else {
                    this.$message.error(`${info.file.name} ${info.file.response.message}.`);
                }
            } else if (info.file.status === "error") {
                this.$message.error(`文件上传失败: ${info.file.msg} `);
            }
        },
        /* 图片预览 */
        getImgView(text) {
            if (text && text.indexOf(",") > 0) {
                text = text.substring(0, text.indexOf(","));
            }
            return window._CONFIG["imgDomainURL"] + "/" + text;
        },
        /* 文件下载 */
        uploadFile(text) {
            if (!text) {
                this.$message.warning("未知的文件");
                return;
            }
            if (text.indexOf(",") > 0) {
                text = text.substring(0, text.indexOf(","));
            }
            window.open(window._CONFIG["domainURL"] + "/sys/common/download/" + text);
        },
        queryServerList() {
            let that = this;
            if (!this.url.serverListUrl) {
                this.$message.error("请设置url.serverListUrl属性");
                return;
            }
            getAction(that.url.serverListUrl).then(res => {
                if (res.success) {
                    if (res.result instanceof Array) {
                        this.serverList = res.result;
                    } else if (res.result.records instanceof Array) {
                        this.serverList = res.result.records;
                    }
                } else {
                    this.serverList = [];
                }
            });
        }
    }
};
