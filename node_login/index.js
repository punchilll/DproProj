/*
Author: 60011212182
*/

var crypto = require('crypto');
var uuid = require('uuid');
var express = require('express');
var mysql = require('mysql');
const bodyParser = require('body-parser')
//var bodyParser = require('body-parser');
const { read } = require('fs');
var url = require('url');
const querystring = require('querystring');
// var url_parts = url.parse(request.url, true);
// var query = url_parts.query;

var con = mysql.createConnection({
    host:'localhost',
    user: 'root',
    password: '',
    database: 'dpro_mb'
});

con.connect ( function ( error ) 
    { 
        if ( error ) 
        {
            console.log ( error );
        }
        else 
        {
            console.log ( "MySQL Connected!" );
        }
    } 
);

var app=express();
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));

app.post('/register/',(req,res,next)=>{
    var post_data = req.body;

    //var uid = uuid.v4();
    var username = post_data.username;

    var password = post_data.password;
    // var hash_data = saltHashPassword(password);
    // password = hash_data.passwordHash;
    // var salt = hash_data.salt;

    var f_name = post_data.f_name;
    var l_name = post_data.l_name;
    var tel = post_data.tel;
    var email = post_data.email;

    //res.json(username+" "+password);

    con.query('select * from user where username=?',[username],function(error,result,fields){
        con.on('error',function(err){
            console.log('[MySQL ERROR]',err);
        });
        if(result && result.length){
            res.json('User already axist!!!');
        }
        else{
            con.query('INSERT INTO `user`( `username`, `password`, `f_name`, `l_name`, `tel`, `email`) values (?,?,?,?,?,?)',[username,password,f_name,l_name,tel,email],function(err,result,fields){
                con.on('error',function(err){
                    console.log('[MySQL ERROR]',err);
                    res.json('Register error',err);
                });
                res.json('Register successful');
            })
        }
        
    })
})

app.post('/login/',(req,res,next)=>{
    var post_data = req.body;

    var username = post_data.username;
    var password = post_data.password;

    con.query('SELECT * FROM user where username=?',[username],function(error,result,fields){
        con.on('error',function(err){
            console.log('[MySQL ERROR]',err);
        });
        if(result && result.length){
            if(password == result[0].password)
                res.end(JSON.stringify(result[0]))
            else
                res.end(JSON.stringify('Wrong password'));
        }
        else{
            res.json('User not exists!!');
        }
        
    })
})

app.post('/service_charge/',(req,res,next)=>{
    
    var post_data = req.body;

    var size = post_data.size;
    var service_charge = post_data.service_charge;
    var note = post_data.note;
    var pic_evidence = post_data.pic_evidence;
    var created_at = post_data.created_at;

    con.query('INSERT INTO `service_charge`( `size`, `service_charge`, `note`, `pic_evidence`, `created_at`) values (?,?,?,?,NOW())',[size,service_charge,note,pic_evidence,created_at],function(err,result,fields){
        con.on('error',function(err){
            console.log('[MySQL ERROR]',err);
            res.json('Charge service error',err);
        });
        res.json('Charge service Successful');
    })
})

app.post('/history/',(req,res,next)=>{
    var post_data = req.body;

    var date = post_data.created_at;

    con.query('SELECT * FROM service_charge where DATE(created_at)=?',[date],function(error,result,fields){
        con.on('error',function(err){
            console.log('[MySQL ERROR]',err);
        });
        if(result && result.length){
            res.end(JSON.stringify(result));
        }
        else{
            res.json('SC not exists!!');
        }
        
    })
})

app.listen(8888,()=>{
    console.log("Puchill running port 8888");
})