# MerinoFullStackBank

MerinoFullStackBank is web base banking application using a Rest API backend made with a java jersey container and JDBC to interact with an AWS database for data persistence. 

## Installation

Use [git bash](https://git-scm.com/downloads) to install MerinoFullStackBank. Navigate to your desired directory using
```bash
cd chosenDirectoryPath
```
Then install using

```bash
git clone https://github.com/2105Pega/MerinoProject1.git
```
Open your [Spring Tool Suite 4(Eclipse)](https://spring.io/tools) and set your workspace to chosenDirectoryPath/MerinoProject1. Create a new project and name it MerinoFullStackBank. The files should load to Spring tools. Right-click MerinoFullStackBank and select Maven > Update Project.
Go to Window > Show View > Other then select Servers > server. Click add a new server in your console and select a Tomcat 9 server

## Usage

Expand src/main/java, then com.revature.controller, and open Controller.java then run Controller on Server and select the Tomcat 9 server. Now open http://localhost:8080/MerinoFullStackBank/ in your prefered browser to use the application.

You can interact with the application by typing your reponse to the given prompts and pressing enter.

If you would like to use an existing user you may use these two pre-existing accounts:

* Employee_username=morejonpa; Employee_password=pass
* Customer_username=merinolu; Customer_password=pass

## Contributing
Pull requests are not welcome at the moment. I am preparing to present this project on 6/7/2021.
## About Author
Luis Merino

Bachelor's degree in mathematics from Michigan State University, May 2016.

Current employee of Revature.
## License
[MIT](https://choosealicense.com/licenses/mit/)