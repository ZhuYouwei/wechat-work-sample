



# WeChat Work Sample

This sample illustrates the scenario of sending a Welcome note to a new
external contact
 
## Authenticate API
One off authentication using corpid and corpsecret to retrieve access_token,
which will be used in the subsequent API calls
https://work.weixin.qq.com/api/doc/90000/90135/91039

## Creating a Message Receiving Server
This sample app include a Spring Boot microservices which provides an endpoint /corporate-customer-event for 
receiving callbacks.

## Subscribe to news and events
In order to allow two-way communication between self-built applications and enterprise WeChat, 
enterprises can enable the receive message mode in the application management background.
Enterprises that enable the message receiving mode need to provide the available message receiving server URL 
(https is recommended).

https://work.weixin.qq.com/api/doc/90000/90135/90237

- Login to [WeChat Work Admin - App Management]
- Follow these sequence to open ![App Management](/doc/images/wechatwork-app-mgmt.jpg "App Management")
- Click "Create an app"
- Create an new app using your SID, choose the allowed users and click "Create an app"
![Create an app](/doc/images/wechatwork-create-app.jpg "Create an app")
- Click on the app that you have just created
- Click on "API enabled to receive messages"
![Configure Event Receiving Server](/doc/images/wechatwork-set-event-receiving-server.jpg "Configure Event Receiving Server")
- Specify the URL, Token and AES encryption key 
- Enable all event types
- Click "Save"

Enterprise needs to provide 
- token defined by enterprise for authenticating the event received from the message receiving URL
- AES Key for encrypting the message

## Receiving events - e.g. Adding an external contact
https://work.weixin.qq.com/api/doc/90000/90135/92130#%E6%B7%BB%E5%8A%A0%E4%BC%81%E4%B8%9A%E5%AE%A2%E6%88%B7%E4%BA%8B%E4%BB%B6

From this API we can get the welcome_code for sending welcome note for the new customer
 
## Send new customer welcome message
Require welcome_code
https://work.weixin.qq.com/api/doc/90000/90135/92137


# Setting up the environment

## Go to a working directory

## Clone the project
git clone https://github.com/jackycct/wechat-work-sample.git

## Lombok for intellij
If you are using Intellij for development, enable Lombok plugin
https://projectlombok.org/setup/intellij

## Enabling Port forwarding 
Enable port forward in your router from external URL to your  callback service
e.g. 
From External Port 8080 To Internal IP Address 192.186.1.123, port 8080

This page provides steps on how to perform port forwarding in major routers https://portforward.com/router.htm

## Identify your public internet IP address from your router
e.g 220.246.105.20

## Validate the port forwarding
Use your browser to access http://220.246.105.20:8080/ping

You should be able to see the "pong"

## Postscript
If you get an illegal key size exception, you need to install java cryptographic extensions. Not required if using Java 9 and beyond. 

## How to deploy to AWS EC2
You should have got a .pem file when creating a EC2 instance. To login 

```ssh -i "<path to pem file>" ec2-user@ec2-100-26-177-98.compute-1.amazonaws.com```

If you get an error 'Permissions for your pem files is too open', do the following:

* In windows explorer, Right click on 'Properties > Security > Advanced > Disable inheritance'
* Agree on 'Convert inherited permissions to explicit permssions'
* Click on 'Edit' permissions in the security tab. 
* Remove all users apart from the logged in user

### Install Java

Run java -version. You should get an error since java should not be installed. 

Run: ```sudo yum install java-11-amazon-corretto.x86_64``` to install a version of Java that already has crypto extensions available. 

To copy files into ~/app directory:
 
```scp -i "<path to pem>" target\wechatwork-sample-0.0.1-SNAPSHOT.jar ec2-user@<EC2 instance public DNS>:~/app/```


[WeChat Work Admin - Customer Contact]: https://work.weixin.qq.com/wework_admin/frame#customer/analysis