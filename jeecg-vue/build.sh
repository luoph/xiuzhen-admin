#!/bin/bash
# 编译目录
output=dist

function logger() {
    time=$(date +'%Y-%m-%d %H:%M:%S')
    echo "["${time}"] "$1
}

function usage() {
    cat << -EOF-
Usage:
$0 -p project
project -- 工程名
-EOF-
    exit 1
}

[[ $# -eq 0 ]] && usage
while getopts "p:" opt; do
    case ${opt} in
    p)
        project=$OPTARG
        ;;
    ?)
        usage
        ;;
    esac
done

if [[ -z "${project}" ]]; then
    usage
fi

logger "==> project:[${project}]"

logger "==> start building"

# 清空上次的目录
if [[ -d "${output}" ]]; then
    rm -rf "${output}"
fi

yarn install
yarn build

zip_file=${project}.zip
# 删除旧文件
if [[ -f "$zip_file" ]]; then
    rm -rf $zip_file
fi

# 压缩文件
logger "==> zip -qr ${zip_file} ${output}"
zip -qr ${zip_file} ${output}

logger "==> finish building"
