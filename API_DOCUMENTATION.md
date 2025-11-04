# API Documentation - AI-Powered DSA Learning Portal

This document provides comprehensive API documentation for the AI-Powered DSA Learning Portal backend.

## Base URL
```
http://localhost:8080/api
```

## Authentication

The API uses JWT (JSON Web Token) for authentication. Include the token in the Authorization header:

```
Authorization: Bearer <your-jwt-token>
```

## Response Format

All API responses follow a consistent format:

### Success Response
```json
{
  "data": <response_data>,
  "message": "Success",
  "status": 200
}
```

### Error Response
```json
{
  "error": "Error message",
  "status": 400
}
```

## Endpoints

### Authentication

#### Register User
```http
POST /auth/register
```

**Request Body:**
```json
{
  "username": "string",
  "email": "string",
  "password": "string"
}
```

**Response:**
```json
{
  "token": "jwt-token",
  "type": "Bearer",
  "id": 1,
  "username": "string",
  "email": "string",
  "role": "USER"
}
```

#### Login User
```http
POST /auth/login
```

**Request Body:**
```json
{
  "username": "string",
  "password": "string"
}
```

**Response:**
```json
{
  "token": "jwt-token",
  "type": "Bearer",
  "id": 1,
  "username": "string",
  "email": "string",
  "role": "USER"
}
```

### Problems

#### Get All Problems
```http
GET /problems
```

**Query Parameters:**
- `difficulty` (optional): EASY, MEDIUM, HARD
- `topic` (optional): ARRAYS, STRINGS, TREES, etc.
- `search` (optional): Search term

**Example:**
```http
GET /problems?difficulty=EASY&topic=ARRAYS&search=sum
```

**Response:**
```json
[
  {
    "id": 1,
    "title": "Two Sum",
    "description": "Given an array of integers...",
    "difficulty": "EASY",
    "topic": "ARRAYS",
    "inputFormat": "First line contains n...",
    "outputFormat": "Print two space-separated indices",
    "constraints": "2 ≤ n ≤ 10^4...",
    "timeLimit": 1000,
    "memoryLimit": 256,
    "createdAt": "2024-01-01T00:00:00",
    "updatedAt": "2024-01-01T00:00:00",
    "testCases": [
      {
        "id": 1,
        "inputData": "4\n2 7 11 15\n9",
        "expectedOutput": "0 1",
        "isSample": true
      }
    ]
  }
]
```

#### Get Problem by ID
```http
GET /problems/{id}
```

**Response:**
```json
{
  "id": 1,
  "title": "Two Sum",
  "description": "Given an array of integers...",
  "difficulty": "EASY",
  "topic": "ARRAYS",
  "inputFormat": "First line contains n...",
  "outputFormat": "Print two space-separated indices",
  "constraints": "2 ≤ n ≤ 10^4...",
  "timeLimit": 1000,
  "memoryLimit": 256,
  "createdAt": "2024-01-01T00:00:00",
  "updatedAt": "2024-01-01T00:00:00",
  "testCases": [...]
}
```

#### Create Problem (Admin Only)
```http
POST /problems
```

**Request Body:**
```json
{
  "title": "string",
  "description": "string",
  "difficulty": "EASY|MEDIUM|HARD",
  "topic": "ARRAYS|STRINGS|TREES|...",
  "inputFormat": "string",
  "outputFormat": "string",
  "constraints": "string",
  "timeLimit": 1000,
  "memoryLimit": 256
}
```

#### Update Problem (Admin Only)
```http
PUT /problems/{id}
```

**Request Body:** Same as create problem

#### Delete Problem (Admin Only)
```http
DELETE /problems/{id}
```

#### Get Solved Problems
```http
GET /problems/solved/{userId}
```

#### Get Unsolved Problems
```http
GET /problems/unsolved/{userId}
```

### Submissions

#### Get User Submissions
```http
GET /submissions/user/{userId}
```

**Response:**
```json
[
  {
    "id": 1,
    "userId": 1,
    "problemId": 1,
    "problemTitle": "Two Sum",
    "code": "def solution():\n    pass",
    "language": "PYTHON",
    "status": "ACCEPTED",
    "timeTaken": 150,
    "memoryUsed": 1024,
    "testCasesPassed": 5,
    "totalTestCases": 5,
    "accuracy": 100.0,
    "errorMessage": null,
    "submittedAt": "2024-01-01T00:00:00"
  }
]
```

#### Get Problem Submissions
```http
GET /submissions/problem/{problemId}
```

#### Get User-Problem Submissions
```http
GET /submissions/user/{userId}/problem/{problemId}
```

#### Get Submission by ID
```http
GET /submissions/{id}
```

#### Submit Code
```http
POST /submissions
```

**Query Parameters:**
- `userId`: User ID
- `problemId`: Problem ID
- `code`: Source code
- `language`: Programming language (PYTHON, JAVA, CPP, JAVASCRIPT, C)

**Example:**
```http
POST /submissions?userId=1&problemId=1&code=def solution():&language=PYTHON
```

**Response:**
```json
{
  "id": 1,
  "userId": 1,
  "problemId": 1,
  "problemTitle": "Two Sum",
  "code": "def solution():",
  "language": "PYTHON",
  "status": "PENDING",
  "submittedAt": "2024-01-01T00:00:00"
}
```

#### Update Submission Result
```http
PUT /submissions/{id}/result
```

**Response:**
```json
{
  "id": 1,
  "userId": 1,
  "problemId": 1,
  "problemTitle": "Two Sum",
  "code": "def solution():",
  "language": "PYTHON",
  "status": "ACCEPTED",
  "timeTaken": 150,
  "memoryUsed": 1024,
  "testCasesPassed": 5,
  "totalTestCases": 5,
  "accuracy": 100.0,
  "submittedAt": "2024-01-01T00:00:00"
}
```

#### Get User Statistics
```http
GET /submissions/stats/user/{userId}
```

**Response:**
```json
{
  "totalSubmissions": 25,
  "acceptedSubmissions": 20,
  "averageAccuracy": 85.5
}
```

### Dashboard

#### Get Dashboard Statistics
```http
GET /dashboard/{userId}
```

**Response:**
```json
{
  "totalProblems": 100,
  "solvedProblems": 25,
  "totalSubmissions": 50,
  "overallAccuracy": 85.5,
  "accuracyByTopic": {
    "ARRAYS": 90.0,
    "STRINGS": 80.0,
    "TREES": 75.0,
    "GRAPHS": 60.0
  },
  "recommendedProblems": [
    {
      "id": 1,
      "title": "Two Sum",
      "description": "Given an array of integers...",
      "difficulty": "EASY",
      "topic": "ARRAYS"
    }
  ],
  "recentSubmissions": [
    {
      "id": 1,
      "problemTitle": "Two Sum",
      "language": "PYTHON",
      "status": "ACCEPTED",
      "submittedAt": "2024-01-01T00:00:00"
    }
  ]
}
```

#### Get AI Recommendations
```http
GET /dashboard/recommendations/{userId}
```

**Response:**
```json
[
  {
    "id": 1,
    "title": "Two Sum",
    "description": "Given an array of integers...",
    "difficulty": "EASY",
    "topic": "ARRAYS"
  }
]
```

## Error Codes

| Code | Description |
|------|-------------|
| 200 | Success |
| 400 | Bad Request |
| 401 | Unauthorized |
| 403 | Forbidden |
| 404 | Not Found |
| 500 | Internal Server Error |

## Data Models

### User
```json
{
  "id": "number",
  "username": "string",
  "email": "string",
  "role": "USER|ADMIN",
  "createdAt": "datetime",
  "lastLogin": "datetime"
}
```

### Problem
```json
{
  "id": "number",
  "title": "string",
  "description": "string",
  "difficulty": "EASY|MEDIUM|HARD",
  "topic": "ARRAYS|STRINGS|TREES|GRAPHS|DYNAMIC_PROGRAMMING|GREEDY|SORTING|SEARCHING|MATH|HASH_TABLE|STACK|QUEUE|LINKED_LIST|BINARY_TREE|HEAP",
  "inputFormat": "string",
  "outputFormat": "string",
  "constraints": "string",
  "timeLimit": "number",
  "memoryLimit": "number",
  "createdAt": "datetime",
  "updatedAt": "datetime",
  "testCases": "TestCase[]"
}
```

### TestCase
```json
{
  "id": "number",
  "inputData": "string",
  "expectedOutput": "string",
  "isSample": "boolean"
}
```

### Submission
```json
{
  "id": "number",
  "userId": "number",
  "problemId": "number",
  "problemTitle": "string",
  "code": "string",
  "language": "PYTHON|JAVA|CPP|JAVASCRIPT|C",
  "status": "PENDING|ACCEPTED|WRONG_ANSWER|TIME_LIMIT_EXCEEDED|MEMORY_LIMIT_EXCEEDED|RUNTIME_ERROR|COMPILATION_ERROR",
  "timeTaken": "number",
  "memoryUsed": "number",
  "testCasesPassed": "number",
  "totalTestCases": "number",
  "accuracy": "number",
  "errorMessage": "string",
  "submittedAt": "datetime",
  "judge0Token": "string"
}
```

## Rate Limiting

The API implements rate limiting to prevent abuse:
- **Authentication endpoints**: 5 requests per minute per IP
- **Submission endpoints**: 10 requests per minute per user
- **Other endpoints**: 100 requests per minute per user

## CORS

The API supports Cross-Origin Resource Sharing (CORS) with the following configuration:
- **Allowed Origins**: http://localhost:3000
- **Allowed Methods**: GET, POST, PUT, DELETE, OPTIONS
- **Allowed Headers**: *
- **Allow Credentials**: true

## Examples

### Complete Workflow Example

1. **Register a new user:**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123"
  }'
```

2. **Login:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'
```

3. **Get problems:**
```bash
curl -X GET "http://localhost:8080/api/problems?difficulty=EASY" \
  -H "Authorization: Bearer <your-jwt-token>"
```

4. **Submit code:**
```bash
curl -X POST "http://localhost:8080/api/submissions?userId=1&problemId=1&code=def solution():&language=PYTHON" \
  -H "Authorization: Bearer <your-jwt-token>"
```

5. **Get dashboard stats:**
```bash
curl -X GET http://localhost:8080/api/dashboard/1 \
  -H "Authorization: Bearer <your-jwt-token>"
```

## Testing

### Using Postman
1. Import the API collection (if available)
2. Set the base URL to `http://localhost:8080/api`
3. Set up authentication using the Bearer token
4. Test each endpoint

### Using curl
See the examples above for curl commands.

### Using JavaScript/Fetch
```javascript
// Example: Get problems
const response = await fetch('http://localhost:8080/api/problems', {
  headers: {
    'Authorization': 'Bearer ' + token,
    'Content-Type': 'application/json'
  }
});
const problems = await response.json();
```

## Support

For API support or questions:
- Create an issue in the repository
- Check the troubleshooting guide
- Review the error messages and status codes

---

**Last Updated**: January 2024
**API Version**: 1.0.0
