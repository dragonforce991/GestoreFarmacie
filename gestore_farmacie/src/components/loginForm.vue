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
                  ></v-text-field>

                  <v-text-field
                    @input="checkDisabled()"
                   
                    v-model="password"
                    id="password"
                    label="Password"
                    name="password"
                    prepend-icon="lock"
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
 export default {
    props: {
      source: String,
    },
    methods:{
        checkDisabled(){
            this.disabled = this.password == "" || this.username == ""
        },
        checkLogin(){
            
            Api().post('/login', {
                user: this.username,
                pass: this.password
            }).then((response)=>{
                console.log(response);
                this.error = response.data.error;
                this.errorMessage = response.data.errorMessage;
                if(this.error === false && this.errorMessage === undefined)
                    this.login = true;
                else 
                    this.login = false; 
                var user = {
                  name  : response.data.username,
                  profileName: response.data.profileName
                }
               
                this.$emit('isLogged', this.login, user)
            })
        }
    },
    data(){
        return {
            password: "",
            username: "",
            disabled: true,
            login: null,
            error: false,
            errorMessage: ""
        }
    }

 }
</script>

