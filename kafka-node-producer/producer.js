var kafka = require('kafka-node');
var Producer = kafka.Producer;
var Client = kafka.Client;
//var client = new Client('localhost:2181');
var client = new Client('ADDYOURZOOKEEPERURLHERE');
var topic = 'test2'; // configure according to your own topic
var producer = new Producer(client, { requireAcks: 1 });

var testObject = {
  CCN : '111222333CCN',
  tripID : '222333444TripID',
  millisecondsSinceEpoch : 1000,
  latitude : "1.01234567",
  longitude : "12.01234567"
}

var payloadSendCounter = 0;
let PAYLOADSENDLIMIT = 100;

producer.on('ready', function () {
   setInterval(function(){sendPayload(producer, testObject)}, 2000);
 });

producer.on('error', function (err) {
  console.log('error', err);
});

function sendPayload(producer, testObject){ 
  console.log('send payload index - ' + payloadSendCounter);
  payloadSendCounter++;
  var p = 0;
  var a = 0;
  producer.send([
    { topic: topic, partition: p, messages: [testObject.CCN, testObject.tripID, testObject.millisecondsSinceEpoch + (new Date).getTime(),
                                      testObject.latitude, testObject.longitude], attributes: a }
     ], function (err, result) {
    console.log("success");
    console.log(err || result);

    // end after a predefined number of payload triggers
    if(payloadSendCounter == PAYLOADSENDLIMIT)
      process.exit();
  });
}