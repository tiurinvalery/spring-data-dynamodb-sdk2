var params = {
    TableName: 'PROD_USER',
};
dynamodb.deleteTable(params, function(err, data) {
    if (err) ppJson(err); // an error occurred
    else ppJson(data); // successful response
});