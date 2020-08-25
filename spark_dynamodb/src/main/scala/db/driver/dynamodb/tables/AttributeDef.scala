package db.driver.dynamodb.tables

import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType

/**
 * Definition of Dynamo Table Attribute
 */
case class AttributeDef(attributeName: String, attributeType: ScalarAttributeType)
