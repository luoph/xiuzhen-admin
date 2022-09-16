#!/bin/bash

module_name="jeecg-boot-module-system"
time_zone_java="jeecg-boot-base-common/src/main/java/org/jeecg/common/constant/TimeConstant.java"

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

# 拷贝对应环境的配置
time_zone=$(get_property "spring.jackson.time-zone" "${module_name}/src/main/resources/application-main.properties")
logger "==> time_zone: ${time_zone}"

# 默认的时区
default_time_zone=$(cat ${time_zone_java} | grep 'DEFAULT_TIMEZONE' | cut -d'=' -f2 | cut -d'"' -f2)

if [[ "${time_zone}" != "${default_time_zone}" ]]; then
    logger "==> replace time_zone ${default_time_zone} --> ${time_zone}"
    if [[ "$OSTYPE" == "darwin"* ]]; then
        sed -i "" "s|${default_time_zone}|${time_zone}|g" ${time_zone_java}
    else
        sed -i "s|${default_time_zone}|${time_zone}|g" ${time_zone_java}
    fi
fi
