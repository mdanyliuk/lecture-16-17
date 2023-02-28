To run project: <br>
`git clone https://github.com/mdanyliuk/lecture-16-17.git` <br>
`cd lecture-16-17` <br>
`./mvnw spring-boot:run` <br>
(or you can run main method of PepApplication.class from your IDE) <br>
<br>
### Endpoints
POST `http://localhost:8080/api/pep/upload` upload zip-archive with initial data<br>
GET `http://localhost:8080/api/pep/topNames` get top ten names of puplic persons<br>
POST `http://localhost:8080/api/pep/_search` search persons by first or/and last names<br>
example of correct request body: <br>
`{
"firstName": "Олександр",
"lastName": "Волков"
}` <br>
`{
"firstName": "Олександр"
}` <br>
`{
"lastName": "Волков"
}` <br>
