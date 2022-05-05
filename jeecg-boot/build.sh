#!/bin/bash

module_name="jeecg-boot-module-system"
time_zone_java="jeecg-boot-base-common/src/main/java/org/jeecg/common/constant/TimeConstant.java"
version="2.1.2"

function logger() {
    time=$(date +'%Y-%m-%d %H:%M:%S')
    echo "["${time}"] "$1
}

function get_property() {
    key=$1
    file=$2
    PROP_VALUE=$(cat $file | grep "${key}=" | cut -d'=' -f2)
    echo $PROP_VALUE
}

function usage() {
    cat <<-EOF
Usage:
$0 -p profile -s server_name
profile -- 环境名, develop/production/stable
server_name -- 服务名, eg: xiuzhen-main
EOF
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

if [[ -z "${profile}" ]] ||
    [[ -z "${server_name}" ]]; then
    usage
fi

logger "==> profile:[${profile}]"
logger "==> server_name:[${server_name}]"

# 拷贝对应环境的配置
project_dir=$(pwd)
cd ${module_name}/src/main/profile/${profile}/
cp -Rf * ../../resources/
cd ${project_dir}

time_zone=$(get_property "spring.jackson.time-zone" "${module_name}/src/main/resources/application-main.properties")
logger "==> time_zone: ${time_zone}"

# 默认的时区
default_time_zone=$(cat ${time_zone_java} | grep 'DEFAULT_TIMEZONE' | cut -d'=' -f2 | cut -d'"' -f2)

if [[ "${time_zone}" != "${default_time_zone}" ]]; then
    logger "==> replace timezone ${default_time_zone} --> ${time_zone}"
    if [[ "$OSTYPE" == "darwin"* ]]; then
        sed -i "" "s|${default_time_zone}|${time_zone}|g" ${time_zone_java}
    else
        sed -i "s|${default_time_zone}|${time_zone}|g" ${time_zone_java}
    fi
fi

logger "==> start building"
mvnd clean package -DskipTests

if [[ ! -d "target" ]]; then
    mkdir -p "target"
fi

mv ${module_name}/target/${module_name}-${version}.jar target/${server_name}-${profile}.jar
logger "==> finish building"
