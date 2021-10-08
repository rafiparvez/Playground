import base64
import json

import boto3

print('Loading function')

eventbridge_client = boto3.client('events')

event_entry = {
    "Detail": "",
    "DetailType": "analyticsEvents",
    "EventBusName": "p13n-events",
    "Source": "AnalyticsEventsProcessor"
}


def lambda_handler(event, context):
    # print("Received event: " + json.dumps(event, indent=2))
    print(f"# records = {len(event.get('Records'))}")
    entries_list = []
    for record in event['Records']:
        # Kinesis data is base64 encoded so decode here
        payload = json.loads(base64.b64decode(record['kinesis']['data']))

        eventbridge_client

        if payload.get('event_type') == 'completed-lesson':
            event_entry["Detail"] = json.dumps(payload)
            entries_list.append(event_entry)
            print("Decoded payload: ", payload)

    if entries_list:
        print("entries_list", entries_list)
        put_event_resp = eventbridge_client.put_events(Entries=entries_list)
        print("put_event_resp", json.dumps(put_event_resp, indent=2))
    return 'Successfully processed {} records.'.format(len(event['Records']))
