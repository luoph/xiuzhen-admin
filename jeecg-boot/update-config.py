# _*_ coding:utf-8 _*_

# 运行脚本前请先安装依赖：
# pip install svn

import argparse
import codecs
import fnmatch
import logging
import os
import re
import shutil
import svn.remote
import sys

PY2 = sys.version_info[0] == 2
PY3 = sys.version_info[0] == 3

if PY3:
    from importlib import reload
reload(sys)

if PY2:
    sys.setdefaultencoding('utf-8')

REMOTE_URL = u"{}/doc_xiuzhen/A-%E5%AF%BC%E8%A1%A8/json/{}/server"
SEARCH_DIR = 'jeecg-boot-module-system'
TARGET_FILE = 'GameConfig.java'
JSON_DIR = 'jeecg-boot-module-system/src/main/resources/json'

# 其他的json
OTHER_JSON = ['task_module_type', 'item_fall_rule', 'item_expend']

# 匹配代码行
CODE_MATCH = 'ConfigTable.valueOf'
CODE_LINE_REGEX = CODE_MATCH + '\([\s\n ]*"[a-zA-Z0-9_]+",'
CONFIG_QUOTE = '"'


def parse_config(config_file, regex, search_str, start_str, end_str):
    config_list = []
    if (not config_file) or (not os.path.exists(config_file)):
        return config_list

    fp = codecs.open(config_file, 'r', encoding='utf-8')
    file_data = fp.read()
    fp.close()

    pattern = re.compile(regex)
    matches = pattern.findall(file_data)
    for item in matches:
        match_line = item.replace('\r\n', '').replace('\n', '').strip()
        start = match_line.find(start_str)
        if (start < 0) or (search_str not in match_line):
            continue

        match_line = match_line[start + len(start_str):-1]
        end = match_line.find(end_str)
        config_table = match_line[0:end]

        if config_table not in config_list:
            config_list.append(config_table)

    config_list.sort()
    return config_list


def remove_dir(dir):
    if os.path.exists(dir):
        shutil.rmtree(dir, True, False)


def mkdir(dir):
    if not os.path.exists(dir):
        os.makedirs(dir)


def copy_json(svn_url, source_folder, target_folder, config_list, other_config=None, verbose=False):
    r = svn.remote.RemoteClient(svn_url)
    r.export(source_folder, revision=None, force=True)

    logging.info(u'svn_url --> {}'.format(svn_url))
    mkdir(target_folder)

    check_file = len(config_list) > 0
    if other_config is not None and len(other_config) > 0:
        config_list = config_list + other_config

    if os.path.isdir(source_folder):
        g = os.walk(source_folder)
        for path, dir_list, file_list in g:
            for file_name in file_list:
                if file_name.endswith('.json'):
                    base_name = os.path.splitext(file_name)[0]
                    if not check_file or base_name in config_list:
                        if verbose:
                            logging.info(u'copy {}'.format(file_name))
                        shutil.copy2(os.path.join(path, file_name), target_folder)

    remove_dir(source_folder)


if __name__ == '__main__':
    LOG_FORMAT = "[%(levelname)s] %(asctime)s %(message)s"
    logging.basicConfig(level=logging.DEBUG, format=LOG_FORMAT)

    parser = argparse.ArgumentParser(description='manual to this script')
    parser.add_argument('--svn', type=str, default='svn://139.9.66.217:7760')
    parser.add_argument('--profile', type=str, default='develop')
    parser.add_argument('--verbose', dest='verbose', type=str, default='False')

    args = parser.parse_args()

    # 查找 RefMgr.ts
    config_file_path = ''
    for root, dirnames, filenames in os.walk(SEARCH_DIR):
        for filename in fnmatch.filter(filenames, TARGET_FILE):
            config_file_path = os.path.join(root, filename)
            logging.info('config_file_path:{}'.format(config_file_path))
            break

    logging.info('start update-config, profile:{}'.format(args.profile))
    all_config = parse_config(config_file_path, CODE_LINE_REGEX, CODE_MATCH, CONFIG_QUOTE, CONFIG_QUOTE)
    repo_url = REMOTE_URL.format(args.svn, args.profile)
    copy_json(repo_url, 'xiuzhen-config', JSON_DIR, all_config, OTHER_JSON, args.verbose == 'True')
    logging.info('finish update-config, profile:{}'.format(args.profile))
