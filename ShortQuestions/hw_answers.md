# March 27th Assignment

## Question 1
- Developer API: It represents Internal/Library API. These are the contract and interfaces defined within the code to allow different components of a software system to communicate. Such as Java Collections Framework and Functional Interfaces, they provide a set of tools and rules that a programmer uses to build an application. Their purpose is to enable code resuability, extensibility, and type safety. 
    - Developer API inteaction occurs through Java syntax, including method calls, parameters, and return types. Developers use tools like Interfaces to define what a class "can do" and Abstract Classes to share common behavior.

- User API: It represents External/REST API. These are the public-facing endpoints exposed by an application (typically via HTTP) that allow external system or frontend cilents to interact with the service.
    - User API interaction occurs through standard web protocols, specifically HTTP methods and URL paths.

## Question 2
A REST API is composed of serval key elements that allow a cilent to interact with a server-side application. The following are the primary components:
- HTTP Methods: [GET, POST, PUT, DELETE, OPTIOAN]; These methods define the specific action the cilent wants to perform on a resource.
- Endpoints and URL Paths: An endpoint is a specific URL Path that reprensents a resource. For example, a blog application might use `/api/v1/post` as the base path for mapping post resources.
- HTTP Status Codes: Status codes are standardied responses sent by the server to inform the client of th request's outcome. Such as `200`, `404`, and `500`, they represent differnt status of the outcome from server.
- Request Body: It contains the data sent by the client to the server, while the Response Body contains the data returned by the server to client. In mordern REST APIs, these bodies are typically formatted as JSON.
- Controller: In a Spring Boot environment, the Controller is the component responsible for defining the API endpoints. It uses annotations like `@RestController` to mark the class as a REST handler.
- Data Transfer Object (DTOs): DTOs are simple Java classes used to transfer data between the client and the server's internal business logic. They are act as a contract for the API, allowing the developer to expose only specific fields to the user while keeping the internal database Entities hidden.
- Response Wrappers: A `ResponseWrappers` is often used in the controller to wrap the response data. It provides full control over the response, allowing the developer to specify both the body and the HTTP status code sent back to the client.

## Question 3
- GET: Read action; It retrieves existing data from the server, it shows `200 OK` as success. It can be used to get a list of all resources or a specific resource by its ID.
- POST: Create action; It shubmits new data to the server to create a resource. It shows `201 Created` as success. It typically requires a Request Body containing the data for the new resource.
- PUT: Update action; It modifies an existing resources identified by a specific ID. It shows `200 OK` as success. It requires both a Path Variable and a Resquest Body.
- DELETE: Delete action; It removes a specific resource from the server. It also shows `200 OK` as success. It typically requires a Path Variable to specify which record to remove.

## Question 4
The Authentication field is identified as a critical component of the Presentation Layer within the Spring Boot flow architecture.
- Architectural Location: In a standard Spring Boot project, authentication resides in the Presentation Layer, which is the first layer to intercept an incoming HTTPS request from a client.
- Request Handling: It acts as a gatekeeper alongside JSON translation. Before a request can reach the Business Layer to execute logic or hte Persistence Layer to access the database, the Presentation Layer must verify the user's identify through this authentication process.

## Question 5
Cookies are managed through two speecific HTTP header fields:
1. `set-Cookie` (Response Header):
    - Origin: Sent by the server to the client.
    - Purpose: Tells the browser to store a piece of data.
    - It has several attributes: Expires, HttpOnly, and Secure.
2. `Cookie` (Request Header):
    - Origin: Sent by the client back to the server.
    - Purpose: To return previously stored data to the server, allowing the server to recognize the user or maintain a "state".
    - Whenever a browser makes a request to server, it checks its local storage for any cookies that server's domain and path. If a match is found, it automatically includes them in the `Cookie` header.

## Question 6
- Purpose of HTTP Response Headers: They serve as metadata that accompanies the data sent from the server to the client. Their primary purpose within a Spring Boot application include:
    - Instruction for the Presentation Layer: The Presentation Layer as the first and last point of contact for an HTTPS request. Response headers tell the client how to interpret the daata being sent back.
    - State and Context: Headers communicate essential information that is not part of the primary data.
    - Protocol Management: They manage details like JSON Translation and Authentication results.

- Why They Are Necessary
    - Headers are used to transmit the HTTP Status Code. Without this, a client wouldn't know if a request was successful, if a new resource was sucessfully generated, or if an error occured.
    - In Spring Boot development, as shown in the sources, developers use the `ResponseEntity` object to explicitly wrap both the data and the status code. This is necessary to provide a complete, standards-compliant reponse that the client's architecture can handle.
    - Since Authentication and Authorization happen at the Presentation and Business layer, headers are necessary to pass tokens or session information.
    - When an expection occurs, the server uses headers to set the appropriate error status, ensuring the client recieves a structured error response rather than just a broken connection.
