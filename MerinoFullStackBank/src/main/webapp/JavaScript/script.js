
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
            } else if (serverResponse.userType == 1) {
                console.log(serverResponse.userName);
                state = serverResponse;
                log.textContent = state.userName;
                renderCustomer(state);
            } else {
                console.log(serverResponse.userName);
                state = serverResponse;
                log.textContent = state.userName;
                renderEmployee(state);
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

const create = document.getElementById("create");
create.addEventListener("click", (event) => {
    renderCreate();
})


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

    const withdrawButton = document.createElement("button");
    withdrawButton.innerHTML = "Withdraw";
    customerView.appendChild(withdrawButton);
    withdrawButton.addEventListener("click", renderWithdraw);

    const depositButton = document.createElement("button");
    depositButton.innerHTML = "Deposit";
    customerView.appendChild(depositButton);
    depositButton.addEventListener("click", renderDeposit);

    const transferButton = document.createElement("button");
    transferButton.innerHTML = "Transfer";
    customerView.appendChild(transferButton);
    transferButton.addEventListener("click", renderTransfer);

    const logger = document.createElement("p");
    customerView.appendChild(logger);

    const transaction = document.createElement("div");
    transaction.setAttribute("id", "transaction");
    customerView.appendChild(transaction);

    const placeholder = document.createElement("p");
    transaction.appendChild(placeholder);

    const app = document.querySelector("#app");
    app.replaceChild(customerView, document.querySelector("#app").firstElementChild);

}

function renderEmployee(state) {
    const employeeView = document.createElement("div");
    employeeView.setAttribute("id", "employeeView");

    const welcome = document.createElement("h3");
    welcome.innerHTML = "Welcome " + state.firstName + " " + state.lastName + ".";
    employeeView.appendChild(welcome);
    employeeView.appendChild(document.createElement("hr"));

    for (var customer of state.employeeCustomerList) {

        const userMessage = document.createElement("h4");
        userMessage.innerHTML = "We have a customer listed under username: " + customer.userName + ".";
        employeeView.appendChild(userMessage);

        const personal = document.createElement("p");
        personal.innerHTML = "We have their personal information listed as: <br> User ID: " + customer.userID + " <br> Name: " + customer.firstName + " " + customer.lastName + " <br> Address: " + customer.address + " <br> Phone number: " + customer.phone;
        employeeView.appendChild(personal);

        const accountHeader = document.createElement("h4");
        accountHeader.innerHTML = "Current Accounts";
        employeeView.appendChild(accountHeader);

        const accountTable = document.createElement("table");
        accountTable.setAttribute("id", "accountTable" + customer.userID);
        const headRow = accountTable.insertRow(0);

        // Insert new cells (<td> elements) at the 1st and 2nd position of the "new" <tr> element:
        const cell1 = headRow.insertCell(0).outerHTML = "<th>Account number</th>";
        const cell2 = headRow.insertCell(1).outerHTML = "<th>Account type</th>";
        const cell3 = headRow.insertCell(2).outerHTML = "<th>Account balance</th>";
        const cell4 = headRow.insertCell(3).outerHTML = "<th>Account status</th>";
        // Add some text to the new cells:
        // cell1.innerHTML = "NEW CELL1";
        // cell2.innerHTML = "NEW CELL2";

        for (let i = 0; i < customer.numberOfAccounts; i++) {
            console.log(i);
            console.log(customer.accountList[i]);
            console.log(customer);
            const id = accountTable.getAttribute("id");
            (function (id) {


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
                            const accTab = document.querySelector("#" + id);

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

                const user = customer.userName;
                const password = customer.password;
                const acc = customer.accountList[i];
                const url = "http://localhost:8080/MerinoFullStackBank/api/controller/account/" + user + "/" + password + "/" + acc;
                accRequest.open("GET", url);
                accRequest.send();

            })(id);


        }
        employeeView.appendChild(accountTable);
        employeeView.appendChild(document.createElement("hr"));
    }
    const app = document.querySelector("#app");
    app.replaceChild(employeeView, document.querySelector("#app").firstElementChild);
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
        for (i = 0; i < radioOptions.length; i++) {
            if (radioOptions[i].checked)
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

function renderCreate() {
    const createView = document.createElement("div");
    createView.setAttribute("id", "createView");

    const welcome = document.createElement("h3");
    welcome.innerHTML = "Please complete this form to create a new user.";
    createView.appendChild(welcome);


    const form = document.createElement("form");
    form.setAttribute("id", "createForm");
    form.setAttribute("method", "POST");
    const url = "http://localhost:8080/MerinoFullStackBank/api/controller/create";

    form.setAttribute("action", url);
    form.addEventListener("submit", (event) => {
        event.preventDefault();
        const createCus = {}


        createCus.user = document.getElementById("user").value;
        createCus.password = document.getElementById("password").value;
        createCus.fName = document.getElementById("fName").value;
        createCus.lName = document.getElementById("lName").value;
        const confirmPassword = document.getElementById("confirmPassword").value;
        if (confirmPassword != createCus.password) {
            log.textContent = "The passwords didn't match";
            return;
        }

        // disable default action
        event.preventDefault();

        // configure a request


        const request = new XMLHttpRequest();
        var url = "http://localhost:8080/MerinoFullStackBank/api/controller/create";
        request.open("POST", url);




        // prepare form data
        // let data = new FormData(form);

        // for (var pair of data.entries()) {
        //     console.log(pair[0] + ', ' + pair[1]);
        // }

        // set headers
        // request.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        // request.setRequestHeader('X-Requested-With', 'XMLHttpRequest');

        console.log(JSON.stringify(createCus))
        request.setRequestHeader("Content-Type", "application/json;charset=UTF-8")
        // send request
        request.send(JSON.stringify(createCus));


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


    const userMessage = document.createElement("p");
    userMessage.innerHTML = "Please type your desired username.";
    form.appendChild(userMessage);

    const user = document.createElement("input");
    user.setAttribute("type", "text");
    user.setAttribute("id", "user");
    user.setAttribute("name", "user");
    user.setAttribute("maxlength", "26");
    user.required = true;
    form.appendChild(user);

    const passwordMessage = document.createElement("p");
    passwordMessage.innerHTML = "Please type your desired password. <br>";
    form.appendChild(passwordMessage);


    const password = document.createElement("input");
    password.setAttribute("type", "password");
    password.setAttribute("id", "password");
    password.setAttribute("name", "password");
    password.setAttribute("maxlength", "26");
    password.required = true;
    form.appendChild(password);

    const passwordMessage2 = document.createElement("p");
    passwordMessage2.innerHTML = "Please confirm your desired password. <br>";
    form.appendChild(passwordMessage2);


    const confirmPassword = document.createElement("input");
    confirmPassword.setAttribute("type", "password");
    confirmPassword.setAttribute("id", "confirmPassword");
    confirmPassword.setAttribute("name", "confirmPassword");
    confirmPassword.setAttribute("maxlength", "26");
    confirmPassword.required = true;
    form.appendChild(confirmPassword);

    const fNameMessage = document.createElement("p");
    fNameMessage.innerHTML = "Please write your first name. <br>"
    form.appendChild(fNameMessage);

    const fName = document.createElement("input");
    fName.setAttribute("type", "text");
    fName.setAttribute("id", "fName");
    fName.setAttribute("name", "fName");
    fName.setAttribute("maxlength", "26");
    fName.required = true;
    form.appendChild(fName);

    const lNameMessage = document.createElement("p");
    lNameMessage.innerHTML = "Please write your last name. <br>"
    form.appendChild(lNameMessage);

    const lName = document.createElement("input");
    lName.setAttribute("type", "text");
    lName.setAttribute("id", "lName");
    lName.setAttribute("name", "lName");
    lName.setAttribute("maxlength", "26");
    lName.required = true;
    form.appendChild(lName);

    form.appendChild(document.createElement("br"));
    form.appendChild(document.createElement("br"));
    const submit = document.createElement("button");
    submit.setAttribute("type", "submit");
    submit.setAttribute("role", "button");
    submit.innerHTML = "Submit account application";
    form.appendChild(submit);

    const returnCustomer = document.createElement("button");
    returnCustomer.innerHTML = "Return to login view";
    returnCustomer.addEventListener("click", (event) => {
        window.location.href = "http://localhost:8080/MerinoFullStackBank/";
    })
    form.appendChild(returnCustomer);

    createView.appendChild(form);

    const log = document.createElement("p");
    createView.appendChild(log);

    const app = document.querySelector("#app");
    app.replaceChild(createView, document.querySelector("#app").firstElementChild);
}

function renderWithdraw() {
    const withdrawView = document.createElement("div");
    withdrawView.setAttribute("id", "withdrawView");

    const amountText = document.createElement("p");
    amountText.innerHTML = "Please type the amount you wish to withdraw. <br>";
    withdrawView.appendChild(amountText);

    const dollarSign = document.createElement("span");
    dollarSign.innerHTML = "$";
    withdrawView.appendChild(dollarSign);

    const amount = document.createElement("input");
    amount.setAttribute("type", "number");
    amount.setAttribute("id", "amount");
    amount.setAttribute("name", "amount");
    amount.setAttribute("step", "0.01");
    amount.required = true;
    withdrawView.appendChild(amount);

    const numberMessage = document.createElement("p");
    numberMessage.innerHTML = "Please type the account number you wish to withdraw from."
    withdrawView.appendChild(numberMessage);

    const accNumber = document.createElement("input");
    accNumber.setAttribute("type", "number");
    accNumber.setAttribute("name", "accNumber");
    accNumber.setAttribute("id", "accNumber");
    accNumber.required = true;
    withdrawView.appendChild(accNumber);

    withdrawView.appendChild(document.createElement("br"));
    withdrawView.appendChild(document.createElement("br"));
    const withdraw = document.createElement("button");
    withdraw.setAttribute("type", "button");
    withdraw.innerHTML = "Withdraw funds";

    withdraw.addEventListener("click", (event) => {
        event.preventDefault();

        if (document.getElementById("amount").value == null || document.getElementById("amount").value == 0 || document.getElementById("accNumber").value == null || document.getElementById("accNumber").value == 0) {
            log.textContent = "Please enter an amount and account number.";
            return;
        }

        const withAttempt = {}
        withAttempt.userName = state.userName;
        withAttempt.password = state.password;
        withAttempt.amount = document.getElementById("amount").value;
        withAttempt.accNumber = document.getElementById("accNumber").value;


        const request = new XMLHttpRequest();
        var url = "http://localhost:8080/MerinoFullStackBank/api/controller/withdraw";
        request.open("PUT", url);



        console.log(JSON.stringify(withAttempt))
        request.setRequestHeader("Content-Type", "application/json;charset=UTF-8")

        request.send(JSON.stringify(withAttempt));


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


    })
    withdrawView.appendChild(withdraw);

    const log = document.createElement("p");
    withdrawView.appendChild(log);

    const transaction = document.querySelector("#transaction");
    transaction.replaceChild(withdrawView, document.querySelector("#transaction").firstElementChild);
}

function renderDeposit() {
    const depositView = document.createElement("div");
    depositView.setAttribute("id", "depositView");

    const amountText = document.createElement("p");
    amountText.innerHTML = "Please type the amount you wish to deposit. <br>";
    depositView.appendChild(amountText);

    const dollarSign = document.createElement("span");
    dollarSign.innerHTML = "$";
    depositView.appendChild(dollarSign);

    const amount = document.createElement("input");
    amount.setAttribute("type", "number");
    amount.setAttribute("id", "amount");
    amount.setAttribute("name", "amount");
    amount.setAttribute("step", "0.01");
    amount.required = true;
    depositView.appendChild(amount);

    const numberMessage = document.createElement("p");
    numberMessage.innerHTML = "Please type the account number you wish to deposit into."
    depositView.appendChild(numberMessage);

    const accNumber = document.createElement("input");
    accNumber.setAttribute("type", "number");
    accNumber.setAttribute("name", "accNumber");
    accNumber.setAttribute("id", "accNumber");
    accNumber.required = true;
    depositView.appendChild(accNumber);

    depositView.appendChild(document.createElement("br"));
    depositView.appendChild(document.createElement("br"));
    const deposit = document.createElement("button");
    deposit.setAttribute("type", "button");
    deposit.innerHTML = "Deposit funds";

    deposit.addEventListener("click", (event) => {
        event.preventDefault();

        if (document.getElementById("amount").value == null || document.getElementById("amount").value == 0 || document.getElementById("accNumber").value == null || document.getElementById("accNumber").value == 0) {
            log.textContent = "Please enter an amount and account number.";
            return;
        }

        const depAttempt = {}
        depAttempt.userName = state.userName;
        depAttempt.password = state.password;
        depAttempt.amount = document.getElementById("amount").value;
        depAttempt.accNumber = document.getElementById("accNumber").value;


        const request = new XMLHttpRequest();
        var url = "http://localhost:8080/MerinoFullStackBank/api/controller/deposit";
        request.open("PUT", url);



        console.log(JSON.stringify(depAttempt))
        request.setRequestHeader("Content-Type", "application/json;charset=UTF-8")

        request.send(JSON.stringify(depAttempt));


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


    })
    depositView.appendChild(deposit);

    const log = document.createElement("p");
    depositView.appendChild(log);

    const transaction = document.querySelector("#transaction");
    transaction.replaceChild(depositView, document.querySelector("#transaction").firstElementChild);
}

function renderTransfer() {
    const transferView = document.createElement("div");
    transferView.setAttribute("id", "transferView");

    const amountText = document.createElement("p");
    amountText.innerHTML = "Please type the amount you wish to transfer. <br>";
    transferView.appendChild(amountText);

    const dollarSign = document.createElement("span");
    dollarSign.innerHTML = "$";
    transferView.appendChild(dollarSign);

    const amount = document.createElement("input");
    amount.setAttribute("type", "number");
    amount.setAttribute("id", "amount");
    amount.setAttribute("name", "amount");
    amount.setAttribute("step", "0.01");
    amount.required = true;
    transferView.appendChild(amount);

    const senderMessage = document.createElement("p");
    senderMessage.innerHTML = "Please type the account number you wish to transfer from."
    transferView.appendChild(senderMessage);

    const senderNumber = document.createElement("input");
    senderNumber.setAttribute("type", "number");
    senderNumber.setAttribute("name", "senderNumber");
    senderNumber.setAttribute("id", "senderNumber");
    senderNumber.required = true;
    transferView.appendChild(senderNumber);

    const receiverMessage = document.createElement("p");
    receiverMessage.innerHTML = "Please type the account number you wish to transfer into."
    transferView.appendChild(receiverMessage);

    const receiverNumber = document.createElement("input");
    receiverNumber.setAttribute("type", "number");
    receiverNumber.setAttribute("name", "receiverNumber");
    receiverNumber.setAttribute("id", "receiverNumber");
    receiverNumber.required = true;
    transferView.appendChild(receiverNumber);

    transferView.appendChild(document.createElement("br"));
    transferView.appendChild(document.createElement("br"));
    const transfer = document.createElement("button");
    transfer.setAttribute("type", "button");
    transfer.innerHTML = "Transfer funds";

    transfer.addEventListener("click", (event) => {
        event.preventDefault();

        if (document.getElementById("amount").value == null || document.getElementById("amount").value == 0 || document.getElementById("senderNumber").value == null || document.getElementById("senderNumber").value == 0 || document.getElementById("receiverNumber").value == null || document.getElementById("receiverNumber").value == 0) {
            log.textContent = "Please enter an amount and both account numbers.";
            return;
        }

        const transAttempt = {}
        transAttempt.userName = state.userName;
        transAttempt.password = state.password;
        transAttempt.amount = document.getElementById("amount").value;
        transAttempt.senderNumber = document.getElementById("senderNumber").value;
        transAttempt.receiverNumber = document.getElementById("receiverNumber").value;


        const request = new XMLHttpRequest();
        var url = "http://localhost:8080/MerinoFullStackBank/api/controller/transfer";
        request.open("PUT", url);



        console.log(JSON.stringify(transAttempt))
        request.setRequestHeader("Content-Type", "application/json;charset=UTF-8")

        request.send(JSON.stringify(transAttempt));


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


    })
    transferView.appendChild(transfer);

    const log = document.createElement("p");
    transferView.appendChild(log);

    const transaction = document.querySelector("#transaction");
    transaction.replaceChild(transferView, document.querySelector("#transaction").firstElementChild);
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