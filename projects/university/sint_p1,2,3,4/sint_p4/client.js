document.addEventListener('DOMContentLoaded', () => {
    // Fetch the list of customers
    fetch('/customer-list')
        .then(response => response.json())
        .then(customers => {
            const tableBody = document.querySelector('#customerTable tbody');
            customers.forEach(customer => {
                const row = document.createElement('tr');
                row.innerHTML = `<td>${customer.clogin}</td>`;
                row.addEventListener('click', () => {
                    // Fetch customer info on click
                    fetch(`/customer-info?clogin=${customer.clogin}`)
                        .then(response => response.text())
                        .then(info => {
                            document.getElementById('customerInfo').innerText = `Customer Name: ${info}`;
                        });
                });
                tableBody.appendChild(row);
            });
        });
});
