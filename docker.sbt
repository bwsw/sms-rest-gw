dockerBaseImage := "openjdk:8"

maintainer := "BITWORKS"

dockerExposedPorts := Seq(9000)
defaultLinuxInstallLocation := "/opt/sms-rest-gw"

dockerRepository := Some("bwsw")