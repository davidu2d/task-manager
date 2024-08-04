import boto3
import json

boto3.setup_default_session(region_name='us-east-1')

s3_client = boto3.client(
    "s3",
    endpoint_url="http://localhost:4566",
    aws_access_key_id="localstack",
    aws_secret_access_key="localstack"
)

sqs_client = boto3.client(
    "sqs",
    endpoint_url="http://localhost:4566",
    aws_access_key_id="localstack",
    aws_secret_access_key="localstack"
)

lambda_client = boto3.client(
    'lambda',
    endpoint_url='http://localhost:4566',
    aws_access_key_id="localstack",
    aws_secret_access_key="localstack"
)

# Cria bucket S3
s3_client.create_bucket(Bucket="task-manager")

# Cria a fila SQS
response = sqs_client.create_queue(QueueName="task-manager", Attributes={'DelaySeconds': '5'})
queue_url = response['QueueUrl']

# Cria a função Lambda
zip_path = '/etc/localstack/init/ready.d/lambda_function.zip'
with open(zip_path, 'rb') as f:
    zipped_code = f.read()

response = lambda_client.create_function(
    FunctionName='TasksLambdaFunction',
    Runtime='python3.8',
    Role='arn:aws:iam::000000000000:role/lambda-role',
    Handler='lambda_function.lambda_handler',
    Code=dict(ZipFile=zipped_code),
    Timeout=300,
    MemorySize=128,
)

lambda_arn = response['FunctionArn']

# Criar uma permissão para a Lambda acessar SQS
lambda_client.add_permission(
    FunctionName='TasksLambdaFunction',
    StatementId='SQSInvoke',
    Action='lambda:InvokeFunction',
    Principal='sqs.amazonaws.com',
    SourceArn=queue_url
)

# Obter a ARN da fila SQS
queue_arn = sqs_client.get_queue_attributes(
    QueueUrl=queue_url,
    AttributeNames=['QueueArn']
)['Attributes']['QueueArn']

event_source_mapping = lambda_client.create_event_source_mapping(
    EventSourceArn=queue_arn,
    FunctionName=lambda_arn,
    BatchSize=10
)