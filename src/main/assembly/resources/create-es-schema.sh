#!/bin/bash

if [ "$#" -ne 1 ]; then
echo "USAGE: create-es-schema.sh es-host:port"
exit 1
fi

host="http://$1/twitter/tweet/_mapping"
echo "Creating schema on $host"
curl -XPUT $host -d ' { "tweet" : { "properties" : { "message" : {"type" : "string"}, "hashTags" : {"type" : "string"}, "location" : {"type" : "geo_point"} } } }'
