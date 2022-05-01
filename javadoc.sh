#! /usr/bin/env bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
SOURCE_DIR=${DIR}/target/site/apidocs
TARGET_DIR=${DIR}/apidocs
mvn javadoc:javadoc

rm -rf ${TARGET_DIR}
cp -r ${SOURCE_DIR} ${TARGET_DIR}