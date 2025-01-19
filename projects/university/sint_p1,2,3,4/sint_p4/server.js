const express = require('express');
const fs = require('fs');
const app = express();
const port = 8888;

// http://localhost:8888/customer-list
// http://localhost:8888/register?clogin=user1&cname=User

// Middleware for parsing request bodies
app.use(express.urlencoded({ extended: true }));
app.use(express.json());

// Persistent storage file
const customerFile = 'customers.json';

// Load customer data on startup
let customers = [];
if (fs.existsSync(customerFile)) {
    customers = JSON.parse(fs.readFileSync(customerFile, 'utf8'));
}

// Serve static files (e.g., Practice 1 files)
app.use(express.static('public-html/p1'));

// Route: Initial screen (Practice 1 page)
app.get('/', (req, res) => {
    res.sendFile(__dirname + '/public-html/p1/p1.html');
});

// Route: Customer registration
app.get('/register', (req, res) => {
    const { clogin, cname } = req.query;

    if (clogin && cname) {
        // Update or add the customer
        const existingCustomer = customers.find(c => c.clogin === clogin);
        if (existingCustomer) {
            existingCustomer.cname = cname;
        } else {
            customers.push({ clogin, cname });
        }

        // Save to persistent storage
        fs.writeFileSync(customerFile, JSON.stringify(customers, null, 2));

        // Response
        res.send(`
            <h1>Registration Successful</h1>
            <p>Customer ${cname} (${clogin}) has been registered.</p>
            <a href="/">Return to Home</a><br>
            <a href="/customers">View Registered Customers</a>
        `);
    } else {
        res.send('<h1>Error: Missing customer information.</h1>');
    }
});

// Route: Access customer information page (SPA skeleton)
app.get('/customers', (req, res) => {
    res.send(`
        <html>
            <head><script src="client.js"></script></head>
            <body>
                <h1>Registered Customers</h1>
                <table id="customerTable" border="1">
                    <thead>
                        <tr>
                            <th>Login</th>
                        </tr>
                    </thead>
                    <tbody>
                    </tbody>
                </table>
                <div id="customerInfo"></div>
            </body>
        </html>
    `);
});

// Route: List all customers (JSON response)
app.get('/customer-list', (req, res) => {
    res.json(customers.map(c => ({ clogin: c.clogin })));
});

// Route: Get customer information
app.get('/customer-info', (req, res) => {
    const { clogin } = req.query;
    const customer = customers.find(c => c.clogin === clogin);
    if (customer) {
        res.send(customer.cname);
    } else {
        res.status(404).send('Customer not found.');
    }
});

// Start the server
app.listen(port, () => {
    console.log(`Server running at http://localhost:${port}`);
});
