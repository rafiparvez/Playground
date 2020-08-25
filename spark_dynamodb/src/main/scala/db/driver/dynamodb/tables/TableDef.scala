package db.driver.dynamodb.tables

import com.amazonaws.services.dynamodbv2.model.{
  AttributeDefinition,
  KeySchemaElement,
  KeyType,
  ProvisionedThroughput
}

/**
  * Trait defining structure of DynamoDB Table
  */
trait TableDef {
  def tableName: String
  def hashKeyName: String
  def sortKeyName: Option[String]
  def attributes: List[AttributeDef]
  def provisionedThroughputOption: Option[ProvisionedThroughputDef]

  def keySchema: List[KeySchemaElement] = {
    sortKeyName match {
      case Some(name) =>
        List[KeySchemaElement](
          new KeySchemaElement()
            .withAttributeName(hashKeyName)
            .withKeyType(KeyType.HASH),
          new KeySchemaElement()
            .withAttributeName(name)
            .withKeyType(KeyType.RANGE)
        )
      case None =>
        List[KeySchemaElement](
          new KeySchemaElement()
            .withAttributeName(hashKeyName)
            .withKeyType(KeyType.HASH))
    }
  }

  def attributeDefinitions: List[AttributeDefinition] = {
    attributes.map { attribute =>
      new AttributeDefinition()
        .withAttributeName(attribute.attributeName)
        .withAttributeType(attribute.attributeType)
    }
  }
// TODO: Define global secondary index

  def provisionedThroughput: Option[ProvisionedThroughput] = {
    provisionedThroughputOption match {
      case Some(provisionedThroughputDef) =>
        Option(
          new ProvisionedThroughput(
            provisionedThroughputDef.readCapacityUnits,
            provisionedThroughputDef.writeCapacityUnits))
      case None => None
    }
  }
}
