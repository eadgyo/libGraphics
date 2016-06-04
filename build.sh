#!/bin/bash
#mvn clean install

name=$(basename target/*javadoc.jar)
group=${name%%-*}
idname=${name#*-}
idname=$group-${idname%%-*}
version=${name#*-}
version=${version#*-}
version=${version%-*}

m2=~/.m2/repository

echo "Jar in $group/$idname/$version"

if [ ! -d "$group" ]
then
    mkdir $group
fi

if [ ! -d "$group/$idname" ]
then
    mkdir "$group/$idname"
fi

if [ ! -d "$group/$idname/$version" ]
then
    mkdir "$group/$idname/$version"
fi

if [ ! -d "$m2/$group" ]
then
    mkdir "$m2/$group"
fi

if [ ! -d "$m2/$group/$idname" ]
then
    mkdir "$m2/$group/$idname"
fi

if [ ! -d "$m2/$group/$idname/$version" ]
then
    mkdir "$m2/$group/$idname/$version"
fi

folder="$group/$idname/$version"

echo "$m2/$folder"

cp target/*.jar "$folder"
cp target/*.jar "$m2/$folder"
cp pom.xml $folder/$idname-$version.pom
cp pom.xml "$m2/$folder/$idname-$version.pom"
