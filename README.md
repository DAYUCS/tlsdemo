### Create Key ###
```
#Create folders to generate all files (separated for client and server)
mkdir ssl && cd ssl && mkdir client && mkdir server

## Server
# Generate server private key and self-signed certificate in one step
openssl req -x509 -newkey rsa:4096 -keyout server/serverPrivateKey.pem -out server/server.crt -days 3650 -nodes
# Create PKCS12 keystore containing private key and related self-sign certificate
openssl pkcs12 -export -out server/keyStore.p12 -inkey server/serverPrivateKey.pem -in server/server.crt
# Generate server trust store from server certificate 
keytool -import -trustcacerts -alias root -file server/server.crt -keystore server/trustStore.jks

## Client
# Generate client's private key and a certificate signing request (CSR)
openssl req -new -newkey rsa:4096 -out client/request.csr -keyout client/myPrivateKey.pem -nodes

## Server
# Sign client's CSR with server private key and a related certificate
openssl x509 -req -days 360 -in client/request.csr -CA server/server.crt -CAkey server/serverPrivateKey.pem -CAcreateserial -out client/pavel.crt -sha256

## Client
# Verify client's certificate
openssl x509 -text -noout -in client/pavel.crt
# Create PKCS12 keystore containing client's private key and related self-sign certificate 
openssl pkcs12 -export -out client/client_pavel.p12 -inkey client/myPrivateKey.pem -in client/pavel.crt -certfile server/myCertificate.crt
```
### Put Key under /resources/keystore ###
    keyStore.p12 -- Server's key
    trustStore.jks -- CA
### Configue in application.properties ###
```
server.port=8443
server.servlet.context-path=/
# The format used for the keystore
server.ssl.key-store-type=PKCS12
# The path to the keystore containing the certificate
server.ssl.key-store=classpath:keystore/keyStore.p12
# The password used to generate the certificate
server.ssl.key-store-password=123456
# Trust store that holds SSL certificates.
server.ssl.trust-store=classpath:keystore/trustStore.jks
# Password used to access the trust store.
server.ssl.trust-store-password=123456
# Type of the trust store.
server.ssl.trust-store-type=JKS
# Whether client authentication is wanted ("want") or needed ("need").
server.ssl.client-auth=need
```