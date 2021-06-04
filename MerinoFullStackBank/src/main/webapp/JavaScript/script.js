
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

    const request = new XMLHttpRequest();

    request.onreadystatechange = function () {

        if (this.readyState == 4) {
            const serverResponse = JSON.parse(this.response);
            console.log(JSON.parse(this.response))
            console.log(serverResponse);
            if (serverResponse.fail == true) {
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
    welcome.innerHTML = "Welcome " + state.userName + ".";
    customerView.appendChild(welcome);

    const personal = document.createElement("p");
    personal.innerHTML = "We have your personal information listed as: <br> User ID: " + state.userID + " <br> Name: " + state.firstName + " " + state.lastName + " <br> Address: " + state.address + " <br> Phone number: " + state.phone;
    customerView.appendChild(personal);

    const accountHeader = document.createElement("h4");
    accountHeader.innerHTML = "Current Accounts";
    customerView.appendChild(accountHeader);

    const accountTable = document.createElement("table");
    accountTable.setAttribute("id", "accountTable");
    const headRow = accountTable.insertRow(0);

    // Insert new cells (<td> elements) at the 1st and 2nd position of the "new" <tr> element:
    const cell1 = headRow.insertCell(0).outerHTML = "<th>Account number</th>";
    const cell2 = headRow.insertCell(1).outerHTML = "<th>Account type</th>";
    const cell3 = headRow.insertCell(2).outerHTML = "<th>Account balance</th>";
    const cell4 = headRow.insertCell(3).outerHTML = "<th>Account status</th>";
    // Add some text to the new cells:
    // cell1.innerHTML = "NEW CELL1";
    // cell2.innerHTML = "NEW CELL2";

    for(let i = 0; i < state.numberOfAccounts; i++){
        const accRequest = new XMLHttpRequest();

        accRequest.onreadystatechange = function () {

            if (this.readyState == 4) {
                const serverResponse = JSON.parse(this.response);
                console.log(JSON.parse(this.response))
                console.log(serverResponse);
                if (serverResponse.fail == true) {
                    logger.textContent = serverResponse.warning;
                } else {
                    console.log(serverResponse.accountNumber);
                    accountResponse = serverResponse;
                    const accTab = document.querySelector("#accountTable");

                    const row = accTab.insertRow();

                    const accNum = row.insertCell(0);
                    accNum.innerHTML = accountResponse.accountNumber;

                    const accType = row.insertCell(1);
                    accType.innerHTML = accountResponse.accountType;

                    const bal = row.insertCell(2);
                    bal.innerHTML = accountResponse.balance;

                    const stat = row.insertCell(3);
                    stat.innerHTML = accountResponse.approved;

                }
    
    
            }
        }

        const user = state.userName;
        const password = state.password;
        const acc = state.accountList[i];
        const url = "http://localhost:8080/MerinoFullStackBank/api/controller/account/" + user + "/" + password + "/" + acc;
        accRequest.open("GET", url);
        accRequest.send();

    }
    customerView.appendChild(accountTable);

    const logger = document.createElement("p");
    customerView.appendChild(logger);

    const app = document.querySelector("#app");
    app.replaceChild(customerView, document.querySelector("#app").firstElementChild);

}