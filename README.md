# Instagram API gallery

The project is built on Play 2.5 framework and uses postgresql database,to fetch Instagram images based on hashtag, by call the Instagram REST API.

Steps to make the app run:-

1. Install activator
2. Download the project and open in an IDE(IntelliJ,Eclipse)
3. Setup a database using postgresql.
4. open application.conf file to view the database properties:-

  default.driver=org.postgresql.Driver
  default.url="jdbc:postgres://utjiocmbwlmqxz:-r4dIYIE0AxXbqSrtVJIphTwZ9@ec2-54-83-56-177.compute-1.amazonaws.com:5432/dvfl0gddq4bem?ssl=  true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
  default.url=${?DATABASE_URL
  
  The url is a postgresql database provided by heroku.
5.Create a new Heroku proc file in the project root with the below properties:-

   web: target/universal/stage/bin/your_project_name -Dhttp.port=${PORT} -Dplay.evolutions.db.default.autoApply=true     -Ddb.default.driver=org.postgresql.Driver -Ddb.default.url=${DATABASE_URL}
   
6.Create a heroku account and git push the code.
7.Start the heroku server and open the app.

















  


