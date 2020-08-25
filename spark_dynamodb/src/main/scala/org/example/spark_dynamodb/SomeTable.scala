package org.example.spark_dynamodb

import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType
import db.driver.dynamodb.tables.{
  AttributeDef,
  ProvisionedThroughputDef,
  TableDef
}

object SomeTable extends TableDef {
  override def tableName: String = "SomeTable"

  override def hashKeyName: String = "id"

  override def sortKeyName: Option[String] = None

  override def attributes: List[AttributeDef] = List(
    AttributeDef("id", ScalarAttributeType.N)
  )

  override def provisionedThroughputOption: Option[ProvisionedThroughputDef] =
    Some(ProvisionedThroughputDef(1L, 1L))
}
