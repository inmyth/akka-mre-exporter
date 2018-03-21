# Mr Exchange user exporter

This exporter uses rippled's websocket `account_tx` command on each user address and saves the result to a MySql db. 

The request conforms to latest version of rippled. 
```
{
  "id": 1,
  "command": "account_tx",
  "account": "r9cZA1mLK5R5Am25ArfXFmqgNwjZgnfk59",
  "ledger_index_min": -1,
  "ledger_index_max": -1,
  "limit": 10
}
```

In case of old version or fragmented ledgers i.e complete_ledgers returning holes, then a deprecated field **count** is required.
This version is handled in `count` branch. 


Db definitions, rippled endpoints are stored in `application.conf`. Template is available as reference.

## Attention
As this exporter depends on complete_ledgers, transactions will be missing if the ledgers have gone from complete_ledgers. 
As such it is better to use exporter that reads data from embedded Sqlite : https://github.com/inmyth/akka-mre-tx-exporter


### Build and Run

Run the following in root to package
```
sbt assembly
```
The jar file is created in `./target/scala-xxx/`

To run:
```
java -jar xxx.jar <path to user addresses file> <path to log folder>
```