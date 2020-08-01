<template>
  <v-app id="inspire">
    <v-content>
      <v-container
        class="fill-height"
        fluid
      >
          <v-row
          align="center"
          justify="center"
        >
          <v-col
            cols="12"
            sm="8"
            md="4"
          >
            <v-card class="elevation-12">
              <v-toolbar
                color="primary"
                dark
                flat
              >
                <v-toolbar-title>Login</v-toolbar-title>
                <v-spacer></v-spacer>
              </v-toolbar>
              <v-card-text>
                <v-form>
                  <v-text-field
                    @input="checkDisabled()"
                    v-model="username"
                    label="Login"
                    name="login"
                    prepend-icon="person"
                    type="text"
                    :error-messages="userErrorMessage"
                    :error="userError"
                  ></v-text-field>
                  <v-text-field
                    @input="checkDisabled()"
                    v-model="password"
                    id="password"
                    label="Password"
                    name="password"
                    prepend-icon="lock"
                    :error-messages="passwordErrorMessage"
                    :error="passwordError"
                    type="password"
                  ></v-text-field>
                </v-form>
              </v-card-text>
              <v-card-actions>
                <v-alert 
                color="error" icon="error"
                text
                light
                elevation:0
                dense
                v-if="error">
                {{errorMessage}}
                </v-alert>  
                <v-spacer ></v-spacer>
                <v-btn color="primary" 
                @click="checkLogin()"                   
                :disabled= "disabled"
                >Login</v-btn>
              </v-card-actions>
            </v-card>
          </v-col>
        </v-row>
  
      </v-container>
    </v-content>
  </v-app>
  
</template>

<script>
import Api from '../services/api.js'
import sha256 from 'crypto-js/sha256'
 export default {
    props: {
      source: String,
    },
    methods:{
        eve(event){
          if(event.keyCode === 13) {
            this.checkLogin();
          }
        }, 
        checkDisabled(){
            this.disabled = this.password == "" || this.username == ""
        },
        checkLogin(){
           
            if(!this.disabled){
              //var req = JSON.stringify("name: '" + this.username + "', pass: '" + sha256(this.password).toString()+"'")
            Api().post('/Login', {
              name: this.username,
              pass: sha256(this.password).toString()
            }).then((response)=>{
                console.log(response.data)
                var user;
                this.error = response.data.error;
                this.errorMessage = response.data.errorMessage;
                if(this.error === false && this.errorMessage === ""){
                    this.userError= false;
                    this.passwordError = false;
                    this.passwordErrorMessage="";
                    this.userErrorMessage="";
                    this.login = true;
                    user = {
                      name  : response.data.userName,
                      profileName: response.data.profileName,
                      id: response.data.ID
                    }
                  }
                else {
                        this.login = false;
                        this.userError= true;
                        this.passwordError = true;
                    } 
                
               
                this.$emit('isLogged', this.login, user)
            })
            }
            else{
          //    var this = this;
              if (this.username == ""){
                this.userError= true;
                this.userErrorMessage = "Inserisci il nome utente"
              }
              if(this.password === ""){
                this.passwordError = true;
                this.passwordErrorMessage = "Inserisci una password";

              }
            }
            
        }
    },
    data(){
        return {
            password: "",
            username: "",
            disabled: true,
            login: null,
            error: false,
            errorMessage: "",
            userErrorMessage:"",
            userError:false,
            passwordErrorMessage:"",
            passwordError:false
        }
    },
    mounted(){

      window.addEventListener('keyup', this.eve);
    },
    beforeDestroy(){
      window.removeEventListener('keyup',this.eve)
    }

 }
</script>

