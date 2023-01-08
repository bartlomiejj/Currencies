THIS IS JUST AN EXAMPLE PORTFOLIO PROJECT. BUSINESS VALUE MIGHT BE LOW :) 

# Currencies Service

Responsibilities:
1. api/currencies/refresh - download currencies from NBP and save in DB. Refresh on every application startup also. 
2. api/currencies/calculate - calculate target currency value. 

Example request:

  <code>{
  "sourceValue": 1000.00,
  "sourceCurrency": "EUR",
  "targetCurrency": "USD"
}</code>

Example response:

 <code>1062.30</code>
