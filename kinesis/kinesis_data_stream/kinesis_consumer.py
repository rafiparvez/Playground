import boto3
import json
from datetime import datetime
import time

my_stream_name = 'playground-analytics-events'

kinesis_client = boto3.client('kinesis', region_name='us-east-1')

response = kinesis_client.describe_stream(StreamName=my_stream_name)

my_shard_id = response['StreamDescription']['Shards'][0]['ShardId']

shard_iterator = kinesis_client.get_shard_iterator(StreamName=my_stream_name,
                                                      ShardId=my_shard_id,
                                                      ShardIteratorType='TRIM_HORIZON')

my_shard_iterator = shard_iterator['ShardIterator']

record_response = kinesis_client.get_records(ShardIterator=my_shard_iterator,
                                              Limit=2)

while 'NextShardIterator' in record_response:
    record_response = kinesis_client.get_records(ShardIterator=record_response['NextShardIterator'])
    records = record_response.get('Records')
    print(record_response)

    # wait for 2 second
    print("wait...")
    time.sleep(1)
