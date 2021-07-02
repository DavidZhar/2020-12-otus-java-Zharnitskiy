<!DOCTYPE html>
<html>
<head>
    <title>Clients</title>
</head>

<body>
<h3>Clients:</h3>
<table style="width: 400px" border="2">
    <thead>
    <tr>
        <td style="width: 100px">Id</td>
        <td style="width: 100px">Name</td>
        <td style="width: 100px">Address</td>
        <td style="width: 150px">Phones</td>
    </tr>
    </thead>
    <#list clients as client>
        <tbody>
        <tr>
            <td>${client.id}</td>
            <td>${client.name}</td>
            <td>${client.address.street}</td>
            <td>
                <#list client.phones as phone>
                    ${phone.number}<br>
                </#list>
            </td>
        </tr>
        </tbody>
    </#list>
</table>
<br>
<h4>Add a client</h4>
<div>
    <br/>
    <form action="/client" method="post">
        <label>
            Name<br/>
            <input type="text" name="name">
        </label>
        <br/>
        <label>
            Address<br/>
            <input type="text" name="address">
        </label>
        <br/>
        <label>
            Phones (separated by comma)<br/>
            <input type="text" name="phones">
        </label>
        <br/>
        <br/>
        <input type="submit" value="ADD">
    </form>
</div>
</body>
</html>
