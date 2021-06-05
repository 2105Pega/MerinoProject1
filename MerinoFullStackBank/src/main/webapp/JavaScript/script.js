
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

    for (let i = 0; i < state.numberOfAccounts; i++) {
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
                    bal.innerHTML = "$" + accountResponse.balance;

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

    const openButton = document.createElement("button");
    openButton.innerHTML = "Open new account";
    customerView.appendChild(openButton);
    openButton.addEventListener("click", renderOpen);

    const logger = document.createElement("p");
    customerView.appendChild(logger);

    const app = document.querySelector("#app");
    app.replaceChild(customerView, document.querySelector("#app").firstElementChild);

}

function renderOpen() {
    const openView = document.createElement("div");
    openView.setAttribute("id", "openView");

    const welcome = document.createElement("h3");
    welcome.innerHTML = "Please complete this form to open a new account " + state.userName + ".";
    openView.appendChild(welcome);


    const form = document.createElement("form");
    form.setAttribute("id", "openForm");
    form.setAttribute("method", "POST");
    const user = state.userName;
    const pass = state.password;
    const url = "http://localhost:8080/MerinoFullStackBank/api/controller/open/" + user + "/" + pass;

    form.setAttribute("action", url);
    form.addEventListener("submit", (event) => {
        const openAcc = {}
        
        
        const radioOptions = document.getElementsByName("accType");
        for(i = 0; i < radioOptions.length; i++) {
            if(radioOptions[i].checked)
            openAcc.accType = radioOptions[i].value;
        }
        openAcc.balance = document.getElementById("balance").value;
        openAcc.joint = document.getElementById("joint").value;


        // disable default action
        event.preventDefault();

        // configure a request
        const userName = state.userName;
        const password = state.password;

        const request = new XMLHttpRequest();
        var url = "http://localhost:8080/MerinoFullStackBank/api/controller/open/" + userName + "/" + password;
        request.open("POST", url);




        // prepare form data
        // let data = new FormData(form);

        // for (var pair of data.entries()) {
        //     console.log(pair[0] + ', ' + pair[1]);
        // }

        // set headers
        // request.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        // request.setRequestHeader('X-Requested-With', 'XMLHttpRequest');

        console.log(JSON.stringify(openAcc))
        request.setRequestHeader("Content-Type", "application/json;charset=UTF-8")
        // send request
        request.send(JSON.stringify(openAcc));

        
        // request.send(data);

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


    });


    const typeMessage = document.createElement("p");
    typeMessage.innerHTML = "Please select the type of account you want to open."
    form.appendChild(typeMessage);

    const checking = document.createElement("input");
    checking.setAttribute("type", "radio");
    checking.setAttribute("name", "accType");
    checking.setAttribute("id", "checking");
    checking.setAttribute("value", "Checking");

    const labelChecking = document.createElement("label");
    labelChecking.setAttribute("for", "checking");
    labelChecking.innerHTML = "Checking";

    form.appendChild(checking);
    form.appendChild(labelChecking);

    const savings = document.createElement("input");
    savings.setAttribute("type", "radio");
    savings.setAttribute("name", "accType");
    savings.setAttribute("id", "savings");
    savings.setAttribute("value", "Savings");

    const labelSavings = document.createElement("label");
    labelSavings.setAttribute("for", "savings");
    labelSavings.innerHTML = "Savings";

    form.appendChild(savings);
    form.appendChild(labelSavings);

    const balanceMessage = document.createElement("p");
    balanceMessage.innerHTML = "Please type the amount of your initial deposit. <br>";
    form.appendChild(balanceMessage);

    const dollarSign = document.createElement("span");
    dollarSign.innerHTML = "$";
    form.appendChild(dollarSign);

    const balance = document.createElement("input");
    balance.setAttribute("type", "number");
    balance.setAttribute("id", "balance");
    balance.setAttribute("name", "balance");
    balance.setAttribute("step", "0.01");
    form.appendChild(balance);

    const jointMessage = document.createElement("p");
    jointMessage.innerHTML = "Please add the user ID of the joint account holder if opening a joint account."
    form.appendChild(jointMessage);

    const joint = document.createElement("input");
    joint.setAttribute("type", "number");
    joint.setAttribute("name", "joint");
    joint.setAttribute("id", "joint");
    form.appendChild(joint);

    form.appendChild(document.createElement("br"));
    form.appendChild(document.createElement("br"));
    const submit = document.createElement("button");
    submit.setAttribute("type", "submit");
    submit.setAttribute("role", "button");
    submit.innerHTML = "Submit account application";
    form.appendChild(submit);

    const returnCustomer = document.createElement("button");
    returnCustomer.innerHTML = "Return to account view";
    returnCustomer.addEventListener("click", (event) => {
        renderCustomer(state);
    })
    form.appendChild(returnCustomer);

    openView.appendChild(form);

    const log = document.createElement("p");
    openView.appendChild(log);

    const app = document.querySelector("#app");
    app.replaceChild(openView, document.querySelector("#app").firstElementChild);
}
// function openSubmit(event) {
//     event.preventDefault();
//     console.log("called loginSubmit");
//     // const user = {};
//     // user.userID = 0;
//     // user.userName= document.getElementById("username").value;
//     // user.password= document.getElementById("password").value;
//     // user.firstName="default";
//     // user.lastName="default";
//     // user.userType = 1;

//     const userName = document.getElementById("username").value;
//     const password = document.getElementById("password").value;

//     const request = new XMLHttpRequest();

//     request.onreadystatechange = function () {

//         if (this.readyState == 4) {
//             const serverResponse = JSON.parse(this.response);
//             console.log(JSON.parse(this.response))
//             console.log(serverResponse);
//             if (serverResponse.fail == true) {
//                 log.textContent = serverResponse.warning;
//             } else {
//                 console.log(serverResponse.userName);
//                 state = serverResponse;
//                 log.textContent = state.userName;
//                 renderCustomer(state);
//             }


//         }
//     }
//     var url = "http://localhost:8080/MerinoFullStackBank/api/controller/login/" + userName + "/" + password;
//     request.open("GET", url);
//     // console.log(JSON.stringify(user))
//     // request.setRequestHeader("Content-Type", "application/json;charset=UTF-8")
//     // request.send(JSON.stringify(user));
//     request.send();
//     log.textContent = `Submitted! Time stamp: ${event.timeStamp}`;
// }