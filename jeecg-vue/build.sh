#!/bin/bash
# 编译目录
output="dist"

function logger() {
    time=$(date +'%Y-%m-%d %H:%M:%S')
    echo "["${time}"] "$1
}

project=$1
if [[ -z "${project}" ]]; then
    echo "project not set"
    exit -1
fi

logger "==> project:[${project}]"

logger "==> start building"

if [ -x "$(command -v nvm)" ]; then
    nvm use 12
fi

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
cd ${output}
logger "==> zip -qr ${zip_file} *"
zip -qr ${zip_file} *

cd ..
if [[ ! -d "target" ]]; then
    mkdir -p target
fi
mv ${output}/${zip_file} target

logger "==> finish building"
