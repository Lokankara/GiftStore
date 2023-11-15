#!/bin/bash

# Base URL
BASE_URL="http://localhost:8080/api"

# Set variables
id="1"
ids="1,2,3"
userId="1"

###
# Tags
###

# Retrieves all tags
curl $BASE_URL/tags

# Adds a new tag
curl -X POST -H "Content-Type: application/json" -d '{
    "name": "NewTag"
}' $BASE_URL/tags

# Retrieves a tag by ID
curl $BASE_URL/tags/$id

# Deletes a tag by ID
curl -X DELETE $BASE_URL/tags/$id

###
# Certificates
###

# Retrieves all certificates
curl $BASE_URL/certificates

# Adds a new certificate
curl -X POST -H "Content-Type: application/json" -d '{
    "name": "NewCertificate",
    "description": "A new certificate."
}' $BASE_URL/certificates

# Retrieves all certificates
curl $BASE_URL/certificates/

# Retrieves a certificate by ID
curl $BASE_URL/certificates/$id

# Updates a certificate by ID
curl -X PATCH -H "Content-Type: application/json" -d '{
    "name": "UpdatedCertificate",
    "description": "An updated certificate."
}' $BASE_URL/certificates/$id

# Deletes a certificate by ID
curl -X DELETE $BASE_URL/certificates/$id

# Retrieves tags of a certificate by ID
curl $BASE_URL/certificates/$id/tags

###
# Users
###

# Retrieves all users
curl $BASE_URL/users

# Retrieves a user by ID
curl $BASE_URL/users/$id

# Retrieves certificates of a user by ID
curl $BASE_URL/users/$id/certificates

###
# Orders
###

# Retrieves all orders
curl $BASE_URL/orders

# Retrieves certificates of an order by ID
curl $BASE_URL/orders/certificates/$id

# Retrieves an order by ID
curl $BASE_URL/orders/$id

# Retrieves orders of a user by ID
curl $BASE_URL/orders/$userId

# Creates an order for a user by ID
curl -X POST $BASE_URL/orders/$userId

# Retrieves all certificates
curl $BASE_URL/certificates

# Adds a new certificate
curl -X POST -H "Content-Type: application/json" -d '{
    "name": "NewCertificate",
    "description": "A new certificate."
}' $BASE_URL/certificates

# Retrieves all certificates
curl $BASE_URL/certificates/

# Retrieves certificates by IDs
curl $BASE_URL/certificates/$ids

# Retrieves a certificate by ID
curl $BASE_URL/certificates/$id

# Updates a certificate by ID
curl -X PATCH -H "Content-Type: application/json" -d '{
    "name": "UpdatedCertificate",
    "description": "An updated certificate."
}' $BASE_URL/certificates/$id

# Deletes a certificate by ID
curl -X DELETE $BASE_URL/certificates/$id

# Retrieves tags of a certificate by ID
curl $BASE_URL/certificates/$id/tags

# Retrieves all tags
curl $BASE_URL/tags

# Adds a new tag
curl -X POST -H "Content-Type: application/json" -d '{
    "name": "NewTag"
}' $BASE_URL/tags

# Retrieves a tag by ID
curl $BASE_URL/tags/$id

# Deletes a tag by ID
curl -X DELETE $BASE_URL/tags/$id

# Retrieves all users
curl $BASE_URL/users

# Retrieves a user by ID
curl $BASE_URL/users/$id

# Retrieves all tags
curl $BASE_URL/tags

# Adds a new tag
curl -X POST -H "Content-Type: application/json" -d '{
    "name": "NewTag"
}' $BASE_URL/tags

# Retrieves a tag by ID
curl $BASE_URL/tags/$id

# Deletes a tag by ID
curl -X DELETE $BASE_URL/tags/$id

# Retrieves all orders
curl $BASE_URL/orders

# Retrieves orders of a user by ID
curl $BASE_URL/orders/users/$userId

# Retrieves an order by ID
curl $BASE_URL/orders/$id

# Creates an order for a user by ID
curl -X POST $BASE_URL/orders/$userId

# Retrieves all certificates
curl $BASE_URL/certificates

# Adds a new certificate
curl -X POST -H "Content-Type: application/json" -d '{
    "name": "NewCertificate",
    "description": "A new certificate."
}' $BASE_URL/certificates

# Updates a certificate by ID
curl -X PATCH -H "Content-Type: application/json" -d '{
    "name": "UpdatedCertificate",
    "description": "An updated certificate."
}' $BASE_URL/certificates

# Retrieves all certificates
curl $BASE_URL/certificates/

# Retrieves certificates by order IDs
curl $BASE_URL/certificates/orders/$ids

# Retrieves certificates by user ID
curl $BASE_URL/certificates/users/$id

# Retrieves a certificate by ID
curl $BASE_URL/certificates/$id

# Deletes a certificate by ID
curl -X DELETE $BASE_URL/certificates/$id

# Retrieves tags of a certificate by ID
curl $BASE_URL/certificates/$id/tags

# Retrieves all users
curl $BASE_URL/users

# Retrieves a user by ID
curl $BASE_URL/users/$id

# Retrieves all tags
curl $BASE_URL/tags

# Adds a new tag
curl -X POST -H "Content-Type: application/json" -d '{
    "name": "NewTag"
}' $BASE_URL/tags

# Retrieves a tag by ID
curl $BASE_URL/tags/$id

# Deletes a tag by ID
curl -X DELETE $BASE_URL/tags/$id

# Retrieves all orders
curl $BASE_URL/orders

# Retrieves orders of a user by ID
curl $BASE_URL/orders/users/$userId

# Retrieves an order by ID
curl $BASE_URL/orders/$id

# Creates an order for a user by ID
curl -X POST $BASE_URL/orders/$userId

# Retrieves all certificates
curl $BASE_URL/certificates

# Adds a new certificate
curl -X POST -H "Content-Type: application/json" -d '{
    "name": "NewCertificate",
    "description": "A new certificate."
}' $BASE_URL/certificates

# Updates a certificate by ID
curl -X PATCH -H "Content-Type: application/json" -d '{
    "name": "UpdatedCertificate",
    "description": "An updated certificate."
}' $BASE_URL/certificates

# Retrieves all certificates
curl $BASE_URL/certificates/

# Retrieves certificates by order IDs
curl $BASE_URL/certificates/orders/$ids

# Retrieves certificates by user ID
curl $BASE_URL/certificates/users/$id

# Retrieves a certificate by ID
curl $BASE_URL/certificates/$id

# Deletes a certificate by ID
curl -X DELETE $BASE_URL/certificates/$id

# Retrieves tags of a certificate by ID
curl $BASE_URL/certificates/$id/tags

# Retrieves all users
curl $BASE_URL/users

# Retrieves a user by ID
curl $BASE_URL/users/$id

# Retrieves all tags
curl $BASE_URL/tags

# Adds a new tag
curl -X POST -H "Content-Type: application/json" -d '{
    "name": "NewTag"
}' $BASE_URL/tags

# Retrieves a tag by ID
curl $BASE_URL/tags/$id

# Deletes a tag by ID
curl -X DELETE $BASE_URL/tags/$id

# Retrieves all orders
curl $BASE_URL/orders

# Retrieves orders of a user by ID
curl $BASE_URL/orders/users/$userId

# Retrieves an order by ID
curl $BASE_URL/orders/$id

# Creates an order for a user by ID
curl -X POST $BASE_URL/orders/$userId

# Retrieves all certificates
curl $BASE_URL/certificates

# Adds a new certificate
curl -X POST -H "Content-Type: application/json" -d '{
    "name": "NewCertificate",
    "description": "A new certificate."
}' $BASE_URL/certificates

# Updates a certificate by ID
curl -X PATCH -H "Content-Type: application/json" -d '{
    "name": "UpdatedCertificate",
    "description": "An updated certificate."
}' $BASE_URL/certificates

# Retrieves all certificates
curl $BASE_URL/certificates/

# Retrieves certificates by order IDs
curl $BASE_URL/certificates/orders/$ids

# Retrieves certificates by user ID
curl $BASE_URL/certificates/users/$id

# Retrieves a certificate by ID
curl $BASE_URL/certificates/$id

# Deletes a certificate by ID
curl -X DELETE $BASE_URL/certificates/$id

# Retrieves tags of a certificate by ID
curl $BASE_URL/certificates/$id/tags

# Retrieves all users
curl $BASE_URL/users

# Retrieves a user by ID
curl $BASE_URL/users/$id

# Retrieves all tags
curl $BASE_URL/tags

# Adds a new tag
curl -X POST -H "Content-Type: application/json" -d '{
    "name": "NewTag"
}' $BASE_URL/tags

# Retrieves a tag by ID
curl $BASE_URL/tags/$id

# Deletes a tag by ID
curl -X DELETE $BASE_URL/tags/$id

# Retrieves all orders
curl $BASE_URL/orders

# Retrieves orders of a user by ID
curl $BASE_URL/orders/users/$userId

# Retrieves an order by ID
curl $BASE_URL/orders/$id

# Creates an order for a user by ID
curl -X POST $BASE_URL/orders/$userId

# Retrieves all certificates
curl $BASE_URL/certificates

# Adds a new certificate
curl -X POST -H "Content-Type: application/json" -d '{
    "name": "NewCertificate",
    "description": "A new certificate."
}' $BASE_URL/certificates

# Updates a certificate by ID
curl -X PATCH -H "Content-Type: application/json" -d '{
    "name": "UpdatedCertificate",
    "description": "An updated certificate."
}' $BASE_URL/certificates

# Retrieves all certificates
curl $BASE_URL/certificates/

# Retrieves certificates by order IDs
curl $BASE_URL/certificates/orders/$ids

# Retrieves certificates by user ID
curl $BASE_URL/certificates/users/$id

# Retrieves a certificate by ID
curl $BASE_URL/certificates/$id

# Deletes a certificate by ID
curl -X DELETE $BASE_URL/certificates/$id

# Retrieves tags of a certificate by ID
curl $BASE_URL/certificates/$id/tags
