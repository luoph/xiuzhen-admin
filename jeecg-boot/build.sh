#!/bin/bash

module_name="jeecg-boot-module-system"
version="2.1.2"

function logger() {
    time=$(date +'%Y-%m-%d %H:%M:%S')
    echo "["${time}"] "$1
}

function usage() {
    cat << -EOF-
Usage:
$0 -p profile -s server_name
profile -- 环境名, develop/production/stable
server_name -- 服务名, eg: xiuzhen-main
-EOF-
    exit 1
}

[[ $# -eq 0 ]] && usage
while getopts "p:s:" opt; do
    case ${opt} in
    p)
        profile=$OPTARG
        ;;
    s)
        server_name=$OPTARG
        ;;
    ?)
        usage
        ;;
    esac
done

if [[ -z "${profile}" ]] \
    || [[ -z "${server_name}" ]] ; then
    usage
fi

logger "==> profile:[${profile}]"
logger "==> server_name:[${server_name}]"

# 拷贝对应环境的配置
project_dir=$(pwd)
cd ${module_name}/src/main/profile/${profile}/
cp -Rf * ../../resources/
cd ${project_dir}

logger "==> start building"
mvn clean package -DskipTests

if [[ ! -d "target" ]]; then
    mkdir -p "target"
fi

mv ${module_name}/target/${module_name}-${version}.jar target/${server_name}-${profile}.jar
logger "==> finish building"
