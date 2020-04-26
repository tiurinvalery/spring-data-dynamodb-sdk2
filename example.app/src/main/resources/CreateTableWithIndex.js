var params = {
    TableName: 'PROD_USER',
    KeySchema: [
        {
            AttributeName: 'UUID',
            KeyType: 'HASH'
        }
    ],
    AttributeDefinitions: [
        {
            AttributeName: 'UUID',
            AttributeType: 'S'
        },
        {
            AttributeName: 'USERNAME',
            AttributeType: 'S'
        }
    ],
    ProvisionedThroughput: {
        ReadCapacityUnits: 300,
        WriteCapacityUnits: 300
    },
    GlobalSecondaryIndexes: [{
        IndexName: 'idndx_username',
        KeySchema: [
            {
                AttributeName: 'USERNAME',
                KeyType: 'HASH'
            }
        ],
        Projection: {
            ProjectionType: 'ALL'
        },
        ProvisionedThroughput: {
            ReadCapacityUnits: 300,
            WriteCapacityUnits: 300
        }
    }]
}

dynamodb.createTable(params, function (err, data) {
    if (err) ppJson(err); // an error occurred
    else ppJson(data); // successful response

});