let stompClient = Stomp.over(new SockJS('/gs-guide-websocket'));
stompClient.connect({}, () => {
    stompClient.subscribe('/topic/clients/all', (message) => {
        $("#clientTable tbody tr").remove()
        showClients(JSON.parse(message.body));
        // console.log(JSON.parse(message.body))
    } )
    stompClient.send("/app/message/client/getAll", {}, {})
});

const sendClient = () => {
    let client = {};
    client.name = document.getElementById("clientName").value;
    client.address = {}
    client.address.street = document.getElementById("clientAddress").value;
    client.phones = []
    document.getElementById("clientPhones").value.split(",").forEach(p => client.phones.push({number: p}));
    stompClient.send("/app/message/client/save", {}, JSON.stringify(client))
}

const sendGetMessage = () => stompClient.send("/app/message/client/getAll", {}, {})

const showClients = (clients) => clients.forEach((client) => {
    $("#clientTableInner").append(
        "<tr><td>" + client.id +
        "</td><td>" + client.name +
        "</td><td>" + client.address.street +
        "</td><td>" + client.phones.map(p => p.number).join("\n") +
        "</td></tr>")
})

$(function () {
    $("form").on('submit', (event) => {
        event.preventDefault();
    });
    $("#getClients").click(sendGetMessage);
    $("#addClient").click(sendClient);
});
