

1. Database installation :

-- install the oracle db threw liquibase plugin manually

configure in the pom.xml in liquibase-maven-plugin the database url and credentials
and then use the following command to install the database : 

mvn liquibase:update

-- if you want to delete the database use the following command :

mvn liquibase:dropAll


