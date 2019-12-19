#!/bin/bash

#bash build.sh -p stable -n payservice -h 10.21.210.70 -w "/data/1101studio" -s "/data/1101studio/server"

module_name="jeecg-boot-module-system"
version="2.1.2"

function logger() {
    time=$(date +'%Y-%m-%d %H:%M:%S')
    echo "["${time}"] "$1
}

function usage() {
    cat << -EOF-
Usage:
$0 -p profile -n server_name
profile -- 环境名, develop/production/stable
server_name -- 服务名, eg: payservice
-EOF-
    exit 1
}

[[ $# -eq 0 ]] && usage
while getopts "p:n:" opt; do
    case ${opt} in
    p)
        profile=$OPTARG
        ;;
    n)
        server_name=$OPTARG
        ;;
    ?)
        usage
        ;;
    esac
done

if [[ -z "$profile" ]] \
    || [[ -z "$server_name" ]]; then
    usage
fi

logger "==> profile:[${profile}]"
logger "==> server_name:[${server_name}]"

logger "==> start building"
mvn clean package -P${profile}

if [[! -d target ]]; then
    mkdir target
fi

mv ${module_name}-${version}.jar target/${server_name}.jar

logger "==> finish building"

logger "==> start uploading:${server_name}.jar"
