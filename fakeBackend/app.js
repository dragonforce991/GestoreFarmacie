var express = require('express')
const bodyParser = require('body-parser')
const cors = require('cors')
const morgan = require('morgan')
const fs = require('fs')
var app = express();

app.use(morgan('combine'));
app.use(bodyParser.json());
app.use(cors());

const adminPass = "8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918";
const TFPass = "63ee3b90f0bc64026eafa8cde95c5f410c847be841536b7240d778688cfed72a";
app.get('', function(req,res){
  res.send('hello');
})
app.post('/Login', function(req,res){
    fs.writeFileSync("./logRequest.txt", JSON.stringify(req.headers)+"\n"+JSON.stringify(req.body));
    
    if(req.body.name == 'admin' && req.body.pass == adminPass){
      var response = {
        error: false,
        username: 'admin',
        profileName: 'REG'
      }
        res.send(response);
    }
    if(req.body.user == 'TF' && req.body.pass == TFPass){
      var response = {
        error: false,
        username: 'TF',
        profileName: 'TF'
      }
        res.send(response);
    }
    res.send(
      {
        error:true,
        errorMessage:"Invalid Login. Please Retry!",
        //profileName: "Reg"
      });
        
})

app.listen(3000, function () {
  console.log('Example app listening on port 3000!');
});
