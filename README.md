# Biu!
* Just for fun and feature demo.
* No support available, do NOT use it in production.
* Main class bglutil.main.Biu
* Setup static final CONFIG parameters in bglutil.conf.Config before compile.
* Must setup ~/.aws/credentials with profile name align with region name such as "virginia", "tokyo", "china", "beijing", "global", etc. Go through the source for their usage.
* Required libraries "AWS SDK for Java", "AWS KCL", "AWS KPL", "Apache Commons Lang", "protobuf", "guava", "commons-compress", "commons-lang", "slf4j".

# Setup
    1. Go through the source code of bglutil/main/Biu.java and provide missing values to certain static fields in bglutil/conf/Config.java.
    2. Build, export and run bglutil.main.Biu.
