SMS REST Gateway
=================================================

**SMS REST Gateway** is the REST API based on [Play Framework](https://github.com/playframework/playframework) for sending SMS 
using different services such as Plivo, Twilio, etc. with LDAP authentication.
### Table of Contents

- [Deployment](#deployment)
    - [Deploy with Docker](#docker-inst)
    - [Deploy with SBT](#sbt-inst)
- [Configuration](#configuration)
- [API](#api)
    - [POST /auth](#auth)
    - [POST /send](#send)
    - [GET /logs](#logs)
- [Extension](#extension)

# Deployment


## Deploy with Docker

This deployment uses Docker for all the things. It requires [Docker Engine](https://docs.docker.com/engine). <br />
See the [installation instructions](https://docs.docker.com/engine/installation/) for your environment.

#### Launch
Run container using command:
```bash
docker run --name <CONTAINER_NAME> [-e ENV1, -e ENV2, ...] [-p <HOST_MACHINE_PORT>:9000] bwsw/sms-rest-gw
```
To configure app with some environment variables see [configuration section](#configuration).


## Deploy with SBT

This deployment requires both Java 8 and SBT. If you don't have one of them, you have to install from:
- Java: [Oracle JDK](http://www.oracle.com/technetwork/java/javase/downloads/index.html) or [OpenJDK](http://openjdk.java.net/install/)
- [SBT install page](http://www.scala-sbt.org/download.html)

#### Download
[Download the project code](https://github.com/bwsw/sms-rest-gw/archive/master.zip) from `master` branch or clone it using git:
```git
git clone https://github.com/bwsw/sms-rest-gw.git
```

#### Launch
Run command from the root of the project:
```bash
sbt start [-DVAR1=VAL1 -DVAR2=VAL2 ...]
```
To configure app with some environment variables see [configuration section](#configuration).


# Configuration
The following environment variables are honored for configuring app instance:

|Variable   |Configuration option  |Environment variable   |Type    |Default value   |Is optional |
|---|---|---|---|---|---|
|Application secret: uses to generate JSON Web Tokens|play.crypto.secret|APPLICATION_SECRET|String|"changeme"|No|
|Token lifetime: how many time session is active (in milliseconds)|play.http.session.maxAge|TOKEN_LIFETIME|Integer|1800000|Yes|
|LDAP authenticate server host|app.ldap.host|LDAP_HOST|String|"localhost"|Yes|
|LDAP authenticate server port|app.ldap.port|LDAP_PORT|Integer|389|Yes|
|SMS gateway service class|app.gateway.service|GATEWAY|String|"com.bwsw.utils.SmsGateway.PlivoSmsGateway"|Yes|
|SMS gateway service id (such as auth_id, username)|app.gateway.auth.id|GATEWAY_AUTHID|String|-|Yes|
|SMS gateway service token (such as auth_token, password)|app.gateway.auth.token|GATEWAY_AUTHTOKEN|String|-|Yes|


# API

## POST /auth

Performs authentication function using LDAP service

**Request parameters:**

_Headers:_

|Header|Value|Description|
|---|---|---|
|Content-Type|application/json|Expected request format|

_Body:_

|Parameter|Type|Description|
|---|---|---|
|username|String|LDAP user DN|
|password|String|LDAP user's password|

**Example:**
```json
{
  "username":"cn=admin,dc=example,dc=com",
  "password":"adm1npassw0rd"
}
```

**Success response:**

- Code: 200
- Content-Type: application/json
- Content: {"token":"<TOKEN_VALUE>"}

**Error response:**

- Code: 400
- Content-Type: application/json
- Content: {"error":"<ERROR_DESCRIPTION>"}

## POST /send

Performs SMS sending function using service defined in config file

**Request parameters:**

_Headers:_

|Header|Value|Description|
|---|---|---|
|Content-Type|application/json|Expected request format|
|token|<USER_TOKEN>|Token received in response body after successful authentication|

_Body:_

|Parameter|Type|Description|
|---|---|---|
|sender|String|Phone number that will be shown as the sender|
|destination|String|Number to which the message will be sent|
|text|String|Text message that will be sent|

**Example:**
```json
{
  "sender":"17891234567",
  "destination":"79991112233",
  "text":"Hello, how are you?"
}
```

**Success response:**

- Code: 200
- Content-Type: application/json
- Content: {"message":"<SEND_DETAILS>"}
<br />
`Header includes updated token`

**Error response:**

- Code: 400
- Content-Type: application/json
- Content: {"error":"<ERROR_DESCRIPTION>"}
<br />
`Header includes updated token`

OR

- Code: 401
- Content-Type: application/json
- Content: {"error":"Bad or expired token"}

## GET /logs

Returns list with sent messages log

**Request parameters:**

_Headers:_

|Header|Value|Description|
|---|---|---|
|token|<USER_TOKEN>|Token received in response body after successful authentication|

_URL parameters:_

|Parameter|Type|Default value|Description|
|---|---|---|---|
|from|Int|0|DateTime (in unix time format) used to filtering logs|
|to|Int|DateTime.now|DateTime (in unix time format) used to filtering logs|

**Example:**
```
/logs?from=750515100&to1488438000
```

**Success response:**

- Code: 200
- Content-Type: application/json
- Content: 
```
{"logs":
  [
    {
        "username":"<USERNAME>",
        "sender":"<SENDER>",
        "destination":"<DEST>",
        "message":"<MESSAGE>",
        "sendtime":<SENDTIME>
    },
    ...
  ]
}
 
```
`Header includes updated token`

**Error response:**

- Code: 400
- Content-Type: application/json
- Content: {"error":"<ERROR_DESCRIPTION>"}
<br />
`Header includes updated token`

OR

- Code: 401
- Content-Type: application/json
- Content: {"error":"Bad or expired token"}


#Extension

To add implementation with new message service:

1. Create new class that extends `SmsGateway` trait and implement `sendMessage` method;
2. Put your class into the `app` project directory;
3. Run application using SBT with env `GATEWAY` set to your `class name` (with package name). 