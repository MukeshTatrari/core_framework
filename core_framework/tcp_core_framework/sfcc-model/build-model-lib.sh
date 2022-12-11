rm -rf ./src/main/java/

mvn clean generate-sources

./update-generated-files.sh

#mvn package -Dmaven.source.skip=true
