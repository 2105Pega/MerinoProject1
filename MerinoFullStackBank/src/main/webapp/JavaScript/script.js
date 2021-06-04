
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
            const serverResponse = JSON.parse(this.response);
            console.log(JSON.parse(this.response))
            console.log(serverResponse);
            if(serverResponse.fail == true){
                log.textContent = serverResponse.warning;
            } else {
                console.log(serverResponse.userName);
                state = serverResponse;
                log.textContent = state.userName;
                renderCustomer(state);
            }
            
            
        }
    }
    var url = "http://localhost:8080/MerinoFullStackBank/api/controller/login/" + userName + "/" + password;
    request.open("GET", url);
    // console.log(JSON.stringify(user))
    // request.setRequestHeader("Content-Type", "application/json;charset=UTF-8")
    // request.send(JSON.stringify(user));
    request.send();
    log.textContent = `Submitted! Time stamp: ${event.timeStamp}`;
}

const form = document.getElementById("login");
const log = document.getElementById("log");
form.addEventListener('submit', loginSubmit);

function renderCustomer(state) {
    const customerView = document.createElement("div");
    customerView.setAttribute("id", "customerView");
    const welcome = document.createElement("h3");
    welcome.innerHTML = "Welcome " + state.firstName + " " + state.lastName + ".";
    customerView.appendChild(welcome);
    const personal = document.createElement("p");
    personal.innerHTML = "We have your personal information listed as: <br> " + state.firstName  + " " + state.lastName ;
    customerView.appendChild(personal);

    const app = document.querySelector("#app");
    app.replaceChild(customerView,document.querySelector("#loginView"));

}