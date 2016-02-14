package aws.daleks.eager

import com.amazonaws.regions.Regions
import com.amazonaws.regions.Regions._
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider
import com.amazonaws.regions.Region
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain
import com.amazonaws.auth.profile.ProfileCredentialsProvider

object EagerAWSDaleks extends App{  
  println("EXTERMINATE!")
  
  def findArg(arg:String):Option[String] = {
   // val i = args.indexOf(s"-$arg") 
  //  if ( i >= 0)
  //    Option(args(i+1))
  //  else
    None
  }
  
  val profile = findArg("profile")
  
  implicit val credentials = profile match {
    case Some(prf) => new ProfileCredentialsProvider(prf)
    case None => new DefaultAWSCredentialsProviderChain
  }
  
  val excludedRegions = List(GovCloud,CN_NORTH_1)
  val regions = Regions.values diff excludedRegions
  
  println(s"Exterminating regions [${regions.mkString(",")}]")

  val globals = List(
    new EagerRoute53Dalek(),
    new EagerIAMDalek())

  val regionals = regions
    .map { Region.getRegion(_) }
    .par
    .foreach { implicit region =>
      println("Preparing extermination of region ["+region+"]")
      List(
        // missing: ECS
        // missing: Lambda
        // missing: CloudFront
        // missing: EFS
        // missing: Glacier
        // missing: RedShift
        // missing: DMS
        // missing: CodeCommit
        // missing: CodeDeploy
        // missing: CodePipeline
        // missing: CloudWatch
        // missing: CloudTrail
        // missing: Config
        // missing: OpsWorks
        // missing: ServiceCatalog
        // missing: ServiceCatalog
        // missing: Inspector
        // missing: WAF
        // missing: CertificateManager
        // missing: DataPipeline
        // missing: ElasticSearch
        // missing: Kinesis
        // missing: MachineLearning
        // missing: IoT
        // missing: GameLift
        // missing: MobileHub
        // missing: Cognito
        // missing: DeviceFarm
        // missing: MobileAnalytics
        // missing: APIGateway
        // missing: AppStream
        // missing: CloudSearch
        // missing: ElasticTranscoder
        // missing: SES
        // missing: SWF
        // missing: WorkSpaces
        // missing: WorkDocs
        // missing: WorkMail

        //new EagerDirectoryServiceDalek,
        new EagerBeanstalkDalek,
        new EagerCloudFormationDalek,
        new EagerDynamoDBDalek,        
        new EagerElastiCacheDalek,
        new EagerEMRDalek,
        new EagerRDSDalek,
        new EagerS3Dalek,
        new EagerSNSDalek,
        new EagerSQSDalek,
        new EagerEC2Dalek) foreach {_.exterminate}
    } 

  globals foreach { _.exterminate }

  println("EXTERMINATE!")

}

