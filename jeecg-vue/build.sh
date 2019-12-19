#!/bin/bash

function logger() {
    time=$(date +'%Y-%m-%d %H:%M:%S')
    echo "["${time}"] "$1
}

function usage() {
    cat << -EOF-
Usage:
$0 -h host -w work_path -f frontend_path
host -- 远程服务器帐号ip, eg: root@10.21.210.70
work_path -- 前端项目备份和接收压缩包路径
frontend_path -- 前端路径
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
        frontend_path=$OPTARG
        ;;
    ?)
        usage
        ;;
    esac
done

if [[ -z "${host}" ]] ||
    [[ -z "${work_path}" ]] ||
    [[ -z "${frontend_path}" ]]; then
    usage
fi

logger "==> host:[${host}]"
logger "==> work_path:[${work_path}]"
logger "==> frontend_path:[${frontend_path}]"

logger "==> start building"
yarn install
yarn build

logger "==> finish building"

project=jeecg-vue

# 压缩文件
zip_file=${project}.zip

# 删除旧文件
if [[ -f "$zip_file" ]]; then
    rm -rf $zip_file
fi

zip -qr ${zip_file} dist
rm -rf dist

# 上传包和备份包路径
package_path=${work_path}/package
backup_path=${work_path}/backup

logger "==> start uploading:${zip_file}"
upload_path=${package_path}/${project}
bash /usr/local/bin/lofile-uploader.sh -h ${host} -f ${zip_file} -d ${upload_path}

logger "==> start deploying"
frontend_parent="$(dirname "${frontend_path}")"
frontend_folder="$(basename "${frontend_path}")"

logger "==> frontend_parent: ${frontend_parent}"
logger "==> frontend_folder: ${frontend_folder}"

ssh ${host} <<ENDSSH

cd ${work_path}
echo "bash frontend.sh -p ${project} -w ${work_path} -f ${frontend_path}"
bash frontend.sh -p ${project} -w ${work_path} -f ${frontend_path}

ENDSSH

logger "==> finish deploying"
