package db.driver.dynamodb

import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.auth.{AWSCredentialsProviderChain, EnvironmentVariableCredentialsProvider}
import com.amazonaws.services.dynamodbv2.document.DynamoDB
import com.amazonaws.services.dynamodbv2.model._
import com.amazonaws.services.dynamodbv2.{AmazonDynamoDB, AmazonDynamoDBClientBuilder}
import com.amazonaws.regions.Regions

object DynamoDbDriver {
  val region = Regions.US_EAST_1
  val credentials: AWSCredentialsProviderChain = getAWSCredentails()
  val endpoint: String = System.getProperty("aws.dynamodb.endpoint")

  implicit lazy val defaultClient: AmazonDynamoDB = AmazonDynamoDBClientBuilder
    .standard()
    .withRegion(region)
    .withCredentials(credentials)
    .build()

  def createTable(TableName: String, key: String): Unit = {

    val dynamoDb = new DynamoDB(defaultClient)

      if (checkTable(TableName)) {
        val tableToDelete = dynamoDb.getTable(TableName)
        tableToDelete.delete()
        tableToDelete.waitForDelete()
      }

    val createTableRequest = new CreateTableRequest()
        .withTableName(TableName)
        .withAttributeDefinitions(new AttributeDefinition(key, "N"))
        .withKeySchema(new KeySchemaElement(key, "HASH"))
        .withProvisionedThroughput(new ProvisionedThroughput(10L, 10L))
    val tableToCreate  = dynamoDb.createTable(createTableRequest)
    tableToCreate.waitForActive()

  }
  private def getAWSCredentails() = {
    val awsCredentials = new AWSCredentialsProviderChain(new EnvironmentVariableCredentialsProvider(), new ProfileCredentialsProvider())
    awsCredentials
  }

  def checkTable(tableName: String)(implicit defaultClient: AmazonDynamoDB): Boolean = {
    val r = defaultClient.listTables()
    r.getTableNames.contains(tableName)
  }

  def deleteTable(tableName: String)(implicit defaultClient: AmazonDynamoDB) =
    defaultClient.deleteTable(tableName)

}
