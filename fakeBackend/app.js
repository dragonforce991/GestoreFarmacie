var express = require('express')
const bodyParser = require('body-parser')
const cors = require('cors')
const morgan = require('morgan')

var app = express();

app.use(morgan('combine'));
app.use(bodyParser.json());
app.use(cors());


app.get('', function(req,res){
  res.send('hello');
})
app.post('/login', function(req,res){
    console.log(req);
    if(req.body.user == 'admin' && req.body.pass == 'admin'){
      var response = {
        error: false,
        username: 'admin',
        profileName: 'REG'
      }
        res.send(response);
    }
    res.send(
      {
        error:true,
        errorMessage:"Invalid Login. Please Retry!",
        profileName: "Reg"
      });
        
})

app.listen(3000, function () {
  console.log('Example app listening on port 3000!');
});
