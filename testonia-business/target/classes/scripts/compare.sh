#!/bin/bash

dirName=`dirname $0`
actualfile=$1
goldenfile=$2
difffile=$3

diffFolder=`dirname $difffile`

isTitlePresent=`grep -c "Present in actual/missing in golden" $difffile`

if [ $isTitlePresent -eq 0 ] ; then
	echo "<font color=\"red\">+red - Present in actual/missing in golden</font>" >> $difffile
	printf "\n" >> $difffile
	echo "<font color=\"green\">-green - Present in golden/missing in actual</font><br/>" >> $difffile
fi

perl $dirName/xmldiff.pl -p -H $actualfile $goldenfile >> $difffile