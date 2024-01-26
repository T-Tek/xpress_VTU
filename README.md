**XpressVTU Merchant API**

Welcome to the XpressVTU Merchant API! This API provides simple and secure endpoints for merchant registration, login, and airtime payment functionalities.

**Endpoints**
Merchant Registration

Endpoint: POST /api/merchants/register
Request Body:
{
  "name": "merchant",
  "email": "merchant@example.com",
  "phoneNumber" : "080xxxxxxxxxx",
  "password": "password123",
  "role" : "userxxxxx"
}


Merchant Login

Endpoint: POST /api/merchants/login
Request Body:
{
  "email": "merchant@example.com",
  "password": "password123"
}

Response:
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
Airtime Payment

Endpoint: POST /api/payment/makePayment
Request Body:
{
  "requestId": "12362",
  "uniqueCode": "MTN_24207",
  "details": {
      "phoneNumber": "09132058051",
      "amount": 100
  }
}
Response:
{
  // Airtime payment response details...
}

Exception Handling
The API handles various exceptions, such as incorrect passwords, merchant not found, and payment service errors.

