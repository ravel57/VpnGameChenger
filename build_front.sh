#!/bin/bash

path=$(pwd)
cd ../../WebstormProjects/vpn-game-changer-front
yarn install
yarn build

# Check if the build directory exists
if [ -d "./dist/spa/" ]; then
    cp -R ./dist/spa/* "$path/src/main/resources/static"
else
    echo "Building error" >&2
    exit 1
fi

cd "$path"