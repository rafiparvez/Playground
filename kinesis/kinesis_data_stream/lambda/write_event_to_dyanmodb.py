import json
import boto3

dynamodb = boto3.resource('dynamodb')

def lambda_handler(event, context):

    db_table = dynamodb.Table('LessonsCompleted')

    event_detail = event.get('detail')
    print('received event detail')
    print(event_detail)

    db_item = {}

    if event_detail:
        print('writing event to db')
        db_item["timestamp"] = int(event_detail.get('timestamp'))
        db_item["user_id"] = str(event_detail.get('user_id', -1))
        db_item["lesson_rank"] = event_detail.get('lesson_rank')
        db_item["class_id"] = event_detail.get('class_id')
        db_item["lesson_id"] = event_detail.get('lesson_id')

        print(db_item)

        response = db_table.put_item(
            Item=db_item
        )
