#!/usr/bin/env bash

BASEDIR=$(dirname $(readlink -f $0))

PROJDIR=/home/vorburger/dev/Minecraft/git/ch.vorburger.minecraft.osgi/ch.vorburger.minecraft.osgi.testplugin

cp $BASEDIR/Chefile $PROJDIR/

docker run -it --rm --name che-cli \
                    -v /var/run/docker.sock:/var/run/docker.sock \
                    -v $BASEDIR/data:/data \
                    -v $PROJDIR:/chedir \
                    eclipse/che:5.3.1 $*





