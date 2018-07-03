
export OUTPUT_FOLDER="s3_output_apk"
rm -r "$OUTPUT_FOLDER"
mkdir -p "$OUTPUT_FOLDER"

APK_OUTPUT_FOLDER="app/build/outputs/apk"
apk_full_path=$(find -L . -name "*.apk")
for element in ${apk_full_path[@]} 
do
	echo $element
	parentdir="$(dirname $element)"
	#echo $parentdir
	export APK_OUTPUT_FOLDER="$parentdir"
	cp $element "$OUTPUT_FOLDER"
done

#export APK_OUTPUT_FOLDER

#echo $APK_OUTPUT_FOLDER

