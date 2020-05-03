var params = {
    TableName: 'PROD_USER',
    Item: { // a map of attribute name to AttributeValue

        UUID: 'uuid-123456',
        USERNAME: 'username-13454422dafbdbdfb'
    },
    ReturnConsumedCapacity: 'TOTAL', // optional (NONE | TOTAL | INDEXES)
};
docClient.put(params, function(err, data) {
    if (err) ppJson(err); // an error occurred
    else ppJson(data); // successful response
});