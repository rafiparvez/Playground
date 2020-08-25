package db.driver.dynamodb.tables

/**
 * Provisioned Throughout for DynamoDB Table
 * @param readCapacityUnits
 * @param writeCapacityUnits
 */
case class ProvisionedThroughputDef(readCapacityUnits: Long, writeCapacityUnits: Long)
