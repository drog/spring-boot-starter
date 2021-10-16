# spring boot starter
Starter project which contains examples with:
* Basic Rest CRUD examples
* Calling external api with RestTemplate
* PostgreSQL
* Coverage with jacoco > 80% lines covered required
* Swagger
* headers and spring boot filters
* jpa auditing

# Swagger documentation
- http://localhost:8080/swagger-ui/index.html
- put https://raw.githubusercontent.com/drog/spring-boot-starter/master/docs/swagger-api-docs.json in https://editor.swagger.io/

# Pre requisites
- java 11
- docker
- docker-compose

# Start
- Start local postgreSQL, redis and mmock with `docker-compose up`

# Basic Diagram
![Basic Diagram](./docs/basic_diagram.png)

# TODO
- fix test with redis in redis-implementation branch

# External links
- Monster Mock https://github.com/jmartin82/mmock
- https://stackoverflow.blog/2020/03/02/best-practices-for-rest-api-design/
- Jpa auditing https://springbootdev.com/2018/03/13/spring-data-jpa-auditing-with-createdby-createddate-lastmodifiedby-and-lastmodifieddate/
- Swagger headers https://stackoverflow.com/questions/40801442/add-a-header-parameter-in-swagger-ui-documentation-with-springfox