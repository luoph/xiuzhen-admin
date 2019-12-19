#!/bin/bash

function logger() {
    time=$(date +'%Y-%m-%d %H:%M:%S')
    echo "["${time}"] "$1
}

function usage() {
    cat << -EOF-
Usage:
$0 -h host -w work_path -f frontend
host -- 远程服务器帐号ip, eg: root@10.21.210.70
work_path -- 前端项目备份和接收压缩包路径
frontend -- 前端路径
-EOF-
    exit 1
}

[[ $# -eq 0 ]] && usage
while getopts "h:w:f:" opt; do
    case ${opt} in
    h)
        host=$OPTARG
        ;;
    w)
        work_path=$OPTARG
        ;;
    f)
        frontend=$OPTARG
        ;;
    ?)
        usage
        ;;
    esac
done

if [[ -z "$host" ]] ||
    [[ -z "$work_path" ]] ||
    [[ -z "$frontend" ]]; then
    usage
fi

logger "==> host:[${host}]"
logger "==> work_path:[${work_path}]"
logger "==> frontend:[${frontend}]"

logger "==> start building"
yarn install
yarn build

logger "==> finish building"

project=jeecg-vue
# 编译目录
output=dist
# 压缩文件
cd ${output}
zip_file=${project}.zip

# 删除旧文件
if [[ -f "$zip_file" ]]; then
    rm -rf $zip_file
fi

zip -qr ${zip_file} ${output}
rm -rf ${output}

# 上传包和备份包路径
package_path=${work_path}/package/
backup_path=${work_path}/backup/

ssh_key="/var/lib/jenkins/.ssh/id_rsa"
logger "==> start uploading:${zip_file}"
logger "==> scp -i ${ssh_key} -o StrictHostKeyChecking=no ${zip_file} ${host}:${package_path}"
scp -i ${ssh_key} -o StrictHostKeyChecking=no ${zip_file} ${host}:${package_path}
logger "==> finish uploading:${zip_file}"

logger "==> start deploying"

frontend_parent="$(dirname "${frontend}")"
frontend_folder="$(basename "${frontend}")"

logger "==> frontend_parent: ${frontend_parent}"
logger "==> frontend_folder: ${frontend_folder}"

ssh -i ${ssh_key} ${host} <<ENDSSH

cd ${work_path}
echo "bash deploy-client.sh -p ${project} -w ${work_path} -f ${frontend}"
bash deploy-client.sh -p ${project} -w ${work_path} -f ${frontend}

ENDSSH

logger "==> finish deploying"
