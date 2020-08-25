package db.driver.dynamodb

import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.auth.{
  AWSCredentialsProviderChain,
  EnvironmentVariableCredentialsProvider
}
import com.amazonaws.services.dynamodbv2.document.{DynamoDB, Table}
import com.amazonaws.services.dynamodbv2.model.{
  AttributeDefinition,
  CreateTableRequest,
  DeleteTableRequest,
  KeySchemaElement,
  ProvisionedThroughput
}
import com.amazonaws.services.dynamodbv2.{
  AmazonDynamoDB,
  AmazonDynamoDBClientBuilder
}
import db.driver.dynamodb.tables.TableDef
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.JavaConverters.asJavaCollectionConverter

class DynamoDbDriver {
  val logger: Logger = LoggerFactory.getLogger(classOf[DynamoDbDriver])
  val region = "us-east-1"

  private lazy val awsCredentials: AWSCredentialsProviderChain =
    new AWSCredentialsProviderChain(
      new EnvironmentVariableCredentialsProvider(),
      new ProfileCredentialsProvider())

  implicit lazy val dynamoDBClient: AmazonDynamoDB = AmazonDynamoDBClientBuilder
    .standard()
    .withRegion(region)
    .withCredentials(awsCredentials)
    .build()

  /**
    * check if a table exists in dynamoDb server
    * @param tableName Name of table
    * @param dynamoDBClient dynamoDB client
    * @return Boolean indicating whether table exits
    */
  def checkIfTableExists(tableName: String)(
      implicit dynamoDBClient: AmazonDynamoDB): Boolean = {
    logger.info(s"checking if table $tableName exists")
    dynamoDBClient.listTables().getTableNames.contains(tableName)
  }

  def createTableFromRequest(createTableRequest: CreateTableRequest)(
      implicit dynamoDBClient: AmazonDynamoDB): Table = {
    val table = new DynamoDB(dynamoDBClient).createTable(createTableRequest)
    logger.info(
      s"Waiting for table ${createTableRequest.getTableName} to become active...")
    table.waitForActive()
    logger.info(s"${createTableRequest.getTableName} created.")
    table
  }

  def getCreateTableRequest(
      tableName: String,
      attributeDefinitions: List[AttributeDefinition],
      keySchema: List[KeySchemaElement],
      provisionedThroughput: ProvisionedThroughput): CreateTableRequest = {
    new CreateTableRequest()
      .withTableName(tableName)
      .withAttributeDefinitions(attributeDefinitions.asJavaCollection)
      .withKeySchema(keySchema.asJavaCollection)
      .withProvisionedThroughput(provisionedThroughput)
  }

  def createTable(table: TableDef): Table = {
    val req = getCreateTableRequest(table.tableName,
                                    table.attributeDefinitions,
                                    table.keySchema,
                                    table.provisionedThroughput.get)
    createTableFromRequest(req)
  }

  def deleteTable(tableName: String)(
      implicit dynamoDBClient: AmazonDynamoDB): Unit = {
    try {
      if (checkIfTableExists(tableName)) {
        val table = new DynamoDB(dynamoDBClient).getTable(tableName)
        logger.info(s"Waiting for table $tableName to be deleted...")
        table.delete()
        table.waitForDelete()
        logger.info(s"$tableName deleted.")
      } else
        logger.warn(s"$tableName does not exist for delete")
    } catch {
      case ex: Exception =>
        logger.error(s"Unable to delete table $tableName with exception: $ex")
    }
  }

}

object DynamoDbDriver {
  val logger: Logger = LoggerFactory.getLogger(classOf[DynamoDbDriver])

  /**
    * Instantiate new DynamoDbDriver
    * @throws
    * @return New instance of DynamoDbDriver
    */
  def apply(): DynamoDbDriver = new DynamoDbDriver()

}
