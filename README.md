Overview
The Ticket Service API is a Java-based RESTful service designed to manage train ticket bookings for a journey from London to France. The API allows users to purchase tickets, view their receipts, manage seat allocations, and modify or remove bookings. This service is built using Spring Boot and follows best practices for REST API development, including validation, pagination, rate limiting, and concurrency control.


Features

Ticket Purchase: Submit a purchase request for a train ticket from London to France.

Receipt Viewing: Retrieve the details of a purchased ticket, including user information, destination, and seat allocation.

User and Seat Management: View the list of users and their allocated seats, remove users, and modify seat assignments.

Concurrency Handling: Thread-safe operations to ensure data integrity.

Rate Limiting: Prevent abuse by limiting the number of requests per second.

Pagination: Efficiently handle large datasets with paginated results.

API Versioning : v1 is used for the current TicketService

Technologies Used

Java 17
Spring Boot 3.x
Spring Web
Spring Validation
Lombok
Guava (Rate Limiter)
Maven

Getting Started

Prerequisites

Java 17 or higher
Maven 3.6.x or higher
An IDE like IntelliJ IDEA or Eclipse

Installation
Clone the repository:

bash
Copy code
git clone https://github.com/yourusername/ticket-service-api.git
cd ticket-service-api
Build the project:

bash
Copy code
mvn clean install
Run the application:

bash
Copy code
mvn spring-boot:run
The application will be accessible at http://localhost:8080.

API Endpoints

NOTE: Multiple ticket booking scenerio for the same user(email) is  not handled as this was not in the requirement .
It can be handled following the logic of Modify, Update and Create a new entry in the map against the email id
Restriction has been added to avoid creation of a multiple ticket by same user email as part of the requirement

1 Purchase a Ticket

   URL: /v1/api/tickets/purchase
   
   Method: POST
   
   Request Body:
   
   json
   
   {
   "firstName": "aman",
   "lastName": "agarwal",
   "email": "amancse@example.com"
   }
   Response:{
   "from": "London",
   "to": "France",
   "user": {
   "firstName": "aman",
   "lastName": "agarwal",
   "email": "amancse@example.com"
   },
   "price": 20.0,
   "seatNumber": "B2"
   }
   
   Status 201 Created with the ticket details.

   
NOTE: Assuming Each user will only book 1 ticket as per functional requirement . 
So it will a single Ticket Object and not List<Ticket> in the response .

2.View Receipt by Email

   URL: http://localhost:8080/v1/api/tickets/{email}
   
   Method: GET
   
   Response:
   
   json
   
{
"from": "London",
"to": "France",
"user": {
"firstName": "aman",
"lastName": "agarwal",
"email": "amancse2012@example.com"
},
"price": 20.0,
"seatNumber": "A1"
}


3. View Users and Seats by Section
   
   URL: http://localhost:8080/v1/api/tickets/seats?section=A
   
   Method: GET
   
   Request Params:
   
   section: The train section (e.g., A or B).
   
   page: Page number for pagination (optional).
   
   size: Page size for pagination (optional).
   
   Response:
   
   json
   
   {
   "A1": "amancse2012@example.com"
   }

   
5. Remove a User
   
   URL: /v1/api/tickets/{email}
   
   Method: DELETE
   
   Response: Status 204 No Content

   
7. Modify Seat Assignment
   URL: /v1/api/tickets/modify-seat/{email}
   Method: PUT
   Request Params:
   newSeat: The new seat assignment.
   Response: Status 204 No Content
