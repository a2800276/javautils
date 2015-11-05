
JAR_DIR=jar

if [ -d ${JAR_DIR} ] ; then
  HEAD_VERSION=`git log -1 --pretty=format:%h`
  JAR_IN=out/artifacts/crypto_jar/crypto.jar
  JAR_OUT=jar/crypto-${HEAD_VERSION}.jar
  cp ${JAR_IN} ${JAR_OUT}
else
  echo "no such dir: ${JAR_DIR}" 
fi


