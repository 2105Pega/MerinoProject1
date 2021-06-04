
let state;

function loginSubmit(event) {
    event.preventDefault();
    console.log("called loginSubmit");
    // const user = {};
    // user.userID = 0;
    // user.userName= document.getElementById("username").value;
    // user.password= document.getElementById("password").value;
    // user.firstName="default";
    // user.lastName="default";
    // user.userType = 1;
    
    const userName = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    var request = new XMLHttpRequest();

    request.onreadystatechange = function () {

        if (this.readyState == 4) {
            var userResponse = JSON.parse(this.response);
            if(userResponse.fail = true){
                log.textContent = userResponse.warning;
            }
            
            
        }
    }
    var url = "http://localhost:8080/MerinoFullStackBank/api/controller/login/" + userName + "/" + password;
    request.open("GET", url);
    // console.log(JSON.stringify(user))
    // request.setRequestHeader("Content-Type", "application/json;charset=UTF-8")
    // request.send(JSON.stringify(user));
    request.send();
    log.textContent = `Form Submitted! Time stamp: ${event.timeStamp}`;
}

const form = document.getElementById("login");
const log = document.getElementById("log");
form.addEventListener('submit', loginSubmit);