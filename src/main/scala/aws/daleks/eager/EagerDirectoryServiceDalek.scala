package aws.daleks.eager

import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.regions.Region
import scala.collection.JavaConverters._
import com.amazonaws.services.directory.AWSDirectoryServiceClient
import com.amazonaws.services.directory.model._
import aws.daleks.util.Humid

class EagerDirectoryServiceDalek(implicit region: Region, credentials: AWSCredentialsProvider) extends Dalek {
  val ds = withRegion(new AWSDirectoryServiceClient(credentials),region)

  def exterminate = {
    val directories = ds.describeDirectories.getDirectoryDescriptions asScala 
    //TODO: include AWS internal account in exclusion list
    
    directories.foreach { dir =>
      try {
        info(this,"** Exterminating Directory " + dir.getDirectoryId)
        Humid {
          ds.deleteDirectory(new DeleteDirectoryRequest().withDirectoryId(dir.getDirectoryId()))
        }
      } catch {
        case e: Exception => println(s"! Failed to exterminate Directory Service ${dir.getDirectoryId}: ${e.getMessage()}")
      }
    }
  }
}