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
$0 -p profile -n server_name -h host -s server_path
profile -- 环境名, develop/production/stable
server_name -- 服务名, eg: payservice
host -- 远程服务器帐号ip, eg: root@10.21.210.70
dir -- 服务器接收jar包文件夹
-EOF-
    exit 1
}

[[ $# -eq 0 ]] && usage
while getopts "p:n:h:s:w:" opt; do
    case ${opt} in
    p)
        profile=$OPTARG
        ;;
    n)
        server_name=$OPTARG
        ;;
    h)
        host=$OPTARG
        ;;
    w)
        work_path=$OPTARG
        ;;
    s)
        server_path=$OPTARG
        ;;
    ?)
        usage
        ;;
    esac
done

if [[ -z "$profile" ]] \
    || [[ -z "$server_name" ]] \
    || [[ -z "$host" ]] \
    || [[ -z "$work_path" ]] \
    || [[ -z "$server_path" ]]; then
    usage
fi

logger "==> profile:[${profile}]"
logger "==> server_name:[${server_name}]"
logger "==> remote_host:[${host}]"
logger "==> work_path:[${work_path}]"
logger "==> server_path:[${server_path}]"

target_path=${work_path}/target

logger "==> start building"
mvn clean package -P${profile}

cd ${module_name}/target
mv ${module_name}-${version}.jar ${server_name}.jar
logger "==> finish building"

ssh_key="/var/lib/jenkins/.ssh/id_rsa"
logger "==> start uploading:${server_name}.jar"
logger "==> scp -i ${ssh_key} -o StrictHostKeyChecking=no ${server_name}.jar ${host}:${target_path}"
scp -i ${ssh_key} -o StrictHostKeyChecking=no target/${server_name}.jar ${host}:${target_path}
logger "==> finish uploading:${server_name}.jar"

logger "==> start deploying"

backup_path=${work_path}/backup

args="-p ${profile} -n ${server_name} -s ${server_path} -b ${backup_path}"
logger "==> service.sh args:$args"

ssh -i ${ssh_key} ${host} << ENDSSH

bash ${work_path}/service.sh stop ${args}

bash ${work_path}/service.sh backup ${args}
cp ${target_path}/${server_name}.jar ${server_path}

bash ${work_path}/service.sh start ${args}

ENDSSH

logger "==> finish deploying"
