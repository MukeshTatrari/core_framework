#GENERATED_SOURCE_DIR=./target/generated-sources/raml-to-jaxrs-maven-plugin/
GENERATED_SOURCE_DIR=./src/main/java/

# remove cyclic inheritance
grep -rl 'ErrorResponse' $GENERATED_SOURCE_DIR | xargs sed -i 's/ErrorResponse extends ErrorResponse/ErrorResponse/g'

# remove conflicting inheritance
grep -rl 'extends PaginatedSearchResult' $GENERATED_SOURCE_DIR | xargs sed -i 's/ extends PaginatedSearchResult {/{/g'

# remove API resources
find $GENERATED_SOURCE_DIR -name "resources" -type d -exec rm -r "{}" \;
