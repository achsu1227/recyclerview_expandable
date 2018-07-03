#!bin/sh

<<comment
apk_full_path=$(find -L $PWD -name "*.apk")
for element in ${apk_full_path[@]} 
do
	echo $element

done
comment

APK_OUTPUT_FOLDER=
apk_full_path=$(find -L . -name "*.apk")
for element in ${apk_full_path[@]} 
do
	#echo $element
	parentdir="$(dirname $element)"
	echo $parentdir
	APK_OUTPUT_FOLDER=$parentdir
done

echo $APK_OUTPUT_FOLDER
#echo "${PWD##*/}"