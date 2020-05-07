var params = {
    TableName: 'PROD_USER',
    Key: {
        UUID: 'uuid-123456'
    },

    ConsistentRead: true,
    ReturnConsumedCapacity: 'TOTAL'
};
docClient.get(params, function(err, data) {
    if (err) ppJson(err); // an error occurred
    else ppJson(data); // successful response
});