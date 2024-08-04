import json

def lambda_handler(event, context):
    for record in event['Records']:
        print(f"Send e-mail notification: {record['body']}")
    return {
        'statusCode': 200,
        'body': json.dumps('Hello from Lambda!')
    }
